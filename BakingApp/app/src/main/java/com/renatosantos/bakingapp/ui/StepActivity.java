package com.renatosantos.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.renatosantos.bakingapp.R;
import com.renatosantos.bakingapp.model.Recipe;
import com.renatosantos.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepActivity extends AppCompatActivity implements RecipeStepFragment.StepListener {

    private static final String EXTRA_STEP_INDEX = "step_index";
    private static final String EXTRA_RECIPE = "recipe_item";

    int stepIndex = 0;
    Recipe mRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ActionBar actionBar = getSupportActionBar();
            if (getSupportActionBar() != null) {
                actionBar.hide();
            }
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        setContentView(R.layout.activity_step);

        Intent intent = getIntent();


        if (intent == null) {
            closeOnError();
        }

        if (!(intent.hasExtra(DetailActivity.EXTRA_RECIPE) && (intent.hasExtra(DetailActivity.EXTRA_STEP_INDEX)))){
            closeOnError();
        }
        else {
            mRecipe = (Recipe) intent.getParcelableExtra(DetailActivity.EXTRA_RECIPE);
            stepIndex = intent.getIntExtra(DetailActivity.EXTRA_STEP_INDEX, 0);
        }


        if (savedInstanceState == null) {
            refreshFragment(mRecipe, stepIndex);
        } else{
            stepIndex = savedInstanceState.getInt(EXTRA_STEP_INDEX);
            mRecipe = savedInstanceState.getParcelable(EXTRA_RECIPE);
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
}



