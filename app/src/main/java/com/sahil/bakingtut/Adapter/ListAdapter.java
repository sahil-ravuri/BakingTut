package com.sahil.bakingtut.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil.bakingtut.Model.Ingredient;
import com.sahil.bakingtut.R;

import java.util.List;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewInfo>{

    Context context;
    List<Ingredient> lists;

    public ListAdapter(Context context, List<Ingredient> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public ViewInfo onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewInfo(LayoutInflater.from(context).inflate(R.layout.ingredients_entry,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewInfo holder, int position)
    {
        holder.ingredient.setText(lists.get(position).getIngredient());
        holder.measure.setText(lists.get(position).getMeasure());
        holder.quantity.setText(String.valueOf(lists.get(position).getQuantity()));
    }

    @Override
    public int getItemCount()
    {
        return lists.size();
    }

    public class ViewInfo extends RecyclerView.ViewHolder
    {
        TextView ingredient;
        TextView measure,quantity;


        public ViewInfo(View itemView)
        {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredient);
            quantity = itemView.findViewById(R.id.quantity);
            measure = itemView.findViewById(R.id.measure);
        }
    }
}

