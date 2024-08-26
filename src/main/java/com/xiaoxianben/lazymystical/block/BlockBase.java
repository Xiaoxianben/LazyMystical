package com.xiaoxianben.lazymystical.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

public class BlockBase extends Block {
    public BlockBase(Material material, SoundType soundType) {
        super(Properties.of(material)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
                .strength(5)
                .sound(soundType));
    }
}
