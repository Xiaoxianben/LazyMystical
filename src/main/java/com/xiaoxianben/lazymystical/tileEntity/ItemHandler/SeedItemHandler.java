package com.xiaoxianben.lazymystical.tileEntity.ItemHandler;


import com.xiaoxianben.lazymystical.manager.SeedManager;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SeedItemHandler extends InputItemHandler {


    public SeedItemHandler(int slotMax, Runnable run) {
        super(slotMax, run);
    }


    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot < this.getSlots()) {
            return SeedManager.isTrueSeed(Minecraft.getInstance().level, new Inventory(stack), 0);
        }
//        return slot == 1 && SeedUtil.isRootBlock(stack);
        return false;
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

}
