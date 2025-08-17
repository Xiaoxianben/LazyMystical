package com.xiaoxianben.lazymystical.block.machine;

import com.xiaoxianben.lazymystical.gui.GUIHandler;
import com.xiaoxianben.lazymystical.tileEntity.TEAccelerantFluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockAccelerantFluid extends BlockMachineBase {


    public BlockAccelerantFluid(int level) {
        super("accelerant_fluid_" + level, level);
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TEAccelerantFluid();
    }

    @Override
    public int getGuiId() {
        return GUIHandler.id_accelerantFluid;
    }
}
