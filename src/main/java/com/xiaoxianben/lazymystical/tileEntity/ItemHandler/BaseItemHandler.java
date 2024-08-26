package com.xiaoxianben.lazymystical.tileEntity.ItemHandler;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BaseItemHandler extends ItemStackHandler {

    private final Runnable run;


    public BaseItemHandler(int slotMax, Runnable run) {
        super(slotMax);
        this.run = run;
    }


    @Override
    protected void onContentsChanged(int slot) {
        this.run.run();
    }

    public IInventory toIInventory() {
        return new Inventory(this.stacks.toArray(new ItemStack[0]));
    }

}
