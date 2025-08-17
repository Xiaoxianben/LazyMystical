package com.xiaoxianben.lazymystical.manager;

import com.xiaoxianben.lazymystical.api.IHasHandlerComponent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackManager {
    public static void writeToNBTOfItemStack(ItemStack stack, NBTTagCompound nbt) {
        NBTTagCompound nbtOld = new NBTTagCompound();
        stack.writeToNBT(nbtOld);

        nbt.setString("id", nbtOld.getString("id"));
        nbt.setInteger("Count", stack.getCount());
        nbt.setInteger("Damage", stack.getItemDamage());
        if (nbtOld.hasKey("tag"))
            nbt.setTag("tag", nbtOld.getTag("tag"));

        if (nbtOld.hasKey("ForgeCaps")) nbt.setTag("ForgeCaps", nbtOld.getTag("ForgeCaps"));

    }

    public static ItemStack readFromNBTOfItemStack(NBTTagCompound nbt) {
        Item item = Item.getByNameOrId(nbt.getString("id"));
        final ItemStack stack = new ItemStack(item == null ? Items.AIR : item,
                nbt.getInteger("Count"),
                nbt.getInteger("Damage"),
                nbt.hasKey("ForgeCaps") ? nbt.getCompoundTag("ForgeCaps") : null);
        if (nbt.hasKey("tag"))
            stack.setTagCompound(nbt.getCompoundTag("tag"));

        return stack;
    }

    public static void writeToNBTOfItemStackHandler(ItemStackHandler handler, NBTTagCompound nbt) {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < handler.getSlots(); i++)
        {
            ItemStack stackInSlot = handler.getStackInSlot(i);
            if (!stackInSlot.isEmpty())
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                writeToNBTOfItemStack(stackInSlot, itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }

        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", handler.getSlots());
    }

    public static void readFromNBTOfItemStackHandler(ItemStackHandler handler, NBTTagCompound nbt) {
        handler.setSize(nbt.getInteger("Size"));
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound itemTag = tagList.getCompoundTagAt(i);
            int slot = itemTag.getInteger("Slot");
            if (slot >= 0 && slot < handler.getSlots())
            {
                handler.setStackInSlot(slot, readFromNBTOfItemStack(itemTag));
            }
        }
    }

    public static void spawnItemHandler(IItemHandler handler, World worldIn, BlockPos pos) {
        for (int i = 0; i < handler.getSlots(); i++) {

            ItemStack itemStack = handler.getStackInSlot(i);

            if (!itemStack.isEmpty()) {
                ItemStack copy = itemStack.copy();
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), copy));
            }
        }

    }

    public static boolean tryInsertComponent(EntityPlayer playerIn, ItemStack heldItemStack, IHasHandlerComponent tileEntity) {
        Item handItem = heldItemStack.getItem();
        ItemStack insertedComponent = playerIn.isSneaking() ? heldItemStack.copy() : new ItemStack(handItem);

        int i = insertedComponent.getCount() - tileEntity.getHandlerComponent().insertComponent(insertedComponent, false).getCount();
        heldItemStack.shrink(i);

        return i > 0;
    }
}
