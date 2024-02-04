package com.xiaoxianben.lazymystical.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class IntSlotItemHandler extends SlotItemHandler {

    private final int StackLimit;
    private final int index;

    public IntSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition, int StackLimit) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
        this.StackLimit = StackLimit;
    }

    //限制物品槽中最多只能放置 物品
    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return this.StackLimit;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int amount) {
        return ((slotInt) this.getItemHandler()).extractItemPrivate(this.index, amount, false);
    }

}
