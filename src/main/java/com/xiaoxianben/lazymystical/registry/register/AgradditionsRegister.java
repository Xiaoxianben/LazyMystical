package com.xiaoxianben.lazymystical.registry.register;

import com.blakebr0.mysticalagradditions.lib.ModCorePlugin;
import com.blakebr0.mysticalagriculture.block.GrowthAcceleratorBlock;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.item.ItemBlock;
import com.xiaoxianben.lazymystical.registry.BlockRegistry;
import com.xiaoxianben.lazymystical.registry.ItemRegistry;

public class AgradditionsRegister {
    public AgradditionsRegister() {
        BlockRegistry.SEED_CULTIVATOR.add(BlockRegistry.BLOCKS.register("seed_cultivator_6", () -> new BlockSeedCultivator(6)));
        BlockRegistry.GROWTH_ACCELERATOR_6 = BlockRegistry.BLOCKS.register("growth_accelerator_6", () -> new GrowthAcceleratorBlock(12*6, ModCorePlugin.CROP_TIER_6.getTextColor()));

        ItemRegistry.ITEMS.register("seed_cultivator_6", () -> new ItemBlock(BlockRegistry.SEED_CULTIVATOR.get(5).get()));
        ItemRegistry.ITEMS.register("growth_accelerator_6", () -> new ItemBlock(BlockRegistry.GROWTH_ACCELERATOR_6.get()));
    }
}
