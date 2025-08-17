package com.xiaoxianben.lazymystical.jsonRecipe;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xiaoxianben.lazymystical.jsonRecipe.recipeType.IRecipeType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Recipe<i, o> {

    private final IRecipeType<i> inputType;
    private final IRecipeType<o> outputType;

    public final List<String> ids = new ArrayList<>();
    public final List<i> inputs = new ArrayList<>();
    public final List<o> outputs = new ArrayList<>();

    public Recipe(IRecipeType<i> input, IRecipeType<o> output) {
        this.inputType = input;
        this.outputType = output;
    }

    /** 你不应该对其返回内容进行修改 */
    @Nullable
    public o getOutput(i input) {
        for (int i = 0; i < inputs.size(); i++) {
            if (inputType.equals(inputs.get(i), input)) {
                return outputs.get(i);
            }
        }
        return null;
    }
    /** 你不应该对其返回内容进行修改 */
    public o getOutput(int index) {
        return outputs.get(index);
    }
    /** 你不应该对其返回内容进行修改 */
    public i getInput(int index) {
        return inputs.get(index);
    }
    public int size() {
        return inputs.size();
    }

    public Recipe<i, o> addRecipe(String id, i input, o output) {
        this.inputs.add(input);
        this.outputs.add(output);
        this.ids.add(id);
        return this;
    }

    public JsonObject toJsonObject() {
        JsonObject json = new JsonObject();

        for (int i = 0; i < ids.size(); i++) {
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.add("input", inputType.getRecipeJson(inputs.get(i)));
            jsonObject1.add("output", outputType.getRecipeJson(outputs.get(i)));

            json.add(ids.get(i), jsonObject1);
        }

        return json;
    }

    public Recipe<i, o> JsonObjectToRecipe(JsonObject json) {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            final JsonObject value = entry.getValue().getAsJsonObject();

            this.ids.add(entry.getKey());
            this.inputs.add(inputType.getRecipe(value.getAsJsonObject("input")));
            this.outputs.add(outputType.getRecipe(value.getAsJsonObject("output")));
        }

        return this;
    }

    public IRecipeType<i> getInputRecipeType() {
        return inputType;
    }

    public IRecipeType<o> getOutputRecipeType() {
        return outputType;
    }
}
