package com.xiaoxianben.lazymystical.init;

import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.api.IModInit;
import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.util.ModInformation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModBlocks implements IModInit {
    @Nullable
    public static LinkedHashSet<Block> allAccelerators = new LinkedHashSet<>();
    @Nullable
    public static LinkedHashSet<Block> allSeedCultivators = new LinkedHashSet<>();


    public static BlockAccelerator[] accelerators = new BlockAccelerator[5];
    public static BlockSeedCultivator[] seedCultivators = new BlockSeedCultivator[5];


    private static void addBlocks(LinkedHashSet<Block> blocks) {
        Main.BLOCKS.addAll(blocks);
        Main.ITEMS.addAll(blocks.stream()
                .map(block -> new ItemBlock(block)
                        .setRegistryName(Objects.requireNonNull(block.getRegistryName())))
                .collect(Collectors.toList()));
    }

    public static void addAllBlocks() {
        addBlocks(allAccelerators);
        addBlocks(allSeedCultivators);
    }


    public void preInit() {

        for (int i = 1; i <= accelerators.length; i++) {
            accelerators[i - 1] = new BlockAccelerator(i);
        }

        for (int i = 1; i <= seedCultivators.length; i++) {
            seedCultivators[i - 1] = new BlockSeedCultivator(i);
        }

    }

    public void init() {

        GameRegistry.addShapelessRecipe(
                new ResourceLocation(ModInformation.MOD_ID, Objects.requireNonNull(accelerators[0].getRegistryName()).getResourcePath() + "_2"),
                new ResourceLocation(ModInformation.MOD_ID, "accelerator1"),
                Item.getItemFromBlock(accelerators[0]).getDefaultInstance(),
                CraftingHelper.getIngredient(com.blakebr0.mysticalagriculture.blocks.ModBlocks.blockGrowthAccelerator)
        );

        String[] strings = {"blockInferium", "blockPrudentium", "blockIntermedium", "blockSuperium", "blockSupremium", "blockInsanium"};
        for (int i = 1; i <= accelerators.length; i++) {
            if (i == 1) {
                ModRecipe.acceleratorRecipe(accelerators[0], strings[0], "gemDiamond");
                ModRecipe.SeedCultivatorRecipe(seedCultivators[0], strings[0], com.blakebr0.mysticalagriculture.blocks.ModBlocks.blockMysticalMachineFrame);
            } else {
                ModRecipe.acceleratorRecipe(accelerators[i - 1], strings[i - 1], accelerators[i - 2]);
                ModRecipe.SeedCultivatorRecipe(seedCultivators[i - 1], strings[i - 1], seedCultivators[i - 2]);
            }
        }

    }

    @Override
    public void postInit() {
        allAccelerators = null;
        allSeedCultivators = null;
    }
}
