package com.renatosantos.bakingapp.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.renatosantos.bakingapp.R;
import com.renatosantos.bakingapp.model.Ingredient;
import com.renatosantos.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsAdapterViewHolder> {
    private List mIngredientsData;
    private Context context;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);

    }

    public IngredientsAdapter(Context context, List mIngredientsData) {
        this.context = context;
        this.mIngredientsData = mIngredientsData;
    }

    public class IngredientsAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView ingredient;
        TextView quantity;

        public IngredientsAdapterViewHolder(View itemView) {
            super(itemView);

            ingredient = (TextView) itemView.findViewById(R.id.tv_ingredient_name);
            quantity = (TextView) itemView.findViewById(R.id.tv_ingredient_quantity);
        }
    }

    @Override
    public IngredientsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);

        IngredientsAdapterViewHolder holder = new IngredientsAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(IngredientsAdapterViewHolder holder, final int position) {

        Ingredient ingredient = (Ingredient) mIngredientsData.get(position);

        holder.ingredient.setText(ingredient.getIngredient());

        holder.quantity.setText(ingredient.getQuantity() + " " + ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return mIngredientsData.size();
    }

    public void setIngredientsData(List<Ingredient> ingredientData) {
        mIngredientsData = ingredientData;

        notifyDataSetChanged();
    }
}
