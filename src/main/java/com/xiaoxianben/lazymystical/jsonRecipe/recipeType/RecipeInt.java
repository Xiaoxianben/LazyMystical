package com.xiaoxianben.lazymystical.jsonRecipe.recipeType;

import com.google.gson.JsonObject;

public class RecipeInt implements IRecipeType<Integer> {

    public final String name;

    public RecipeInt(String name) {
        this.name = name;
    }

    @Override
    public Class<Integer> getClassType() {
        return Integer.class;
    }

    @Override
    public JsonObject getRecipeJson(Integer integer) {
        JsonObject json = new JsonObject();

        json.addProperty("names", this.name);
        json.addProperty("count", integer);

        return json;
    }

    @Override
    public Integer getRecipe(JsonObject json) {
        return json.get("count").getAsInt();
    }
}
