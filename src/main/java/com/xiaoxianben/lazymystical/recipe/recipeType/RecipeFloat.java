package com.xiaoxianben.lazymystical.recipe.recipeType;

import com.google.gson.JsonObject;

public class RecipeFloat implements IRecipeType<Float> {

    private final String name;

    public RecipeFloat(String name) {
        this.name = name;
    }

    @Override
    public Class<Float> getClassType() {
        return Float.class;
    }

    @Override
    public JsonObject getRecipeJson(Float f) {
        JsonObject json = new JsonObject();

        json.addProperty("names", this.name);
        json.addProperty("count", f);

        return json;
    }

    @Override
    public Float getRecipe(JsonObject json) {
        return json.get("count").getAsFloat();
    }
}
