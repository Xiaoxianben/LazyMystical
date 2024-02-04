package com.xiaoxianben.lazymystical.init;

import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.util.Reference;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static BlockAccelerator[] accelerators = new BlockAccelerator[5];

    public static BlockSeedCultivator inferiumSeedCultivator;
    public static BlockSeedCultivator prudentiumSeedCultivator;
    public static BlockSeedCultivator intermediumSeedCultivator;
    public static BlockSeedCultivator superiumSeedCultivator;
    public static BlockSeedCultivator supremiumSeedCultivator;

    public static void preInit() {
        for (int i = 1; i <= accelerators.length; i++) {
            accelerators[i - 1] = new BlockAccelerator(i);
        }

        inferiumSeedCultivator = new BlockSeedCultivator("inferium_seed_cultivator", 1);
        prudentiumSeedCultivator = new BlockSeedCultivator("prudentium_seed_cultivator", 2);
        intermediumSeedCultivator = new BlockSeedCultivator("tertium_seed_cultivator", 3);
        superiumSeedCultivator = new BlockSeedCultivator("imperium_seed_cultivator", 4);
        supremiumSeedCultivator = new BlockSeedCultivator("supremium_seed_cultivator", 5);
    }

    public static void init() {
        ModRecipe.SeedCultivatorRecipe(inferiumSeedCultivator, 1);
        ModRecipe.SeedCultivatorRecipe(prudentiumSeedCultivator, 2);
        ModRecipe.SeedCultivatorRecipe(intermediumSeedCultivator, 3);
        ModRecipe.SeedCultivatorRecipe(superiumSeedCultivator, 4);
        ModRecipe.SeedCultivatorRecipe(supremiumSeedCultivator, 5);

        String[] strings = {"blockInferium", "blockPrudentium", "blockIntermedium", "blockSuperium", "blockSupremium"};
        for (int i = 1; i <= accelerators.length; i++) {
            if (i == 1) {
                ModRecipe.acceleratorRecipe(accelerators[0], strings[0], "gemDiamond");
                GameRegistry.addShapelessRecipe(ModBlocks.accelerators[0].getRegistryName(),
                        new ResourceLocation(Reference.MOD_ID, "accelerator1"),
                        Item.getItemFromBlock(ModBlocks.accelerators[0]).getDefaultInstance(),
                        CraftingHelper.getIngredient(com.blakebr0.mysticalagriculture.blocks.ModBlocks.blockGrowthAccelerator));

            } else
                ModRecipe.acceleratorRecipe(accelerators[i - 1], strings[i - 1], accelerators[i - 2]);
        }

    }
}
