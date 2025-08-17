package com.xiaoxianben.lazymystical.gui.slot;

import com.xiaoxianben.lazymystical.tileEntity.itemHandler.SeedCultivatorItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotSeedCultivatorItemHandler extends SlotItemHandler {

    private final int index;

    public SlotSeedCultivatorItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return !((SeedCultivatorItemHandler) this.getItemHandler()).extractItemInternal(this.index, 1, true).isEmpty();
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int amount) {
        return ((SeedCultivatorItemHandler) this.getItemHandler()).extractItemInternal(this.index, amount, false);
    }

}
