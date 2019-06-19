package com.sahil.bakingtut.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sahil.bakingtut.Constants;
import com.sahil.bakingtut.Model.Recipe;
import com.sahil.bakingtut.R;
import com.sahil.bakingtut.StepListActivity;

import java.io.Serializable;
import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewInformation>
{

    Context context;
    List<Recipe> list;
    public MainActivityAdapter(Context context, List<Recipe> list)
    {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewInformation onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewInformation(LayoutInflater.from(context).inflate(R.layout.recipie_details_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewInformation holder, int position)
    {
        holder.recipeNames.setText(list.get(position).getName());
        if(!TextUtils.isEmpty(list.get(position).getImage()))
        {
            Glide.with(context).load(list.get(position).getImage()).into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewInformation extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView recipeNames;
        ImageView imageView;
        public ViewInformation(View itemView) {
            super(itemView);
            recipeNames = itemView.findViewById(R.id.recipe_title);
            imageView = itemView.findViewById(R.id.home_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, StepListActivity.class);
            intent.putExtra(Constants.RECIPIE_TITLE,list.get(position).getName());
            intent.putExtra(Constants.INGREDIENTS_LIST_KEY, (Serializable) list.get(position).getIngredients());
            intent.putExtra(Constants.STEPS_LIST_KEY, (Serializable) list.get(position).getSteps());
            context.startActivity(intent);
        }
    }
}