package com.xiaoxianben.lazymystical.manager.seedManagr;

import com.blakebr0.mysticalagradditions.items.ItemTier6Seed;
import com.blakebr0.mysticalagradditions.lib.CropType;
import com.xiaoxianben.lazymystical.jsonRecipe.Recipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class AgradditionsModManager {


    public AgradditionsModManager() {
    }

    public void init(Recipe<ItemStack, SeedManager.SeedRegisterRecipe> recipes, Map<ItemStack, IBlockState> RootBlockRecipes) {
        for (CropType.Type type : CropType.Type.values()) {
            if (type.isEnabled()) {
                final ItemTier6Seed seed = type.getSeed();

                RootBlockRecipes.put(new ItemStack(seed), type.getRoot());
                final SeedManager.SeedRegisterRecipe seedRegisterRecipe = new SeedManager.SeedRegisterRecipe(seed, new ItemStack(type.getCrop()));

                recipes.addRecipe(seed.getRegistryName().toString(), new ItemStack(seed), seedRegisterRecipe);
            }
        }
    }

}
