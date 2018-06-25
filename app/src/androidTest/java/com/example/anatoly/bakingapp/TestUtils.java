package com.example.anatoly.bakingapp;

import com.example.anatoly.bakingapp.Model.Ingredient;
import com.example.anatoly.bakingapp.Model.Recipe;
import com.example.anatoly.bakingapp.Model.Step;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestUtils {



    public static Recipe getMockRecipe(){
        Recipe recipe = new Recipe();
        Random random = new Random();

        List<Step> steps = new ArrayList<>();
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < random.nextInt(5); i++) {
            ingredients.add(getMockIngredient(i));
        }

        for (int i = 0; i < 20; i++) {
            steps.add(getMockStep(i));
        }

        recipe.setId(random.nextInt());

        recipe.setIngredients(ingredients);
        recipe.setImageUrl("");
        recipe.setName("test recipe");
        recipe.setServings(random.nextInt(10));
        recipe.setSteps(steps);
        return recipe;
    }

    public static Ingredient getMockIngredient(int quantity){
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredient("Lemon "+quantity);
        ingredient.setQuantity(quantity);
        ingredient.setMeasure("Piece");
        return ingredient;
    }

    public static Step getMockStep(int id){
        Step step = new Step();
        step.setId(id);
        step.setShortDescription("Short description");
        step.setDescription("Long description");
        step.setVideoUrl("");
        step.setThumbnailUrl("");
        return step;
    }
}
