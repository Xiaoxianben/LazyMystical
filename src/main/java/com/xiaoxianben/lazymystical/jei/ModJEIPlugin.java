package com.xiaoxianben.lazymystical.jei;


import com.xiaoxianben.lazymystical.gui.guiScreen.GuiAccelerantFluid;
import com.xiaoxianben.lazymystical.gui.guiScreen.GuiSeedCultivator;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.jei.advancedGuiHandler.GuiContainerAdvancedGuiHandler;
import com.xiaoxianben.lazymystical.jei.recipeCategory.AccelerantFluidCategory;
import com.xiaoxianben.lazymystical.jei.recipeCategory.SeedCultivatorCategory;
import com.xiaoxianben.lazymystical.jei.recipeWrapper.AccelerantFluidWrapper;
import com.xiaoxianben.lazymystical.jei.recipeWrapper.SeedCultivatorWrapper;
import com.xiaoxianben.lazymystical.jsonRecipe.ModJsonRecipe;
import com.xiaoxianben.lazymystical.manager.seedManagr.SeedManager;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JEIPlugin
public class ModJEIPlugin implements IModPlugin {

    @ParametersAreNonnullByDefault
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registry.addRecipeCategories(new SeedCultivatorCategory(guiHelper));
        registry.addRecipeCategories(new AccelerantFluidCategory(guiHelper));
    }

    @ParametersAreNonnullByDefault
    @Override
    public void register(IModRegistry registry) {
        for (Block[] seedCultivators : EnumBlockType.SeedCultivator.getAllBlocks()) {
            Arrays.stream(seedCultivators)
                    .filter(Objects::nonNull)
                    .map(block -> new ItemStack(Item.getItemFromBlock(block)))
                    .forEach(itemStack -> registry.addRecipeCatalyst(itemStack, SeedCultivatorCategory.ID));
        }
        for (Block[] accelerantFluids : EnumBlockType.AccelerantFluid.getAllBlocks()) {
            Arrays.stream(accelerantFluids)
                    .filter(Objects::nonNull)
                    .map(block -> new ItemStack(Item.getItemFromBlock(block)))
                    .forEach(itemStack -> registry.addRecipeCatalyst(itemStack, AccelerantFluidCategory.ID));
        }

        registry.addRecipes(SeedCultivatorRecipes(), SeedCultivatorCategory.ID);
        registry.addRecipes(AccelerantFluidRecipes(), AccelerantFluidCategory.ID);

        registry.addAdvancedGuiHandlers(new GuiContainerAdvancedGuiHandler());

        registry.addRecipeClickArea(GuiSeedCultivator.class, 64, 15, 22, 16, SeedCultivatorCategory.ID);
        registry.addRecipeClickArea(GuiAccelerantFluid.class, 63, 33, 22, 16, AccelerantFluidCategory.ID);
    }


    private static List<SeedCultivatorWrapper> SeedCultivatorRecipes() {
        List<SeedCultivatorWrapper> recipes = new ArrayList<>();

        for (int i = 0; i < ModJsonRecipe.seedManagerRecipe.size(); i++) {
            ItemStack inputItem = ModJsonRecipe.seedManagerRecipe.getInput(i).copy();
            SeedManager.SeedRegisterRecipe output = ModJsonRecipe.seedManagerRecipe.getOutput(i).copy();

            ItemStack[] stackOutput = new ItemStack[output.outputOther.length+1];
            stackOutput[0] = output.output;
            if (stackOutput.length > 1) {
                System.arraycopy(output.outputOther, 0, stackOutput, 1, output.outputOther.length);
            }

            Block rootBlock = SeedManager.getRootBlock(inputItem);
            ItemStack[] inputs;
            if (rootBlock != null) {
                inputs = new ItemStack[]{inputItem, new ItemStack(rootBlock)};
            } else {
                inputs = new ItemStack[]{inputItem};
            }

            recipes.add(new SeedCultivatorWrapper(inputs, stackOutput));
        }

        return recipes;
    }
    private static List<AccelerantFluidWrapper> AccelerantFluidRecipes() {
        List<AccelerantFluidWrapper> recipes = new ArrayList<>();

        for (int i = 0; i < ModJsonRecipe.accelerantFluidRecipe.size(); i++) {
            ItemStack inputItem = ModJsonRecipe.accelerantFluidRecipe.getInput(i).copy();
            Integer output = ModJsonRecipe.accelerantFluidRecipe.getOutput(i);

            recipes.add(new AccelerantFluidWrapper(inputItem, output));
        }

        return recipes;
    }

}
