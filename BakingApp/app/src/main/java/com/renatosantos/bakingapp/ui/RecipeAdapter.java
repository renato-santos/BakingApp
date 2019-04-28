package com.renatosantos.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.renatosantos.bakingapp.R;
import com.renatosantos.bakingapp.model.Recipe;
import com.renatosantos.bakingapp.model.Step;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {
    private List mRecipesData;
    private Context context;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);

    }

    public RecipeAdapter(Context context, List mRecipeData, ListItemClickListener listener) {
        this.context = context;
        mOnClickListener = listener;
        this.mRecipesData = mRecipeData;
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView name;
        TextView servings;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.iv_recipe_image);
            name = (TextView) itemView.findViewById(R.id.tv_recipe_name);
            servings = (TextView) itemView.findViewById(R.id.tv_recipe_servings);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }

    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);

        RecipeAdapterViewHolder holder = new RecipeAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, final int position) {

        Recipe recipe = (Recipe) mRecipesData.get(position);

        holder.name.setText(recipe.getName());
        holder.servings.setText("x"+ recipe.getServings());

        String imageUrl = recipe.getImage();

        List<Step> steps = recipe.getSteps();

        String videoUrl = steps.get(steps.size()-1).getVideoURL();

        if(imageUrl.isEmpty()){
            if(videoUrl.isEmpty()){
                holder.image.setImageResource(R.drawable.error);
            } else {
                Glide
                        .with(context)
                        .load(Uri.parse(videoUrl))
                        .into(holder.image);
            }

        } else {

            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipesData.size();
    }

    public void setRecipeData(List<Recipe> recipeData) {
        mRecipesData = recipeData;

        notifyDataSetChanged();
    }
}
