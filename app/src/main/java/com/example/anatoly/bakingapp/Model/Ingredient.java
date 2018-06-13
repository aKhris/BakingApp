package com.example.anatoly.bakingapp.Model;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class Ingredient implements Serializable {
    private int mQuantity;
    private String mMeasure;
    private String mIngredient;

    public int getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public void setmMeasure(String mMeasure) {
        this.mMeasure = mMeasure;
    }

    public String getmIngredient() {
        return mIngredient;
    }

    public void setmIngredient(String mIngredient) {
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
        String result ="";
        for (Ingredient ing:ingredients
             ) {
            result+=ing.toString()+"\n";
        }

        return result.substring(0, result.length()-1);  //removing the last "\n" character
    }
}
