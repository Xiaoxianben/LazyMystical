package com.xiaoxianben.lazymystical.manager;

import com.blakebr0.mysticalagradditions.blocks.BlockTier6Crop;
import com.blakebr0.mysticalagradditions.blocks.BlockTier6InferiumCrop;
import com.blakebr0.mysticalagradditions.items.ItemTier6Seed;
import com.blakebr0.mysticalagradditions.lib.CropType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AgradditionsModManager {


    public AgradditionsModManager() {
    }


    public void init(Map<Item, List<ItemStack>> recipes, Map<Item, IBlockState> RootBlockRecipes) {
        for (CropType.Type type : CropType.Type.values()) {
            RootBlockRecipes.put(type.getSeed(), type.getRoot());
            recipes.put(type.getSeed(), Collections.singletonList(type.getCrop().getDefaultInstance()));
        }
    }


    public boolean isTier6Seed(Item seed) {
        return seed instanceof ItemTier6Seed;
    }

    public IBlockState getRootBlock(Item seed) {
        Block cropBlock = ((IPlantable) seed).getPlant(null, null).getBlock();
        if (cropBlock instanceof BlockTier6Crop) {
            return ((BlockTier6Crop) cropBlock).getRoot();
        }
        return null;
    }

    public int getResultItemCount(Item seed) {
        if (this.isTier6Seed(seed) && ((IPlantable) seed).getPlant(null, null).getBlock() instanceof BlockTier6InferiumCrop) {
            return 6;
        }
        return 1;
    }

}
