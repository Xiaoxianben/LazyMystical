package com.xiaoxianben.lazymystical.tileEntity.ItemHandler;

import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class OutputItemHandler extends ItemStackHandler {

    public TESeedCultivator te;


    public OutputItemHandler(int slotMax, TESeedCultivator te) {
        super(slotMax);
        this.te = te;
    }


    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
    }

    /**
     * <p>
     * Inserts an ItemStack into the given slot and return the remainder.
     * The ItemStack <em>should not</em> be modified in this function!
     * </p>
     * Note: This behaviour is subtly different from {@link IFluidHandler#fill(FluidStack, boolean)}
     *
     * @param slot     Slot to insert into.
     * @param stack    ItemStack to insert. This must not be modified by the item handler.
     * @param simulate If true, the insertion is only simulated
     * @return The remaining ItemStack that was not inserted (if the entire stack is accepted, then return an empty ItemStack).
     * May be the same as the input ItemStack if unchanged, otherwise a new ItemStack.
     * The returned ItemStack can be safely modified after.
     **/
    @Nonnull
    public ItemStack insertItemPrivate(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    protected void onContentsChanged(int slot) {
        if (te != null && slot == 0) {
            this.te.updateThis();
        }
    }

}
