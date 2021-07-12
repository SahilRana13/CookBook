package com.example.cookbook.Models;

public class RecipeInfo {


    String recipeName,chefName,recipeType,recipeDuration,countryName,recipeIngredients,recipeDirections;

    public RecipeInfo() {
    }

    public RecipeInfo(String recipeName, String chefName, String recipeType, String recipeDuration, String countryName, String recipeIngredients, String recipeDirections) {
        this.recipeName = recipeName;
        this.chefName = chefName;
        this.recipeType = recipeType;
        this.recipeDuration = recipeDuration;
        this.countryName = countryName;
        this.recipeIngredients = recipeIngredients;
        this.recipeDirections = recipeDirections;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public String getRecipeDuration() {
        return recipeDuration;
    }

    public void setRecipeDuration(String recipeDuration) {
        this.recipeDuration = recipeDuration;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(String recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public String getRecipeDirections() {
        return recipeDirections;
    }

    public void setRecipeDirections(String recipeDirections) {
        this.recipeDirections = recipeDirections;
    }


}
