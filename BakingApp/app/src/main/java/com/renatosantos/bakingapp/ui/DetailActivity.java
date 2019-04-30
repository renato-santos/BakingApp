package com.renatosantos.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.renatosantos.bakingapp.R;
import com.renatosantos.bakingapp.model.Recipe;

import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements StepAdapter.ListItemClickListener, RecipeStepFragment.StepListener {

    private boolean mTwoPane;
    private Recipe mRecipe;
    private int stepIndex = 0;
    public static final String EXTRA_STEP_INDEX = "step_index";
    public static final String EXTRA_RECIPE = "recipe_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();


        if (intent == null) {
            closeOnError();
        }

        if (!(intent.hasExtra(MainActivity.PARCELABLE_RECIPE))){
            closeOnError();
        }else{
            mRecipe = (Recipe) intent.getParcelableExtra(MainActivity.PARCELABLE_RECIPE);
        }

        if(findViewById(R.id.linear_layout_tab) != null){
            mTwoPane = true;
            if (savedInstanceState == null) {
                refreshFragment(mRecipe, stepIndex);
            } else{
                stepIndex = savedInstanceState.getInt(EXTRA_STEP_INDEX);
                mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
            }
        } else {
            mTwoPane = false;
        }

        DetailListFragment detailListFragment = new DetailListFragment();

        detailListFragment.setRecipe(mRecipe);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.recipe_detail_view, detailListFragment)
                .commit();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        if(mTwoPane){
            stepIndex = clickedItemIndex;

            refreshFragment(mRecipe,stepIndex);


        } else {
            Intent intent = new Intent(this, StepActivity.class);

            intent.putExtra(EXTRA_RECIPE, mRecipe);
            intent.putExtra(EXTRA_STEP_INDEX, clickedItemIndex);

            this.startActivity(intent);
        }

    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNext() {

        if (stepIndex < mRecipe.getSteps().size() - 1) {
            stepIndex++;
            refreshFragment(mRecipe, stepIndex);
        }
    }

    @Override
    public void onPrevious() {
        if (stepIndex > 0) {
            stepIndex--;
            refreshFragment(mRecipe, stepIndex);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_STEP_INDEX, stepIndex);
        outState.putParcelable(EXTRA_RECIPE, mRecipe);
    }

    private void refreshFragment(Recipe recipe, int index){

        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

        recipeStepFragment.setRecipe(recipe);
        recipeStepFragment.setStepIndex(index);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.recipe_step_view, recipeStepFragment)
                .commit();

    }

}



