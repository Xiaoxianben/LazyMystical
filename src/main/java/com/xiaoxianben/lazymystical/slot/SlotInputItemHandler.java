package com.xiaoxianben.lazymystical.slot;

import com.xiaoxianben.lazymystical.tileEntity.itemHandler.InputItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotInputItemHandler extends SlotItemHandler {

    private final int index;

    public SlotInputItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return !((InputItemHandler) this.getItemHandler()).extractItemPrivate(this.index, 1, true).isEmpty();
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int amount) {
        return ((InputItemHandler) this.getItemHandler()).extractItemPrivate(this.index, amount, false);
    }

}
