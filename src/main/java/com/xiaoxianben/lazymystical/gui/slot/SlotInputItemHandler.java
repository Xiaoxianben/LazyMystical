package com.xiaoxianben.lazymystical.gui.slot;

import com.xiaoxianben.lazymystical.tileEntity.ItemHandler.InputItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotInputItemHandler extends SlotItemHandler {

    protected final int index;

    public SlotInputItemHandler(InputItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
    }

    public boolean mayPickup(PlayerEntity playerIn) {
        return !((InputItemHandler) this.getItemHandler()).extractItemPrivate(this.index, 1, true).isEmpty();
    }

    @Nonnull
    public ItemStack remove(int amount) {
        return ((InputItemHandler) this.getItemHandler()).extractItemPrivate(this.index, amount, false);
    }

}
