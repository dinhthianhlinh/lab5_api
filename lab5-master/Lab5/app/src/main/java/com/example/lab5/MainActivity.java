package com.example.lab5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab5.adapter.Recycle_Item_Distributors;
import com.example.lab5.handle.Item_Distributor_Handle;
import com.example.lab5.model.Distributor;
import com.example.lab5.model.Response;
import com.example.lab5.services.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity implements Item_Distributor_Handle {

    private HttpRequest httpRequest;
    private Recycle_Item_Distributors adapter;
    private Item_Distributor_Handle handle;
    private RecyclerView rclview;
    private EditText edSearch;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handle = new Item_Distributor_Handle() {
            @Override
            public void onDelete(String id) {
                showDeleteConfirmationDialog(id);
            }

            @Override
            public void Update(String id, Distributor distributor) {
                showEditDialog(id,distributor);
            }
        };
        rclview = findViewById(R.id.rclView);
        btnAdd = findViewById(R.id.addButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rclview.setLayoutManager(layoutManager);


//        khởi tạo services Request
        httpRequest = new HttpRequest();
//        thực thi call api
        httpRequest.callApi()
                .getListDistributor()
                .enqueue(getDistributorApi);

        edSearch = findViewById(R.id.edSearch);
//
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Xử lý khi text thay đổi
                String key = s.toString();
                if (!key.isEmpty()) {
                    httpRequest.callApi()
                            .searchDistributor(key) // phương thức api cần thực thi
                            .enqueue(getDistributorApi); // xử lí bất đồng bộ
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý sau khi text thay đổi
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

    }


    private void getData(ArrayList<Distributor> ds) {
        adapter = new Recycle_Item_Distributors(this, ds, handle);
        rclview.setAdapter(adapter);

    }


    public void Delete(String id) {
        httpRequest.callApi()
                .deleteDistributorById(id)
                .enqueue(responseDistributor);
    }

    Callback<Response<ArrayList<Distributor>>> getDistributorApi = new Callback<Response<ArrayList<Distributor>>>() {


        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
//                    lấy data
                    ArrayList<Distributor> ds = response.body().getData();
//                    set dữ liệu lên recycle
                    getData(ds);
//                    Toast ra thôgn tin từ messenger
                    Toast.makeText(MainActivity.this, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {
            Log.d(">>> GetListDistributor", "onFailure" + t.getMessage());
        }
    };

    Callback<Response<Distributor>> responseDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
//                    call lại danh sách
                    httpRequest.callApi()
                            .getListDistributor()
                            .enqueue(getDistributorApi);
                    Toast.makeText(MainActivity.this, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable t) {
            Log.d(">>> GetListDistributor", "onFailure" + t.getMessage());

        }
    };

    private void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        Button buttonConfirm = dialogView.findViewById(R.id.buttonConfirm);

        final AlertDialog dialog = dialogBuilder.create();

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();

                if (!name.isEmpty()) {
                    Distributor distributor = new Distributor();
                    distributor.setName(name);
                    httpRequest.callApi()
                            .addDistributor(distributor)
                            .enqueue(responseDistributor);
                    Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void showDeleteConfirmationDialog(final String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Delete(id);
                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Huỷ bỏ xóa
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showEditDialog(String id, Distributor distributor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Chỉnh sửa thông tin");
        // Inflate layout cho dialog
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update, null);
        builder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.edNameU);
        editTextName.setText(distributor.getName());

        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = editTextName.getText().toString();

                if (!newName.isEmpty()) {
                    Distributor distributor = new Distributor();
                    distributor.setName(newName);
                    httpRequest.callApi()
                            .updateDistributorById(id, distributor)
                            .enqueue(responseDistributor);
                    // Hiển thị thông báo cập nhật thành công
                    Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập tên", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Huỷ bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onDelete(String id) {

    }

    @Override
    public void Update(String id, Distributor distributor) {

    }


}
