package com.xiaoxianben.lazymystical.tileEntity.ItemHandler;

import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import com.xiaoxianben.lazymystical.util.seed.SeedUtil;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SeedItemHandler extends InputItemHandler {

    public TESeedCultivator te;


    public SeedItemHandler(int slotMax, TESeedCultivator te) {
        super(slotMax);
        this.te = te;
    }


    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == 0 && SeedUtil.getSeeds().contains(stack.getItem())) {
            return true;
        }
        return slot == 1 && SeedUtil.isRootBlock(stack);
    }

    /**
     * Retrieves the maximum stack size allowed to exist in the given slot.
     *
     * @param slot Slot to query.
     * @return The maximum stack size allowed in the slot.
     */
    public int getSlotLimit(int slot) {
        return slot == 0 ? 64 : 1;
    }

    protected void onContentsChanged(int slot) {
        if (te != null && slot == 0) {
            this.te.updateThis();
        }
    }

}
