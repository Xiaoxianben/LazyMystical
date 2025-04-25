package com.xiaoxianben.lazymystical.manager;

import com.blakebr0.mysticalagradditions.lib.CropType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AgradditionsModManager {


    public AgradditionsModManager() {
    }

    public void init(Map<Item, List<ItemStack>> recipes, Map<Item, IBlockState> RootBlockRecipes) {
        for (CropType.Type type : CropType.Type.values()) {
            if (type.isEnabled()) {
                RootBlockRecipes.put(type.getSeed(), type.getRoot());
                recipes.put(type.getSeed(), Collections.singletonList(new ItemStack(type.getCrop())));
            }
        }
    }

}
