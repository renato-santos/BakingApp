package com.renatosantos.bakingapp.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RecipeEndpointInterface {
    // Request method and URL specified in the annotation

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();

}
