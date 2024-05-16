package com.xiaoxianben.lazymystical.jei;


import com.xiaoxianben.lazymystical.GUI.BlockGUI;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        for (BlockSeedCultivator seedCultivator : ModBlocks.seedCultivators) {
            registry.addRecipeCatalyst(Item.getItemFromBlock(seedCultivator).getDefaultInstance(), SeedCultivatorCategory.ID);
        }

        registry.addRecipes(SeedCultivatorRecipes(), SeedCultivatorCategory.ID);

        registry.addAdvancedGuiHandlers(new SeedCultivatorAdvancedGuiHandler());

        Rectangle recipeClickArea = BlockGUI.getRecipeClickArea();
        registry.addRecipeClickArea(BlockGUI.class, recipeClickArea.x, recipeClickArea.y, recipeClickArea.width, recipeClickArea.height, SeedCultivatorCategory.ID);
    }

    private List<SeedCultivatorWrapper> SeedCultivatorRecipes() {
        List<SeedCultivatorWrapper> recipes = new ArrayList<>();

        // steam
        for (int i = 0; i < SeedUtil.getSeedToCropMap().size(); i++) {
            ArrayList<ItemStack> Outputs = new ArrayList<>();

            Item inputItem = (Item) SeedUtil.getSeedToCropMap().keySet().toArray()[i];
            ItemStack input;
            Outputs.add(SeedUtil.getCrop(inputItem));
            Outputs.add(input = inputItem.getDefaultInstance());

            if (SeedUtil.getRootBlockMeta(inputItem) != -1)
                Outputs.add(new ItemStack(SeedUtil.agradditionsMoaUtil.getRootBlockState(inputItem).getBlock(), 1, SeedUtil.getRootBlockMeta(inputItem)));
            recipes.add(new SeedCultivatorWrapper(input, Outputs.toArray(new ItemStack[0])));
        }

        return recipes;
    }

}
