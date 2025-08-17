package com.xiaoxianben.lazymystical.gui.container;

import com.xiaoxianben.lazymystical.tileEntity.TEBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class ContainerBase<TE extends TEBase> extends Container {


    protected int inputSlotCount = 0;
    protected EntityPlayer player;
    TE te;


    protected ContainerBase(EntityPlayer player, TE te) {
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

        this.te = te;
        ++te.openGuiCount;
        te.sendUpdatePacket();
    }


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
                if (!this.mergeItemStack(itemStackOfSlot, 36, 36 + this.inputSlotCount, false)) {
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
            }
            if (itemStackOfSlot.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onSlotChanged();
            slot.onTake(player, itemStackOfSlot);
        }
        return itemstack;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;

        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot = this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();

                if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = slot.getSlotStackLimit();

                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.onSlotChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.onSlotChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
                    if (stack.getCount() > slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.splitStack(slot1.getSlotStackLimit()));
                    } else {
                        slot1.putStack(stack.splitStack(stack.getCount()));
                    }

                    slot1.onSlotChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return flag;
    }

    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        --te.openGuiCount;
        if (te.openGuiCount <= 0) {
            te.openGuiCount = 0;
        }
    }


    public TE getTE() {
        return te;
    }

    public void handlerButtonClick(int buttonId, int... ints) {
    }
}
