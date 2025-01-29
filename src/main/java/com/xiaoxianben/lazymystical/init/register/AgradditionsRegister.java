package com.xiaoxianben.lazymystical.init.register;

import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.init.ModRecipe;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class AgradditionsRegister implements IModRegister {

    public final int[] levels = new int[]{6};
    public final String[] oreBlocks = new String[]{"blockInsanium"};
    public final EnumModRegister selfRegister = EnumModRegister.AGRADDITIONS;

    @Override
    public void preInit() {
        for (EnumBlockType blockType : EnumBlockType.values()) {
            List<Block> blocks = new ArrayList<>();
            for (int level : levels) {
                blocks.add(blockType.create(level));
            }
            blockType.addBlocks(selfRegister, blocks.toArray(new Block[0]));
        }
    }

    @Override
    public void init() {

        int iB = 0;
        Block[] blocks = EnumBlockType.Accelerator.getBlocks(selfRegister);
        for (Block block : blocks) {
            ModRecipe.instance.acceleratorRecipe(block, oreBlocks[iB],
                    EnumBlockType.Accelerator.getBlocks(EnumModRegister.MINECRAFT)[4]);
            ++iB;
        }
        iB = 0;
        blocks = EnumBlockType.SeedCultivator.getBlocks(selfRegister);
        for (Block block : blocks) {
            ModRecipe.instance.SeedCultivatorRecipe(block, oreBlocks[iB],
                    EnumBlockType.SeedCultivator.getBlocks(EnumModRegister.MINECRAFT)[4]);
            ++iB;
        }
    }

    @Override
    public void posInit() {

    }
}
