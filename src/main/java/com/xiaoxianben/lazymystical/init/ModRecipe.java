package com.xiaoxianben.lazymystical.init;

import com.xiaoxianben.lazymystical.util.ModInformation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipe {
    public static void init() {
    }

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

    public static void SeedCultivatorRecipe(Block outBlock, int level) {
        Object[] material = getMaterial(level);
        if (material == null) return;
        String ingot = (String) material[0];
        String essence = (String) material[1];
        Block inputBlock = (Block) material[2];
        Object[] params = {
                "EIE",
                "IXI",
                "EIE",
                'E', essence,
                'I', ingot,
                'X', inputBlock
        };

        GameRegistry.addShapedRecipe(outBlock.getRegistryName(),
                new ResourceLocation(ModInformation.MOD_ID, "seedCultivator"),
                Item.getItemFromBlock(outBlock).getDefaultInstance(),
                params);
    }

    public static Object[] getMaterial(int level) {
        Object[] params = new Object[3];
        switch (level) {
            case 1:
                params[0] = "blockInferium";
                params[1] = "blockInferiumEssence";
                params[2] = Block.getBlockFromName("mysticalagriculture:mystical_machine_frame");
                break;
            case 2:
                params[0] = "blockPrudentium";
                params[1] = "blockPrudentiumEssence";
                params[2] = ModBlocks.inferiumSeedCultivator;
                break;
            case 3:
                params[0] = "blockIntermedium";
                params[1] = "blockIntermediumEssence";
                params[2] = ModBlocks.prudentiumSeedCultivator;
                break;
            case 4:
                params[0] = "blockSuperium";
                params[1] = "blockSuperiumEssence";
                params[2] = ModBlocks.intermediumSeedCultivator;
                break;
            case 5:
                params[0] = "blockSupremium";
                params[1] = "blockSupremiumEssence";
                params[2] = ModBlocks.superiumSeedCultivator;
                break;
            default:
                return null;
        }
        return params;
    }
}
