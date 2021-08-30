package com.example.cookbook.Models;

public class RecipeSearchInfo {

    String rName,chefName;

    public RecipeSearchInfo(String rName, String chefName) {
        this.rName = rName;
        this.chefName = chefName;
    }

    public RecipeSearchInfo() {
    }

    public String getrName() {
        return rName;
    }

    public String getChefName() {
        return chefName;
    }
}
