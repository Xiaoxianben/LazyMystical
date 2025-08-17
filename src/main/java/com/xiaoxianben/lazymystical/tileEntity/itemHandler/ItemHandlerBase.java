package com.xiaoxianben.lazymystical.tileEntity.itemHandler;

import com.xiaoxianben.lazymystical.manager.ItemStackManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemHandlerBase extends ItemStackHandler {


    public ItemHandlerBase(int size) {
        super(size);
    }

    public ItemHandlerBase() {
        this(1);
    }


    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        ItemStackManager.writeToNBTOfItemStackHandler(this, nbt);

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        ItemStackManager.readFromNBTOfItemStackHandler(this, nbt);

        onLoad();
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (isItemValid(slot, stack)) {
            return insertItemInternal(slot, stack, simulate);
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (canExtractItem(slot)) {
            return extractItemInternal(slot, amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    /**
     * 只检查slot是否可以被提取。
     *
     * @param slot Slot to extract from.
     * @return true if the stack can be extracted from this slot, false otherwise.
     */
    public boolean canExtractItem(int slot) {
        return true;
    }

    /**
     * 只内部使用，其它慎用。
     *
     * @param slot     Slot to insert into.
     * @param stack    ItemStack to insert. This must not be modified by the item handler.
     * @param simulate If true, the insertion is only simulated
     * @return The remaining ItemStack that was not inserted (if the entire stack is accepted, then return an empty ItemStack).
     * Maybe the same as the input ItemStack if unchanged, otherwise a new ItemStack.
     * The returned ItemStack can be safely modified after.
     **/
    @Nonnull
    public ItemStack insertItemInternal(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    /**
     * 只内部使用，其它慎用。
     *
     * @param slot     Slot to extract from.
     * @param amount   Amount to extract (maybe greater than the current stack's max limit)
     * @param simulate If true, the extraction is only simulated
     * @return ItemStack extracted from the slot, must be empty if nothing can be extracted.
     * The returned ItemStack can be safely modified after, so item handlers should return a new or copied stack.
     **/
    @Nonnull
    public ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

}
