package com.xiaoxianben.lazymystical.tileEntity.ItemHandler;

import com.xiaoxianben.lazymystical.util.seed.SeedUtil;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SeedItemHandler extends InputItemHandler {

    public SeedItemHandler(int slotMax) {
        super(slotMax);
    }


    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == 0 && SeedUtil.getSeeds().contains(stack.getItem())) {
            return true;
        }
        return slot == 1 && SeedUtil.isRootBlock(stack);
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        if (slot == 1) {
            return 1;
        }
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

}
