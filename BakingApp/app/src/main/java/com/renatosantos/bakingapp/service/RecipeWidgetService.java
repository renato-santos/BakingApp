package com.renatosantos.bakingapp.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.renatosantos.bakingapp.R;
import com.renatosantos.bakingapp.model.Recipe;
import com.renatosantos.bakingapp.widget.RecipeWidgetProvider;

public class RecipeWidgetService extends IntentService {

    private static final String TAG = RecipeWidgetService.class.getSimpleName();

    public RecipeWidgetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

            RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds);
    }

    public static void startWidgets(@NonNull Context context) {
        Intent intent = new Intent(context, RecipeWidgetService.class);
        context.startService(intent);
    }
}
