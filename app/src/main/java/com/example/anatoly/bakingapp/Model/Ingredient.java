package com.example.anatoly.bakingapp.Model;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class Ingredient implements Serializable {
    private int mQuantity;
    private String mMeasure;
    private String mIngredient;

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public void setMeasure(String mMeasure) {
        this.mMeasure = mMeasure;
    }

    public void setIngredient(String mIngredient) {
        this.mIngredient = mIngredient;
    }

    @Override
    public String toString() {
        return String.format(Locale.getDefault(),"%s - %d (%s)",
                mIngredient,
                mQuantity,
                mMeasure
        );
    }

    public static String getIngredientsList(List<Ingredient> ingredients){
        StringBuilder result = new StringBuilder();
        for (Ingredient ing:ingredients
             ) {
            result.append(ing.toString()).append("\n");
        }

        return result.substring(0, result.length()-1);  //removing the last "\n" character
    }
}
