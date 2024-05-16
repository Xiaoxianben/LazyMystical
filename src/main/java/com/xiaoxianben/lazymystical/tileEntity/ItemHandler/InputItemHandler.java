package com.xiaoxianben.lazymystical.tileEntity.ItemHandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class InputItemHandler extends ItemStackHandler {


    public InputItemHandler(int slotMax) {
        super(slotMax);
    }


    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (this.isItemValid(slot, stack)) stack = super.insertItem(slot, stack, simulate);
        return stack;
    }

    @Nonnull
    public ItemStack extractItemPrivate(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

}
