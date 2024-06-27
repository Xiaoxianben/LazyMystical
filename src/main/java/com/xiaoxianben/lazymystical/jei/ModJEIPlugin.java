package com.xiaoxianben.lazymystical.jei;


import com.xiaoxianben.lazymystical.GUI.BlockGUI;
import com.xiaoxianben.lazymystical.init.ModBlocks;
import com.xiaoxianben.lazymystical.jei.AdvancedGuiHandler.SeedCultivatorAdvancedGuiHandler;
import com.xiaoxianben.lazymystical.jei.RecipeCategory.SeedCultivatorCategory;
import com.xiaoxianben.lazymystical.jei.RecipeWrapper.SeedCultivatorWrapper;
import com.xiaoxianben.lazymystical.util.seed.SeedUtil;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JEIPlugin
public class ModJEIPlugin implements IModPlugin {

    @ParametersAreNonnullByDefault
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers jeiHelpers = registry.getJeiHelpers();

        registry.addRecipeCategories(new SeedCultivatorCategory(jeiHelpers.getGuiHelper()));
    }


    @ParametersAreNonnullByDefault
    @Override
    public void register(IModRegistry registry) {
        for (Block seedCultivator : ModBlocks.allSeedCultivators) {
            if (Objects.nonNull(seedCultivator)) {
                registry.addRecipeCatalyst(Item.getItemFromBlock(seedCultivator).getDefaultInstance(), SeedCultivatorCategory.ID);
            }
        }

        registry.addRecipes(SeedCultivatorRecipes(), SeedCultivatorCategory.ID);

        registry.addAdvancedGuiHandlers(new SeedCultivatorAdvancedGuiHandler());

        Rectangle recipeClickArea = BlockGUI.getRecipeClickArea();
        registry.addRecipeClickArea(BlockGUI.class, recipeClickArea.x, recipeClickArea.y, recipeClickArea.width, recipeClickArea.height, SeedCultivatorCategory.ID);
    }

    private List<SeedCultivatorWrapper> SeedCultivatorRecipes() {
        List<SeedCultivatorWrapper> recipes = new ArrayList<>();

        Item[] inputs = SeedUtil.getSeedToCropMap().keySet().toArray(new Item[0]);
        // steam
        for (int i = 0; i < inputs.length; i++) {
            ArrayList<ItemStack> Outputs = new ArrayList<>();
            ArrayList<ItemStack> input = new ArrayList<>();

            Item inputItem = inputs[i];

            input.add(inputItem.getDefaultInstance());
            Outputs.add(Objects.requireNonNull(SeedUtil.getCrop(inputItem)).copy());
            Outputs.add(inputItem.getDefaultInstance());

            if (SeedUtil.getRootBlockMeta(inputItem) != -1)
                input.add(new ItemStack(SeedUtil.agradditionsModUtil.getRootBlockState(inputItem).getBlock(), 1, SeedUtil.getRootBlockMeta(inputItem)));
            recipes.add(new SeedCultivatorWrapper(input.toArray(new ItemStack[0]), Outputs.toArray(new ItemStack[0])));
        }

        return recipes;
    }

}
