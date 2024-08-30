package com.xiaoxianben.lazymystical.gui;


import com.xiaoxianben.lazymystical.slot.SlotInputItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BlockContainer extends Container {

    protected int addSlotCount = 3;

    protected List<Rectangle> BlockRectangles = new ArrayList<>();


    protected TESeedCultivator tileEntity;


    public BlockContainer(EntityPlayer player, TESeedCultivator tileEntity) {
        super();

        this.tileEntity = tileEntity;

        //将玩家物品槽第一行（快捷栏）加入容器
        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
        // 将背包槽添加进容器
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // 将容器格子添加进入容器
        this.addSlotToContainer(new SlotInputItemHandler(tileEntity.getItemHandler(1), 0, 39, 33));
        if (tileEntity.getItemHandler(1).getSlots() == 2) {
            this.addSlotToContainer(new SlotInputItemHandler(tileEntity.getItemHandler(1), 1, 39, 33 + 18));
            addSlotCount++;
        }
        for (int i = 0; i < tileEntity.getItemHandler(2).getSlots(); i++) {
            this.BlockRectangles.add(new Rectangle(-18, 18 * i, 18, 18));
            this.addSlotToContainer(new SlotInputItemHandler(tileEntity.getItemHandler(2), i, -17, 1 + 18 * i));
            addSlotCount++;
        }

        this.addSlotToContainer(new SlotItemHandler(tileEntity.getItemHandler(3), 0, 99, 33));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getItemHandler(3), 1, 121, 33));

    }


    @ParametersAreNonnullByDefault
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotNumber);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemStackOfSlot = slot.getStack();
            itemstack = itemStackOfSlot.copy();

            // 如果是在背包格子
            if (slotNumber < 36) {
                if (!this.mergeItemStack(itemStackOfSlot, 36, 36 + this.addSlotCount - 2, false)) {
                    int startIndex = slotNumber < 9 ? 9 : 0;
                    int endIndex = slotNumber < 9 ? 36 : 9;
                    if (!this.mergeItemStack(itemStackOfSlot, startIndex, endIndex, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (!this.mergeItemStack(itemStackOfSlot, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStackOfSlot.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemStackOfSlot.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemStackOfSlot);
        }
        return itemstack;
    }

    public List<Rectangle> getGuiExtraAreas() {
        return this.BlockRectangles;
    }
}
