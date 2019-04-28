package com.renatosantos.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.renatosantos.bakingapp.R;
import com.renatosantos.bakingapp.model.Ingredient;
import com.renatosantos.bakingapp.model.Recipe;
import com.renatosantos.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailListFragment extends Fragment  {

    @BindView(R.id.recyclerview_ingredients) RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.recyclerview_steps) RecyclerView mStepRecyclerView;;
    @BindView(R.id.toolbar) Toolbar mToobar;
    private GridLayoutManager mIngredientsGridLayoutManager, mStepGridLayoutManager;
    private IngredientsAdapter mIngredientsAdapter;
    private StepAdapter mStepAdapter;
    private List mIngredientsData, mStepsData;
    public Recipe mRecipe;

    StepAdapter.ListItemClickListener mCallback;

    private static final String EXTRA_RECIPE = "recipe_item";

    public DetailListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.fragment_detail_list, container, false);

        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
        }

        mToobar.setTitle(mRecipe.getName());

        mIngredientsGridLayoutManager = new GridLayoutManager(getContext(), 1);

        mIngredientsRecyclerView.setLayoutManager(mIngredientsGridLayoutManager);
        mIngredientsRecyclerView.setHasFixedSize(true);

        mIngredientsData = new ArrayList<Ingredient>();

        mIngredientsAdapter = new IngredientsAdapter(getContext(), mIngredientsData);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);

        loadIngredients(mRecipe.getIngredients());

        mStepGridLayoutManager = new GridLayoutManager(getContext(), 1);

        mStepRecyclerView.setLayoutManager(mStepGridLayoutManager);
        mStepRecyclerView.setHasFixedSize(true);

        mStepsData = new ArrayList<Step>();

        mStepAdapter = new StepAdapter(getContext(), mStepsData, mCallback);
        mStepRecyclerView.setAdapter(mStepAdapter);


        loadStep(mRecipe.getSteps());

        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (StepAdapter.ListItemClickListener) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " should implement ListItemClickListener.");
        }
    }

    private void loadStep(List<Step> stepsData) {
        mStepsData.clear();
        mStepsData = stepsData;

        mStepAdapter.setStepsData(mStepsData);
    }

    private void loadIngredients(List<Ingredient> ingredientsData) {

        mIngredientsData.clear();
        mIngredientsData = ingredientsData;

        mIngredientsAdapter.setIngredientsData(mIngredientsData);
    }


    public void setRecipe(Recipe recipe){
        mRecipe = recipe;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_RECIPE, mRecipe);
    }


    private void closeOnError() {
        getActivity().finish();
        Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
    }
}
