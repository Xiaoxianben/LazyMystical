package com.xiaoxianben.lazymystical.jei.recipeWrapper;

import com.xiaoxianben.lazymystical.init.register.MinecraftRegister;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class AccelerantFluidWrapper implements IRecipeWrapper {

    public final ItemStack inputItemStack;
    public final FluidStack outputFluidStack;

    public AccelerantFluidWrapper(ItemStack itemStack, int i) {
        this.inputItemStack = itemStack;
        this.outputFluidStack = new FluidStack(MinecraftRegister.accelerantFluid, i);
    }


    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, inputItemStack);
        ingredients.setOutput(VanillaTypes.FLUID, outputFluidStack);
    }
}
