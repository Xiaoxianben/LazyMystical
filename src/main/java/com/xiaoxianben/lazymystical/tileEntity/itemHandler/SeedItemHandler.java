package com.xiaoxianben.lazymystical.tileEntity.itemHandler;


import com.xiaoxianben.lazymystical.manager.SeedManager;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SeedItemHandler extends InputItemHandler {


    public SeedItemHandler(int slotMax, Runnable run) {
        super(slotMax, run);
    }

    /**
     * Retrieves the maximum stack size allowed to exist in the given slot.
     *
     * @param slot Slot to query.
     * @return The maximum stack size allowed in the slot.
     */
    public int getSlotLimit(int slot) {
        return slot < this.getSlots() ? 64 : 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        switch (slot) {
            case 0:
                return SeedManager.getResultItemCount(stack.getItem()) != 0;
            case 1:
                return SeedManager.getRootBlock(this.getStackInSlot(0).getItem()) == Block.getBlockFromItem(stack.getItem());
            default:
                return false;
        }
    }

}
