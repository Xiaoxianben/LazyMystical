package com.xiaoxianben.lazymystical.GUI;


import com.xiaoxianben.lazymystical.slot.SlotInputItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
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

    @ParametersAreNonnullByDefault
    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;

        if (reverseDirection) {
            i = endIndex - 1;
        }

        while (!stack.isEmpty() && ((reverseDirection && i >= startIndex) || (!reverseDirection && i < endIndex))) {
            Slot slot = this.inventorySlots.get(i);
            ItemStack SlotItemstack = slot.getStack();

            // 如果槽位是空的或者可以放入物品
            boolean isTrueItem = this.isSameItem(stack, SlotItemstack);

            if (SlotItemstack.isEmpty() || isTrueItem) {
                int stackSize = Math.min(stack.getCount(), slot.getItemStackLimit(stack));

                if (stackSize > slot.getItemStackLimit(stack) - SlotItemstack.getCount()) {
                    stackSize = slot.getItemStackLimit(stack) - SlotItemstack.getCount();
                }

                ItemStack newItemStack = stack.splitStack(stackSize);
                newItemStack.setCount(newItemStack.getCount() + SlotItemstack.getCount());
                slot.putStack(newItemStack);
                slot.onSlotChanged();
                flag = true;
            }

            i += (reverseDirection ? -1 : 1);
        }

        return flag;
    }

    @ParametersAreNonnullByDefault
    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack itemstack = ItemStack.EMPTY;
        InventoryPlayer inventoryplayer = player.inventory;

        if (clickTypeIn == ClickType.QUICK_CRAFT) {
            return super.slotClick(slotId, dragType, clickTypeIn, player);
        } else if ((clickTypeIn == ClickType.PICKUP || clickTypeIn == ClickType.QUICK_MOVE) && (dragType == 0 || dragType == 1)) {
            if (slotId == -999 || clickTypeIn == ClickType.QUICK_MOVE) {
                return super.slotClick(slotId, dragType, clickTypeIn, player);
            } else {
                if (slotId < 0) {
                    return ItemStack.EMPTY;
                }

                Slot slot = this.inventorySlots.get(slotId);

                if (slot != null) {
                    ItemStack itemStackOfSlot = slot.getStack();
                    ItemStack itemStackOfMouse = inventoryplayer.getItemStack();

                    if (!itemStackOfSlot.isEmpty()) {
                        itemstack = itemStackOfSlot.copy();
                    }

                    if (itemStackOfSlot.isEmpty()) {
                        this.moveItemToSlot(slot, itemStackOfMouse, dragType, inventoryplayer);
                    } else if (slot.canTakeStack(player)) {
                        if (itemStackOfMouse.isEmpty() && !itemStackOfSlot.isEmpty()) {
                            int l2 = dragType == 0 ? itemStackOfSlot.getCount() : (itemStackOfSlot.getCount() + 1) / 2;
                            inventoryplayer.setItemStack(itemStackOfSlot.splitStack(l2));

                            slot.putStack(itemStackOfSlot);

                        } else if (slot.isItemValid(itemStackOfMouse)) {
                            if (this.isSameItem(itemStackOfSlot, itemStackOfMouse)) {
                                int k2 = dragType == 0 ? itemStackOfMouse.getCount() : 1;

                                if (k2 > slot.getItemStackLimit(itemStackOfMouse) - itemStackOfSlot.getCount()) {
                                    k2 = slot.getItemStackLimit(itemStackOfMouse) - itemStackOfSlot.getCount();
                                }

                                if (k2 > itemStackOfMouse.getMaxStackSize() - itemStackOfSlot.getCount()) {
                                    k2 = itemStackOfMouse.getMaxStackSize() - itemStackOfSlot.getCount();
                                }

                                itemStackOfSlot.grow(k2);
                                itemStackOfMouse.shrink(k2);
                                slot.putStack(itemStackOfSlot);
                                inventoryplayer.setItemStack(itemStackOfMouse);

                            } else if (itemStackOfMouse.getCount() <= slot.getItemStackLimit(itemStackOfMouse)) {
                                slot.putStack(itemStackOfMouse);
                                inventoryplayer.setItemStack(itemStackOfSlot);
                            }
                        }
                    }

                    slot.onSlotChanged();
                }
            }
        } else {
            return super.slotClick(slotId, dragType, clickTypeIn, player);
        }

        return itemstack;
    }

    private void moveItemToSlot(Slot slot, ItemStack itemStackOfMouse, int dragType, InventoryPlayer inventoryplayer) {
        if (slot.isItemValid(itemStackOfMouse)) {

            ItemStack itemStackOfSlot = slot.getStack();

            int i3 = dragType == 0 ? itemStackOfMouse.getCount() : 1;

            if (i3 > slot.getItemStackLimit(itemStackOfMouse) - itemStackOfSlot.getCount()) {
                i3 = slot.getItemStackLimit(itemStackOfMouse) - itemStackOfSlot.getCount();
            }

            if (i3 > itemStackOfMouse.getMaxStackSize() - itemStackOfSlot.getCount()) {
                i3 = itemStackOfMouse.getMaxStackSize() - itemStackOfSlot.getCount();
            }

            slot.putStack(itemStackOfMouse.splitStack(i3));
            inventoryplayer.setItemStack(itemStackOfMouse);

        }
    }

    private boolean isSameItem(ItemStack itemStackOfSlot, ItemStack itemStackOfMouse) {
        return itemStackOfSlot.getItem() == itemStackOfMouse.getItem() && itemStackOfSlot.getMetadata() == itemStackOfMouse.getMetadata() && ItemStack.areItemStackTagsEqual(itemStackOfSlot, itemStackOfMouse);
    }
}
