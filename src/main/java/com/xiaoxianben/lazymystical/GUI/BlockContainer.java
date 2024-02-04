package com.xiaoxianben.lazymystical.GUI;


import com.blakebr0.mysticalagriculture.items.ItemSeed;
import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.block.TESeedCultivator;
import com.xiaoxianben.lazymystical.slot.IntSlotItemHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class BlockContainer extends Container {

    protected int addSlot = 8;

    public BlockContainer(EntityPlayer player, TESeedCultivator tileEntity) {
        super();
        this.addSlotToContainer(new IntSlotItemHandler(tileEntity.getTank(1), 0, 56, 27, 64));

        for (int i = 0; i < 5; i++) {
            this.addSlotToContainer(new IntSlotItemHandler(tileEntity.getTank(2), i, 20 + i * 18, 49, 128));
        }

        this.addSlotToContainer(new SlotItemHandler(tileEntity.getTank(3), 0, 116, 27));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.getTank(3), 1, 138, 27));

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

            Block block = Block.getBlockFromItem(itemStackOfSlot.getItem());

            boolean isSpecialItems = itemStackOfSlot.getItem() instanceof ItemSeed ||
                    block instanceof com.blakebr0.mysticalagriculture.blocks.BlockAccelerator ||
                    block instanceof BlockAccelerator;

            // 如果是在特殊格子
            if (slotNumber < addSlot) {
                if (!this.mergeItemStack(itemStackOfSlot, (9 + addSlot), (36 + addSlot), false)) {
                    if (!this.mergeItemStack(itemStackOfSlot, addSlot, (9 + addSlot), false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // 如果是特殊物品，将物品合并到特定槽中
            } else if (isSpecialItems) {
                if (!this.mergeItemStack(itemStackOfSlot, 0, addSlot, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotNumber < (9 + addSlot)) {
                // 如果是 物品槽第一行，将物品合并到 背包槽中
                if (!this.mergeItemStack(itemStackOfSlot, (9 + addSlot), (36 + addSlot), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotNumber >= (9 + addSlot) && slotNumber < (36 + addSlot)) {
                // 如果是 背包槽，将物品合并到 物品槽第一行
                if (!this.mergeItemStack(itemStackOfSlot, addSlot, (9 + addSlot), false)) {
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

}
