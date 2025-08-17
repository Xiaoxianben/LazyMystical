package com.xiaoxianben.lazymystical.block.machine;

import com.xiaoxianben.lazymystical.gui.GUIHandler;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSeedCultivator extends BlockMachineBase {


    public BlockSeedCultivator(Integer level) {
        super("seed_cultivator_" + level, level);
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TESeedCultivator();
    }

    @Override
    public int getGuiId() {
        return GUIHandler.id_seedCultivator;
    }
}
