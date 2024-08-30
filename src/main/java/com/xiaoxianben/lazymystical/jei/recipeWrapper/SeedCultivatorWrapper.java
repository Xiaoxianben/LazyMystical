package com.xiaoxianben.lazymystical.jei.recipeWrapper;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SeedCultivatorWrapper implements IRecipeWrapper {

    public ItemStack[] input;

    public ItemStack[] output;


    public SeedCultivatorWrapper(ItemStack[] input, ItemStack[] output) {
        this.input = input;
        this.output = output;
    }


    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.stream(input).collect(Collectors.toList()));
        ingredients.setOutputs(VanillaTypes.ITEM, Arrays.stream(output).collect(Collectors.toList()));
    }

}
