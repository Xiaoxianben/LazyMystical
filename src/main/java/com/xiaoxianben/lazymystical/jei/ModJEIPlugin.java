package com.xiaoxianben.lazymystical.jei;


import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.gui.BlockGUI;
import com.xiaoxianben.lazymystical.init.EnumBlockLevel;
import com.xiaoxianben.lazymystical.jei.advancedGuiHandler.SeedCultivatorAdvancedGuiHandler;
import com.xiaoxianben.lazymystical.jei.recipeCategory.SeedCultivatorCategory;
import com.xiaoxianben.lazymystical.jei.recipeWrapper.SeedCultivatorWrapper;
import com.xiaoxianben.lazymystical.manager.SeedManager;
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
import java.util.Map;
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
        for (Block seedCultivator : LazyMystical.BLOCKS.subList(0, EnumBlockLevel.enableNumber())) {
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

        for (Map.Entry<Item, List<ItemStack>> entry : SeedManager.getSeedToCropMap().entrySet()) {
            ArrayList<ItemStack> Outputs = new ArrayList<>();
            ArrayList<ItemStack> input = new ArrayList<>();

            input.add(entry.getKey().getDefaultInstance());
            Outputs.add(entry.getValue().get(0));
            Outputs.add(entry.getKey().getDefaultInstance());

            if (SeedManager.getRootBlock(entry.getKey()) != null)
                input.add(new ItemStack(SeedManager.getRootBlock(entry.getKey()), 1, SeedManager.getRootBlockMeta(entry.getKey())));
            recipes.add(new SeedCultivatorWrapper(input.toArray(new ItemStack[0]), Outputs.toArray(new ItemStack[0])));
        }

        return recipes;
    }

}
