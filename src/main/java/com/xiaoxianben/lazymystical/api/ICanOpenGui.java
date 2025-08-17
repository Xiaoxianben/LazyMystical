package com.xiaoxianben.lazymystical.api;

import com.xiaoxianben.lazymystical.LazyMystical;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICanOpenGui {
    int getGuiId();

    default void openGui(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        playerIn.openGui(LazyMystical.MOD_ID, getGuiId(), worldIn, pos.getX(), pos.getY(), pos.getZ());
    }
}
