package com.xiaoxianben.lazymystical.init.register;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.block.BlockAccelerantDirt;
import com.xiaoxianben.lazymystical.fluid.FluidRegisterer;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.init.EnumItemType;
import com.xiaoxianben.lazymystical.init.ModRecipe;
import com.xiaoxianben.lazymystical.item.ItemAccelerantGel;
import com.xiaoxianben.lazymystical.item.compomemt.EnumTypeComponent;
import com.xiaoxianben.lazymystical.item.compomemt.ItemComponent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class MinecraftRegister implements IModRegister {

    public static Fluid accelerantFluid;

    public final int[] levels = new int[]{1, 2, 3, 4, 5};
    public final String[] ores = new String[]{"Inferium", "Prudentium", "Intermedium", "Superium", "Supremium"};
    private static final EnumModRegister selfRegister = EnumModRegister.MINECRAFT;

    @Override
    public void preInit() {
        // 注册方块
        for (EnumBlockType blockType : EnumBlockType.values()) {
            if (blockType.inputType != int.class) {
                continue;
            }
            List<Block> blocks = new ArrayList<>();
            for (int level : levels) {
                blocks.add(blockType.create(level));
            }
            blockType.setBlocks(selfRegister, blocks.toArray(new Block[0]));
        }
        EnumBlockType.HandCrankGenerator.setBlocks(selfRegister, EnumBlockType.HandCrankGenerator.create(null));

        // 注册物品
        for (EnumItemType itemType : EnumItemType.values()) {
            if (itemType.inputType != int.class) {
                continue;
            }
            List<Item> items = new ArrayList<>();
            for (int level : levels) {
                items.add(itemType.create(level));
            }
            itemType.setItems(selfRegister, items.toArray(new Item[0]));
        }

        registerAllComponent();

        // 注册流体
        accelerantFluid = FluidRegisterer.registerFluid(selfRegister, "accelerant_fluid").setDensity(2000).setViscosity(2000);

    }

    @Override
    public void init() {

        ModRecipe.registerHandClickGenerator();

        // Accelerator
        Block[] blocks = EnumBlockType.Accelerator.getBlocks(selfRegister);
        GameRegistry.addShapelessRecipe(
                new ResourceLocation(blocks[0].getRegistryName().toString() + "_2"),
                new ResourceLocation(LazyMystical.MOD_ID, "accelerator1"),
                new ItemStack(Item.getItemFromBlock(blocks[0])),
                CraftingHelper.getIngredient(com.blakebr0.mysticalagriculture.blocks.ModBlocks.blockGrowthAccelerator)
        );
        for (int i = 0; i < blocks.length; ++i) {
            Block block = blocks[i];
            ModRecipe.acceleratorRecipe(block, ores[i], i == 0 ? "gemDiamond" : blocks[i - 1]);
        }

        // SeedCultivator
        ModRecipe.SeedCultivatorRecipe();
        blocks = EnumBlockType.SeedCultivator.getBlocks(selfRegister);
        for (int i = 1; i < blocks.length; ++i) {
            ModRecipe.registerUpgradeRecipe(blocks[i], blocks[i - 1], ores[i]);
        }

        // AccelerantFluid
        ModRecipe.AccelerantFluidRecipe();
        blocks = EnumBlockType.AccelerantFluid.getBlocks(selfRegister);
        for (int i = 1; i < blocks.length; ++i) {
            ModRecipe.registerUpgradeRecipe(blocks[i], blocks[i - 1], ores[i]);
        }

        // AccelerantDirt
        ModRecipe.registerAccelerantDirt();
        blocks = EnumBlockType.AccelerantDirt.getBlocks(selfRegister);
        for (int i = 1; i < blocks.length; ++i) {
            ModRecipe.registerAccelerantDirt((BlockAccelerantDirt) blocks[i], blocks[i - 1]);
        }

        // AccelerantGel
        Item[] items = EnumItemType.AccelerantGel.getItems(selfRegister);
        for (int i = 0; i < items.length; i++) {
            ModRecipe.registerAccelerantGel((ItemAccelerantGel) items[i], ores[i]);
        }

        // component
        Item[] componentItems = EnumItemType.Component.getItems(selfRegister);
        ModRecipe.registerComponentRecipe(componentItems[0], FluidRegisterer.getBucket("accelerant_fluid"));
        ModRecipe.registerComponentRecipe(componentItems[1], Blocks.GLOWSTONE);
    }

    @Override
    public void posInit() {

    }


    protected static void registerAllComponent() {
        EnumTypeComponent[] values = EnumTypeComponent.values();
        Item[] items = new Item[values.length];

        for (int i = 0; i < items.length; i++) {
            EnumTypeComponent value = values[i];
            items[i] = new ItemComponent(value);
        }

        EnumItemType.Component.setItems(selfRegister, items);
    }
}
