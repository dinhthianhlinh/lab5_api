package com.example.lab5.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.MainActivity;
import com.example.lab5.R;
import com.example.lab5.handle.Item_Distributor_Handle;
import com.example.lab5.model.Distributor;
import com.example.lab5.model.Response;
import com.example.lab5.services.HttpRequest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class Recycle_Item_Distributors extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Distributor> ds;
    private Item_Distributor_Handle handle;


    public Recycle_Item_Distributors(Activity activity, ArrayList<Distributor> ds, Item_Distributor_Handle handle) {
        this.context = activity;
        this.ds = ds;
        this.handle = handle;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_distributor, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Distributor distributor = ds.get(position);

        MyViewHolder viewHolder = (MyViewHolder) holder;

        // Set data to views
        viewHolder.tvName.setText(distributor.getName());
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handle.onDelete(distributor.getId());
            }
        });
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Distributor distributor=ds.get(position);
                handle.Update(distributor.getId(), distributor);
            }
        });


    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId;
        ImageView btnDelete, btnEdit;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txtName);
            tvId = itemView.findViewById(R.id.tvId);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }


}
