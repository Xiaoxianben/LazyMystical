package com.xiaoxianben.lazymystical.tileEntity.ItemHandler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class OutputItemHandler extends BaseItemHandler {


    public OutputItemHandler(int slotMax, Runnable run) {
        super(slotMax, run);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return false;
    }

    /**
     * @param slot     Slot to insert into.
     * @param stack    ItemStack to insert. This must not be modified by the item handler.
     * @param simulate If true, the insertion is only simulated
     * @return The remaining ItemStack that was not inserted (if the entire stack is accepted, then return an empty ItemStack).
     * Maybe the same as the input ItemStack if unchanged, otherwise a new ItemStack.
     * The returned ItemStack can be safely modified after.
     **/
    @Nonnull
    public ItemStack insertItemPrivate(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = this.stacks.get(slot);
            int limit = this.getStackLimit(slot, stack);
            if (!existing.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                    return stack;
                }

                limit -= existing.getCount();
            }

            if (limit <= 0) {
                return stack;
            } else {
                boolean reachedLimit = stack.getCount() > limit;
                if (!simulate) {
                    if (existing.isEmpty()) {
                        this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                    } else {
                        existing.grow(reachedLimit ? limit : stack.getCount());
                    }

                    this.onContentsChanged(slot);
                }

                return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
            }
        }
    }

}
