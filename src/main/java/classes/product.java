package com.example.foodallergyapp.App.classes;

import java.util.List;

/**
**************************************
Created :
Crated By: Ian Holderness 14023756
Version : 1.0.0
Last Update:

Version Updates:

Refrences:

**************************************
 */

public class product {
    private static int id;
    private static String disc;
    private static List<String> ingredients;
    private static Boolean allergen;


    /**
     *
     * @return id
     */
    public static int getId() {
        return id;
    }

    /**
     *
     * @return ingredients
     */
    public static List<String> getIngredients() {
        return ingredients;
    }

    /**
     *
     * @return disc
     */
    public static String getDisc() {
        return disc;
    }

    /**
     *
     * @param disc
     */
    public static void setDisc(String disc) {
        product.disc = disc;
    }


    public static void setAllergen(Boolean a) {
        product.allergen = allergen;
    }

    public Boolean getAllergen() {
        return product.allergen;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @param arr
     */
    public void setIngredients(List<String> arr) {
        this.ingredients = arr;
    }

}
