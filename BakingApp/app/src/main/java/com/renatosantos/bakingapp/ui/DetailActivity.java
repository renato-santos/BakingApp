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

public class DetailActivity extends AppCompatActivity implements StepAdapter.ListItemClickListener {

    private boolean mTwoPane;
    private Recipe mRecipe;
    public static final String PARCELABLE_STEP = "Step";
    public static final String STEP_INDEX = "StepIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(findViewById(R.id.linear_layout_tab) != null){
            mTwoPane = true;
        } else {
            mTwoPane = false;
        }

        Intent intent = getIntent();


        if (intent == null) {
            closeOnError();
        }

        if (!(intent.hasExtra(MainActivity.PARCELABLE_RECIPE))){
            closeOnError();
        }

        mRecipe = (Recipe) intent.getParcelableExtra(MainActivity.PARCELABLE_RECIPE);

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
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

            recipeStepFragment.setRecipe(mRecipe);
            recipeStepFragment.setStepIndex(clickedItemIndex);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().
                    replace(R.id.recipe_step_view, recipeStepFragment)
                    .commit();


        } else {
            Intent intent = new Intent(this, StepActivity.class);

            intent.putExtra(PARCELABLE_STEP, mRecipe);
            intent.putExtra(STEP_INDEX, clickedItemIndex);

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

}



