package com.renatosantos.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.renatosantos.bakingapp.R;
import com.renatosantos.bakingapp.model.Recipe;
import com.renatosantos.bakingapp.model.RecipeEndpointInterface;
import com.renatosantos.bakingapp.utilities.NetworkUtils;
import com.renatosantos.bakingapp.utilities.SimpleIdlingResource;
import com.renatosantos.bakingapp.widget.RecipeWidgetProvider;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener   {

    @BindView(R.id.recyclerview_recipes)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    private RecipeAdapter mRecipeAdapter;
    private List mRecipeData;
    private GridLayoutManager mGridLayoutManager;
    public static final String PARCELABLE_RECIPE = "Recipe";
    public static final String PREF_RECIPE = "PreferencesRecipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_title);

        ButterKnife.bind(this);


        mGridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.grid_columns));

        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecipeData = new ArrayList<Recipe>();

        mRecipeAdapter = new RecipeAdapter(this, mRecipeData, this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        loadRecipes();

        getIdlingResource();

    }

    private void loadRecipes() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkUtils.RECIPE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeEndpointInterface apiService =
                retrofit.create(RecipeEndpointInterface.class);

        mLoadingIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        Call<List<Recipe>> call;
        call = apiService.getRecipes();

        if (getIdlingResource() != null) {
            getIdlingResource().setIdleState(false);
        }

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                int statusCode = response.code();

                if (statusCode != HttpURLConnection.HTTP_OK) {
                    showErrorMessage();
                }
                List<Recipe> recipeList = response.body();

                if (recipeList == null) {
                    showErrorMessage();
                    return;
                }

                getIdlingResource().setIdleState(true);

                populateUI(recipeList);

                showRecipesDataView();


            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // Log error here since request failed
                showErrorMessage();
            }
        });
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {


        Recipe recipe = (Recipe) mRecipeData.get(clickedItemIndex);

        String json = null;
        if(recipe != null) {
            json = new Gson().toJson(recipe);
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        prefs.edit().putString(PREF_RECIPE, json).apply();


        Intent intent = new Intent(this, DetailActivity.class);

        intent.putExtra(PARCELABLE_RECIPE, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds);

        this.startActivity(intent);

    }

    public void populateUI(List<Recipe> recipeData){

        mRecipeData.clear();
        mRecipeData = recipeData;

        mRecipeAdapter.setRecipeData(mRecipeData);

    }

    public void showRecipesDataView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage() {

        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
