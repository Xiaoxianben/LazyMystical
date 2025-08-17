package com.xiaoxianben.lazymystical.jsonRecipe.recipeType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xiaoxianben.lazymystical.manager.seedManagr.SeedManager;
import net.minecraft.item.ItemStack;

public class RecipeSeedCultivator implements IRecipeType<SeedManager.SeedRegisterRecipe> {
    @Override
    public Class<SeedManager.SeedRegisterRecipe> getClassType() {
        return SeedManager.SeedRegisterRecipe.class;
    }

    @Override
    public JsonObject getRecipeJson(SeedManager.SeedRegisterRecipe recipe) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("input", RecipeTypesOwn.recipe_itemStack.getRecipeJson(recipe.input));
        jsonObject.add("output", RecipeTypesOwn.recipe_itemStack.getRecipeJson(recipe.output));

        JsonArray jsonArray = new JsonArray();
        for (ItemStack itemStack : recipe.outputOther) {
            jsonArray.add(RecipeTypesOwn.recipe_itemStack.getRecipeJson(itemStack));
        }
        jsonObject.add("outputOther", jsonArray);

        return jsonObject;
    }

    @Override
    public SeedManager.SeedRegisterRecipe getRecipe(JsonObject json) {
        JsonArray jsonArray = json.get("outputOther").getAsJsonArray();
        ItemStack[] outputOther = new ItemStack[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            outputOther[i] = RecipeTypesOwn.recipe_itemStack.getRecipe(jsonArray.get(i).getAsJsonObject());
        }

        return new SeedManager.SeedRegisterRecipe(
                RecipeTypesOwn.recipe_itemStack.getRecipe(json.get("input").getAsJsonObject()),
                RecipeTypesOwn.recipe_itemStack.getRecipe(json.get("output").getAsJsonObject()),
                outputOther
        );
    }
}
