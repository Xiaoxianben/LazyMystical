package com.xiaoxianben.lazymystical.init.register;

import com.xiaoxianben.lazymystical.block.BlockAccelerantDirt;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.init.EnumItemType;
import com.xiaoxianben.lazymystical.init.ModRecipe;
import com.xiaoxianben.lazymystical.item.ItemAccelerantGel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class AgradditionsRegister implements IModRegister {

    public final int[] levels = new int[]{6};
    public final String[] ores = new String[]{"Insanium"};
    public final EnumModRegister selfRegister = EnumModRegister.AGRADDITIONS;

    @Override
    public void preInit() {
        // 注册方块
        for (EnumBlockType blockType : EnumBlockType.values()) {
            if (blockType.inputType != int.class) {
                continue;
            }
            List<Block> blocks = new ArrayList<>(levels.length);
            for (int level : levels) {
                blocks.add(blockType.create(level));
            }
            blockType.setBlocks(selfRegister, blocks.toArray(new Block[0]));
        }
        // 注册物品
        for (EnumItemType itemType : EnumItemType.values()) {
            if (itemType.inputType != int.class) {
                continue;
            }
            List<Item> items = new ArrayList<>(levels.length);
            for (int level : levels) {
                items.add(itemType.create(level));
            }
            itemType.setItems(selfRegister, items.toArray(new Item[0]));
        }
    }

    @Override
    public void init() {

        // Accelerator
        ModRecipe.acceleratorRecipe(EnumBlockType.Accelerator.getBlocks(selfRegister)[0], ores[0], EnumBlockType.Accelerator.getBlocks(EnumModRegister.MINECRAFT)[4]);

        // SeedCultivator
            ModRecipe.registerUpgradeRecipe(EnumBlockType.SeedCultivator.getBlocks(selfRegister)[0],
                    EnumBlockType.SeedCultivator.getBlocks(EnumModRegister.MINECRAFT)[4], ores[0]);

        // AccelerantFluid
        ModRecipe.registerUpgradeRecipe(EnumBlockType.AccelerantFluid.getBlocks(selfRegister)[0],
                EnumBlockType.AccelerantFluid.getBlocks(EnumModRegister.MINECRAFT)[4], ores[0]);

        // AccelerantDirt
        ModRecipe.registerAccelerantDirt((BlockAccelerantDirt) EnumBlockType.AccelerantDirt.getBlocks(selfRegister)[0],
                EnumBlockType.AccelerantDirt.getBlocks(EnumModRegister.MINECRAFT)[4]);

        // AccelerantGel
        Item[] items = EnumItemType.AccelerantGel.getItems(selfRegister);
        for (int i = 0; i < items.length; i++) {
            ModRecipe.registerAccelerantGel((ItemAccelerantGel) items[i], ores[i]);
        }
    }

    @Override
    public void posInit() {

    }
}
