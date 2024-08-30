package com.xiaoxianben.lazymystical.tileEntity.itemHandler;

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

}
