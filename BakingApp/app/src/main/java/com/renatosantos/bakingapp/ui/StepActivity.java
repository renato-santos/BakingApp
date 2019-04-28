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

public class StepActivity extends AppCompatActivity {

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

        if (!(intent.hasExtra(DetailActivity.PARCELABLE_STEP) && (intent.hasExtra(DetailActivity.STEP_INDEX)))){
            closeOnError();
        }

        Recipe recipe = (Recipe) intent.getParcelableExtra(DetailActivity.PARCELABLE_STEP);
        int stepIndex = intent.getIntExtra(DetailActivity.STEP_INDEX, 0);


        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

        recipeStepFragment.setRecipe(recipe);
        recipeStepFragment.setStepIndex(stepIndex);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().
                replace(R.id.recipe_step_view, recipeStepFragment)
                .commit();


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



