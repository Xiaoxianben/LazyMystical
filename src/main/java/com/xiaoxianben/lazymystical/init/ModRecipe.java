package com.xiaoxianben.lazymystical.init;

import com.blakebr0.mysticalagriculture.blocks.ModBlocks;
import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.block.BlockAccelerantDirt;
import com.xiaoxianben.lazymystical.fluid.FluidRegisterer;
import com.xiaoxianben.lazymystical.init.register.EnumModRegister;
import com.xiaoxianben.lazymystical.item.ItemAccelerantGel;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipe {

    public static void acceleratorRecipe(Block outBlock, String block, Object inputBlock) {
        Object[] params = {
                "SIS",
                "IXI",
                "SIS",
                'S', "cobblestone",
                'I', "block" + block + "Essence",
                'X', inputBlock
        };
        GameRegistry.addShapedRecipe(
                outBlock.getRegistryName(),
                new ResourceLocation(LazyMystical.MOD_ID, "accelerator"),
                new ItemStack(Item.getItemFromBlock(outBlock)),
                params);
    }

    /**
     * 第一个方块的配方
     */
    public static void SeedCultivatorRecipe() {
        Block outBlock = EnumBlockType.SeedCultivator.getBlocks(EnumModRegister.MINECRAFT)[0];

        Object[] params = {
                "EBE",
                "BXB",
                "ENE",
                'E', "essenceInferium",
                'B', "ingotInferium",
                'N', "blockInferium",
                'X', ModBlocks.blockMysticalMachineFrame
        };

        GameRegistry.addShapedRecipe(
                outBlock.getRegistryName(),
                new ResourceLocation(LazyMystical.MOD_ID, "seedCultivator"),
                new ItemStack(Item.getItemFromBlock(outBlock)),
                params);
    }

    /**
     * 第一个方块的配方
     */
    public static void AccelerantFluidRecipe() {
        Block outBlock = EnumBlockType.AccelerantFluid.getBlocks(EnumModRegister.MINECRAFT)[0];
        Object[] params = {
                "IPI",
                "CXC",
                "IBI",
                'P', Blocks.PISTON,
                'C', Items.CAULDRON,
                'I', "ingotInferium",
                'B', "blockInferium",
                'X', ModBlocks.blockMysticalMachineFrame
        };

        GameRegistry.addShapedRecipe(
                outBlock.getRegistryName(),
                new ResourceLocation(LazyMystical.MOD_ID, "accelerantFluid"),
                new ItemStack(Item.getItemFromBlock(outBlock)),
                params);
    }

    public static void registerHandClickGenerator() {
        Block outBlock = EnumBlockType.HandCrankGenerator.getBlocks(EnumModRegister.MINECRAFT)[0];
        Object[] params = {
                "IMI",
                "MXM",
                "IMI",
//                'P', Blocks.PISTON,
//                'C', Items.CAULDRON,
                'I', "ingotInferium",
//                'B', "blockInferium",
                'M', Items.STICK,
                'X', ModBlocks.blockMysticalMachineFrame
        };

        GameRegistry.addShapedRecipe(
                outBlock.getRegistryName(),
                null,
                new ItemStack(Item.getItemFromBlock(outBlock)),
                params);
    }

    public static void registerUpgradeRecipe(Block outBlock, Block inputBlock, String ore) {
        Object[] params = {
                "EIE",
                "IXI",
                " B ",
                'I', "ingot" + ore,
                'E', "essence" + ore,
                'B', "block" + ore,
                'X', inputBlock
        };

        GameRegistry.addShapedRecipe(
                outBlock.getRegistryName(), null,
                new ItemStack(Item.getItemFromBlock(outBlock)),
                params);
    }

    public static void registerAccelerantDirt(BlockAccelerantDirt outBlock, Block inBlock) {
        GameRegistry.addShapelessRecipe(
                outBlock.getRegistryName(),
                new ResourceLocation(LazyMystical.MOD_ID, "accelerant_dirt"),
                new ItemStack(Item.getItemFromBlock(outBlock)),
                CraftingHelper.getIngredient(inBlock),
                CraftingHelper.getIngredient(Item.getByNameOrId(LazyMystical.MOD_ID + ":accelerant_gel_" + outBlock.level))
        );
    }

    /**
     * 第一个方块的配方
     */
    public static void registerAccelerantDirt() {
        BlockAccelerantDirt outBlock = (BlockAccelerantDirt) EnumBlockType.AccelerantDirt.getBlocks(EnumModRegister.MINECRAFT)[0];
        GameRegistry.addShapelessRecipe(
                outBlock.getRegistryName(),
                new ResourceLocation(LazyMystical.MOD_ID, "accelerant_dirt"),
                new ItemStack(Item.getItemFromBlock(outBlock)),
                CraftingHelper.getIngredient("dirt"),
                CraftingHelper.getIngredient(Item.getByNameOrId(LazyMystical.MOD_ID + ":accelerant_gel_" + outBlock.level))
        );
    }

    public static void registerAccelerantGel(ItemAccelerantGel item, String ore) {
        Object[] params = {
                "   ",
                "EFE",
                " E ",
                'E', "essence" + ore,
                // 'B', "block" + ore + "Essence",
                'F', FluidRegisterer.getBucket("accelerant_fluid")
        };

        GameRegistry.addShapedRecipe(
                item.getRegistryName(), null,
                new ItemStack(item),
                params);
    }

    public static void registerComponentRecipe(Item out, Object in) {
        Object[] params = {
                "III",
                "IFI",
                "III",
                'I', "ingotIron",
                'F', in
        };

        GameRegistry.addShapedRecipe(
                out.getRegistryName(), null,
                new ItemStack(out),
                params);
    }

}
