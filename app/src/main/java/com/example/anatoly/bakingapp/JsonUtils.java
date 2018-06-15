package com.example.anatoly.bakingapp;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.anatoly.bakingapp.Model.Ingredient;
import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String JSON_RECIPES_FILE_NAME = "android-baking-app-json.json";

    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_NAME = "name";
    private static final String JSON_KEY_INGREDIENTS = "ingredients";
    private static final String JSON_KEY_STEPS = "steps";
    private static final String JSON_KEY_SERVINGS = "servings";
    private static final String JSON_KEY_IMAGE = "image";


    private static final String JSON_KEY_QUANTITY = "quantity";
    private static final String JSON_KEY_MEASURE = "measure";
    private static final String JSON_KEY_INGREDIENT = "ingredient";


    private static final String JSON_KEY_SHORT_DESCRIPTION = "shortDescription";
    private static final String JSON_KEY_DESCRIPTION = "description";
    private static final String JSON_KEY_VIDEO_URL = "videoURL";
    private static final String JSON_KEY_THUMBNAIL_URL = "thumbnailURL";





    public static ArrayList<Recipe> getRecipesList (Context context){
        ArrayList<Recipe> recipeList = new ArrayList<>();
        String recipesString = readFromAssets(context, JSON_RECIPES_FILE_NAME);
        JSONArray recipesArray = parseResults(recipesString);
        if(recipesArray == null || recipesArray.length()==0){
            return recipeList;
        }
        for (int i = 0; i < recipesArray.length(); i++) {
            try {
                JSONObject recipeObject = recipesArray.getJSONObject(i);
                Recipe recipe = parseRecipe(recipeObject);
                recipeList.add(recipe);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return recipeList;
    }


    @Nullable
    public static Recipe getRecipeById(Context context, int id){
        String recipesString = readFromAssets(context, JSON_RECIPES_FILE_NAME);
        JSONArray recipesArray = parseResults(recipesString);
        if(recipesArray == null || recipesArray.length()==0){
            return null;
        }
        for (int i = 0; i < recipesArray.length(); i++) {
            try {
                JSONObject recipeObject = recipesArray.getJSONObject(i);
                Recipe recipe = parseRecipe(recipeObject);
                if(recipe.getId()==id){return recipe;}
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }









    /**
     * Function for parsing the whole JSON string into JSONarray
     * @return JSONArray of recipes-JSONObject's
     */
    private static JSONArray parseResults (String recipesString){
        JSONArray array = null;
        try {
            array = new JSONArray(recipesString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }




    private static Recipe parseRecipe(JSONObject jsonObject){
        Recipe recipe = new Recipe();

        int id = jsonObject.optInt(JSON_KEY_ID);
        String name = jsonObject.optString(JSON_KEY_NAME);
        int servings = jsonObject.optInt(JSON_KEY_SERVINGS);
        String imageUrl = jsonObject.optString(JSON_KEY_IMAGE);
        List<Ingredient> ingredients = new ArrayList<>();
        List<Step> steps = new ArrayList<>();
        try {
            JSONArray ingredientsJson = jsonObject.getJSONArray(JSON_KEY_INGREDIENTS);
            if(ingredientsJson!=null && ingredientsJson.length()>0){
                for (int i = 0; i < ingredientsJson.length(); i++) {
                    ingredients.add(parseIngredient(ingredientsJson.getJSONObject(i)));
                }
            }

            JSONArray stepsJson = jsonObject.getJSONArray(JSON_KEY_STEPS);
            if(stepsJson!=null && stepsJson.length()>0){
                for (int i = 0; i < stepsJson.length(); i++) {
                    steps.add(parseStep(stepsJson.getJSONObject(i)));
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        recipe.setId(id);
        recipe.setName(name);
        recipe.setServings(servings);
        recipe.setImageUrl(imageUrl);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);

        return recipe;
    }

    private static Ingredient parseIngredient(JSONObject jsonObject){
        Ingredient resultIngredient = new Ingredient();

        int quantity = jsonObject.optInt(JSON_KEY_QUANTITY);
        String measure = jsonObject.optString(JSON_KEY_MEASURE);
        String ingredient = jsonObject.optString(JSON_KEY_INGREDIENT);

        resultIngredient.setQuantity(quantity);
        resultIngredient.setMeasure(measure);
        resultIngredient.setIngredient(ingredient);

        return resultIngredient;
    }

    private static Step parseStep(JSONObject jsonObject){
        Step step = new Step();

        int id = jsonObject.optInt(JSON_KEY_ID);
        String shortDescription = jsonObject.optString(JSON_KEY_SHORT_DESCRIPTION);
        String description = jsonObject.optString(JSON_KEY_DESCRIPTION);
        String videoUrl = jsonObject.optString(JSON_KEY_VIDEO_URL);
        String thumbnailUrl = jsonObject.optString(JSON_KEY_THUMBNAIL_URL);

        step.setId(id);
        step.setShortDescription(shortDescription);
        step.setDescription(description);
        step.setVideoUrl(videoUrl);
        step.setThumbnailUrl(thumbnailUrl);

        return step;
    }

    /**
     * The function of reading asset file contents into String variable
     * Got it from here: https://gist.github.com/laaptu/6459577
     * @param context - a context to get resources from
     * @param fileName - name of the file in assets folder
     * @return a String that written in the file
     */
    private static String readFromAssets(Context context, String fileName) {
        StringBuilder ReturnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets()
                    .open(fileName);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line = "";
            while ((line = input.readLine()) != null) {
                ReturnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return ReturnString.toString();
    }



}
