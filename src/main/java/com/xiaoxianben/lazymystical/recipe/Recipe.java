package com.xiaoxianben.lazymystical.recipe;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xiaoxianben.lazymystical.recipe.recipeType.IRecipeType;

import java.util.ArrayList;
import java.util.List;

public class Recipe<i, o> {

    private final IRecipeType<i> input;
    private final IRecipeType<o> output;

    public int id;
    public List<i> inputs = new ArrayList<>();
    public List<o> outputs = new ArrayList<>();

    public Recipe(IRecipeType<i> input, IRecipeType<o> output) {
        this.input = input;
        this.output = output;
    }

    public Recipe<i, o> create(int id, List<i> inputs, List<o> outputs) {
        Recipe<i, o> recipe = new Recipe<>(input, output);
        recipe.id = id;
        recipe.inputs = inputs;
        recipe.outputs = outputs;
        return recipe;
    }

    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();
        json.addProperty("id", this.id);

        JsonArray inputJsonArray = new JsonArray();
        for (i i : inputs) {
            inputJsonArray.add(input.getRecipeJson(i));
        }
        json.add("inputs", inputJsonArray);

        JsonArray outputJsonArray = new JsonArray();
        for (o o : outputs) {
            outputJsonArray.add(output.getRecipeJson(o));
        }
        json.add("outputs", outputJsonArray);

        return json;
    }

    public Recipe<i, o> JsonObjectToRecipe(JsonObject json) {
        Recipe<i, o> recipe = new Recipe<>(input, output);
        recipe.id = json.get("id").getAsInt();

        JsonArray inputJsonArray = json.getAsJsonArray("inputs");
        for (int i = 0; i < inputJsonArray.size(); i++) {
            recipe.inputs.add(input.getRecipe(inputJsonArray.get(i).getAsJsonObject()));
        }

        JsonArray outputJsonArray = json.getAsJsonArray("outputs");
        for (int i = 0; i < outputJsonArray.size(); i++) {
            recipe.outputs.add(output.getRecipe(outputJsonArray.get(i).getAsJsonObject()));
        }
        return recipe;
    }

    public IRecipeType<i> getInputRecipeType() {
        return input;
    }

    public IRecipeType<o> getOutputRecipeType() {
        return output;
    }
}
