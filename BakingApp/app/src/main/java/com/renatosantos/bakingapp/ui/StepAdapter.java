package com.renatosantos.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.renatosantos.bakingapp.R;
import com.renatosantos.bakingapp.model.Recipe;
import com.renatosantos.bakingapp.model.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {
    private List mStepData;
    private Context context;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);

    }

    public StepAdapter(Context context, List mStepData, ListItemClickListener listener) {
        this.context = context;
        mOnClickListener = listener;
        this.mStepData = mStepData;
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView description;

        public StepAdapterViewHolder(View itemView) {
            super(itemView);

            description = (TextView) itemView.findViewById(R.id.tv_step_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }

    @Override
    public StepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);

        StepAdapterViewHolder holder = new StepAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(StepAdapterViewHolder holder, final int position) {

        Step step = (Step) mStepData.get(position);

        holder.description.setText(step.getShortDescription());


    }

    @Override
    public int getItemCount() {
        return mStepData.size();
    }

    public void setStepsData(List<Step> stepData) {
        mStepData = stepData;

        notifyDataSetChanged();
    }
}
