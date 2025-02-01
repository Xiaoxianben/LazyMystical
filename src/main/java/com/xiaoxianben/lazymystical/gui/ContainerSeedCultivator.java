package com.xiaoxianben.lazymystical.gui;

import com.xiaoxianben.lazymystical.gui.slot.SlotInputItemHandler;
import com.xiaoxianben.lazymystical.registry.ContainerTypeRegistry;
import com.xiaoxianben.lazymystical.tileEntity.IntArraySeedCultivator;
import com.xiaoxianben.lazymystical.tileEntity.ItemHandler.InputItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContainerSeedCultivator extends Container {

    public final TESeedCultivator te;
    protected final IntArraySeedCultivator intArray;
    final List<Rectangle> guiExtraArea = new ArrayList<>();
    protected int inSlotCount = 1;


    public ContainerSeedCultivator(int id, PlayerInventory playerInventory, BlockPos pos, World world, IntArraySeedCultivator intArray) {
        super(ContainerTypeRegistry.ContainerTypeSeedCultivator.get(), id);
        this.intArray = intArray;
        addDataSlots(this.intArray);

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
        // 将背包槽添加进容器
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        this.te = Objects.requireNonNull((TESeedCultivator) world.getBlockEntity(pos));
        this.addSlot(new SlotInputItemHandler((InputItemHandler) te.getItemHandler(0), 0, 39, 33));
        if (this.te.blockLevel > 5) {
            this.addSlot(new SlotInputItemHandler((InputItemHandler) te.getItemHandler(0), 1, 39, 33 + 18));
            inSlotCount++;
        }
        for (int i = 0; i < te.getItemHandler(1).getSlots(); i++) {
            this.addSlot(new SlotInputItemHandler((InputItemHandler) te.getItemHandler(1), i, -17, 1 + 18 * i));
            guiExtraArea.add(new Rectangle(-18, 18 * i, 18, 18));
            inSlotCount++;
        }
        this.addSlot(new SlotItemHandler(te.getItemHandler(2), 0, 99, 33));
        this.addSlot(new SlotItemHandler(te.getItemHandler(2), 1, 121, 33));

    }

    public static ContainerSeedCultivator creat(int windowId, PlayerInventory playerInventory, PacketBuffer buffer) {
        return new ContainerSeedCultivator(windowId, playerInventory, buffer.readBlockPos(), Minecraft.getInstance().level, new IntArraySeedCultivator(buffer.readVarIntArray()));
    }

    protected List<Rectangle> getGuiExtraAreas() {
        return guiExtraArea;
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity playerEntity) {
        return true;
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int slotNumber) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotNumber);

        if (slot != null && slot.hasItem()) {
            ItemStack itemStackOfSlot = slot.getItem();
            itemstack = itemStackOfSlot.copy();

            // 如果是在背包格子
            if (slotNumber < 36) {
                if (!this.moveItemStackTo(itemStackOfSlot, 36, 36 + this.inSlotCount, false)) {
                    int startIndex = slotNumber < 9 ? 9 : 0;
                    int endIndex = slotNumber < 9 ? 36 : 9;
                    if (!this.moveItemStackTo(itemStackOfSlot, startIndex, endIndex, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else {
                if (!this.moveItemStackTo(itemStackOfSlot, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStackOfSlot.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStackOfSlot.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemStackOfSlot);
        }
        return itemstack;
    }

}
