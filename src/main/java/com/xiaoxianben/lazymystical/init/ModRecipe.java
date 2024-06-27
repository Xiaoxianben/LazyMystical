package com.xiaoxianben.lazymystical.init;

import com.xiaoxianben.lazymystical.api.IModInit;
import com.xiaoxianben.lazymystical.util.ModInformation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipe implements IModInit {
    public static void acceleratorRecipe(Block outBlock, String block, String inputBlock) {
        Object[] params = {
                "SIS",
                "IXI",
                "SIS",
                'S', "stone",
                'I', block,
                'X', inputBlock
        };
        GameRegistry.addShapedRecipe(outBlock.getRegistryName(),
                new ResourceLocation(ModInformation.MOD_ID, "accelerator"),
                Item.getItemFromBlock(outBlock).getDefaultInstance(),
                params);
    }

    public static void acceleratorRecipe(Block outBlock, String block, Block inputBlock) {
        Object[] params = {
                "SIS",
                "IXI",
                "SIS",
                'S', "stone",
                'I', block,
                'X', inputBlock
        };
        GameRegistry.addShapedRecipe(
                new ResourceLocation(ModInformation.MOD_ID, outBlock.getRegistryName() + "_1"),
                new ResourceLocation(ModInformation.MOD_ID, "accelerator"),
                Item.getItemFromBlock(outBlock).getDefaultInstance(),
                params);
    }

    public static void SeedCultivatorRecipe(Block outBlock, String blockOre, Block recipeBlock) {
        Object[] params = {
                "EBE",
                "BXB",
                "EBE",
                'E', blockOre + "Essence",
                'B', blockOre,
                'X', recipeBlock
        };

        GameRegistry.addShapedRecipe(outBlock.getRegistryName(),
                new ResourceLocation(ModInformation.MOD_ID, "seedCultivator"),
                Item.getItemFromBlock(outBlock).getDefaultInstance(),
                params);
    }

    public void preInit() {
    }

    public void init() {
    }

    @Override
    public void postInit() {

    }

}
