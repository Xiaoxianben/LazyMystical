package com.xiaoxianben.lazymystical.slot;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class slotInt extends ItemStackHandler {

    protected int slotLimit;

    public slotInt(int slotMax, int slotLimit) {
        super(slotMax);
        this.slotLimit = slotLimit;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slotLimit;
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
