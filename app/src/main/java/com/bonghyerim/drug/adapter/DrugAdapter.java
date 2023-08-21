package com.bonghyerim.drug.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bonghyerim.drug.R;
import com.bonghyerim.drug.model.Drug;


import java.util.ArrayList;

public class DrugAdapter extends RecyclerView.Adapter<DrugAdapter.ViewHolder> {

    Context context;
    ArrayList<Drug> DrugList;

    public DrugAdapter(Context context, ArrayList<Drug> drugList) {
        this.context = context;
        DrugList = drugList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drug drug = DrugList.get(position);

        holder.drugNameTextView.setText(drug.itemNameText);
        holder.effectTextView.setText(drug.efcyQesitmText);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(drug);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return DrugList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Drug drug);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView drugNameTextView, effectTextView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            drugNameTextView = itemView.findViewById(R.id.drugNameTextView);
            effectTextView= itemView.findViewById(R.id.effectTextView);
        }
    }
}