package com.xiaoxianben.lazymystical.tileEntity.ItemHandler;


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
                return SeedManager.isTrueSeed(stack.getItem());
            case 1:
                Block crux = SeedManager.getCrux(this.getStackInSlot(0).getItem());
                return crux != null && crux.asItem() == stack.getItem();
            default:
                return false;
        }
    }

}
