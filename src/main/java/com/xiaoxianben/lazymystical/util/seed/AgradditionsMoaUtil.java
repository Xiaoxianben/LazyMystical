package com.xiaoxianben.lazymystical.util.seed;

import com.blakebr0.mysticalagradditions.blocks.BlockTier6Crop;
import com.blakebr0.mysticalagradditions.blocks.BlockTier6InferiumCrop;
import com.blakebr0.mysticalagradditions.items.ItemTier6Seed;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;

import java.util.LinkedHashMap;

public class AgradditionsMoaUtil {

    protected LinkedHashMap<Item, Integer> seedMap;


    public AgradditionsMoaUtil(LinkedHashMap<Item, Integer> seedMap) {
        this.seedMap = seedMap;
    }


    public void init(Item item) {
        if (this.getRootBlockState(item) != null) {
            IBlockState rootBlock = this.getRootBlockState(item);
            seedMap.put(item, rootBlock.getBlock().getMetaFromState(rootBlock));
        }
    }


    public boolean isTier6Seed(Item seed) {
        return seed instanceof ItemTier6Seed;
    }

    public boolean isRootBlock(ItemStack rootBlock) {
        return rootBlock.getItem() instanceof com.blakebr0.mysticalagradditions.blocks.ItemBlockSpecial;
    }

    public IBlockState getRootBlockState(Item seed) {
        if (!this.isTier6Seed(seed)) return null;
        Block cropBlock = ((IPlantable) seed).getPlant(null, null).getBlock();
        if (cropBlock instanceof BlockTier6Crop) {
            return ((BlockTier6Crop) cropBlock).getRoot();
        }
        return null;
    }

    public int getCropCount(Item seed) {
        if (this.isTier6Seed(seed) && ((IPlantable) seed).getPlant(null, null).getBlock() instanceof BlockTier6InferiumCrop) {
            return 6;
        }
        return 1;
    }

}
