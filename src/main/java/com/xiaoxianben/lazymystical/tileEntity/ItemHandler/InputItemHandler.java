package com.xiaoxianben.lazymystical.tileEntity.ItemHandler;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class InputItemHandler extends BaseItemHandler {


    public InputItemHandler(int slotMax, Runnable run) {
        super(slotMax, run);
    }


    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    public ItemStack extractItemPrivate(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

}
