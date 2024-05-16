package com.xiaoxianben.lazymystical.tileEntity.ItemHandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class OutputItemHandler extends ItemStackHandler {

    public OutputItemHandler(int slotMax) {
        super(slotMax);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Nonnull
    public ItemStack insertItemPrivate(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

}
