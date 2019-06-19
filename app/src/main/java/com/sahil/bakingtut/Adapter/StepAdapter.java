package com.sahil.bakingtut.Adapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sahil.bakingtut.Constants;
import com.sahil.bakingtut.Model.Step;
import com.sahil.bakingtut.R;
import com.sahil.bakingtut.StepDetailActivity;
import com.sahil.bakingtut.StepDetailFragment;
import com.sahil.bakingtut.StepListActivity;

import java.io.Serializable;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewInfor>
{

    StepListActivity stepsListActivity;
    List<Step> lists;
    public boolean mTwoPane;

    public StepAdapter(StepListActivity context, List<Step> lists, boolean mTwoPane)
    {
        this.stepsListActivity = context;
        this.lists = lists;
        this.mTwoPane = mTwoPane;
    }



    @NonNull
    @Override
    public ViewInfor onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewInfor(LayoutInflater.from(stepsListActivity).inflate(R.layout.steps_list_entry,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewInfor holder, int position)
    {
        holder.steps.setText(lists.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewInfor extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView steps;
        public ViewInfor(View itemView)
        {
            super(itemView);
            steps = itemView.findViewById(R.id.step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(mTwoPane)
            {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STEP_LIST_ACTIVITY_EXTRA_KEY, (Serializable) lists);
                bundle.putInt(Constants.POSITION_KEY,position);
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                stepDetailFragment.setArguments(bundle);
                stepsListActivity.getSupportFragmentManager().beginTransaction().replace(R.id.step_detail_container,stepDetailFragment).commit();
            }
            else
            {

                Intent intent = new Intent(stepsListActivity, StepDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.STEP_LIST_ACTIVITY_EXTRA_KEY, (Serializable) lists);
                bundle.putInt(Constants.POSITION_KEY,position);
                intent.putExtra(Constants.BUNDLE_KEY,bundle);
                stepsListActivity.startActivity(intent);
            }

        }
    }
}
