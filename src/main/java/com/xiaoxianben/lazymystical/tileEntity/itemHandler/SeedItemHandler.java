package com.xiaoxianben.lazymystical.tileEntity.itemHandler;


import com.xiaoxianben.lazymystical.manager.SeedManager;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SeedItemHandler extends InputItemHandler {


    public SeedItemHandler(int slotMax, Runnable run) {
        super(slotMax, run);
    }


    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        switch (slot) {
            case 0:
                return SeedManager.getResultItemCount(stack.getItem()) != 0;
            case 1:
                return (!stack.isEmpty()) && SeedManager.getRootBlock(this.getStackInSlot(0).getItem()) == Block.getBlockFromItem(stack.getItem());
            default:
                return false;
        }
    }

}
