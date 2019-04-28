package com.renatosantos.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.renatosantos.bakingapp.R;
import com.renatosantos.bakingapp.model.Ingredient;
import com.renatosantos.bakingapp.model.Recipe;
import com.renatosantos.bakingapp.service.RecipeWidgetService;
import com.renatosantos.bakingapp.ui.MainActivity;

import java.util.List;

public class RecipeWidgetProvider extends AppWidgetProvider {




    public static void updateAppWidget (Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe){


        RemoteViews views;

        if (recipe != null) {
            views = getIngredientListWidget(context, recipe);
        } else {
            views = getEmptyWidget(context);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        RecipeWidgetService.startWidgets(context);

    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String json = prefs.getString(MainActivity.PREF_RECIPE, null);

        Recipe recipe = null;

        if(json!= null){

            recipe = (Recipe) new Gson().fromJson(json, Recipe.class);
        }

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    private static RemoteViews getIngredientListWidget(Context context, Recipe recipe) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        String recipeName = recipe.getName();
        views.setTextViewText(R.id.tv_ingredients_title_widget, recipeName);

        String recipeList = "";

        for (Ingredient ingredient : recipe.getIngredients()){
            recipeList = recipeList + " • " + ingredient.getIngredient() + '\n';
        }

        views.setTextViewText(R.id.tv_ingredients_list_widget, recipeList);

        return views;
    }

    private static RemoteViews getEmptyWidget(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.empty_widget_view);

        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.image_no_recipe, appPendingIntent);
        views.setOnClickPendingIntent(R.id.image_no_recipe, appPendingIntent);

        return views;
    }

}

