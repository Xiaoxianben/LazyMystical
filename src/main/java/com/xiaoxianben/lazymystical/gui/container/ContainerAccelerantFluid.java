package com.xiaoxianben.lazymystical.gui.container;


import com.xiaoxianben.lazymystical.tileEntity.TEAccelerantFluid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAccelerantFluid extends ContainerBase<TEAccelerantFluid> {


    public ContainerAccelerantFluid(EntityPlayer player, TEAccelerantFluid tileEntity) {
        super(player, tileEntity);
        ++inputSlotCount;

        // 将容器格子添加进入容器
        this.addSlotToContainer(new SlotItemHandler(tileEntity.container.itemStackHandler, 0, 39, 33));
    }

}
