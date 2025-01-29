package com.xiaoxianben.lazymystical.init;

import com.xiaoxianben.lazymystical.LazyMystical;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipe {

    public static ModRecipe instance;

    public void acceleratorRecipe(Block outBlock, String block, Object inputBlock) {
        Object[] params = {
                "SIS",
                "IXI",
                "SIS",
                'S', "stone",
                'I', block,
                'X', inputBlock
        };
        GameRegistry.addShapedRecipe(
                outBlock.getRegistryName(),
                new ResourceLocation(LazyMystical.MOD_ID, "accelerator"),
                Item.getItemFromBlock(outBlock).getDefaultInstance(),
                params);
    }

    public void SeedCultivatorRecipe(Block outBlock, String blockOre, Block recipeBlock) {
        Object[] params = {
                "EBE",
                "BXB",
                "EBE",
                'E', blockOre + "Essence",
                'B', blockOre,
                'X', recipeBlock
        };

        GameRegistry.addShapedRecipe(outBlock.getRegistryName(),
                new ResourceLocation(LazyMystical.MOD_ID, "seedCultivator"),
                Item.getItemFromBlock(outBlock).getDefaultInstance(),
                params);
    }

}
