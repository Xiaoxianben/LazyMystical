package com.xiaoxianben.lazymystical.block;

import com.blakebr0.mysticalagriculture.blocks.crop.BlockInferiumCrop;
import com.blakebr0.mysticalagriculture.blocks.crop.BlockMysticalCrop;
import com.blakebr0.mysticalagriculture.items.ItemSeed;
import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.event.PacketConsciousness;
import com.xiaoxianben.lazymystical.slot.slotInt;
import com.xiaoxianben.lazymystical.slot.slotOut;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class TESeedCultivator extends TileEntity implements ITickable {

    public int timeRun = 0;
    public int tick = 0;
    public int level;

    // “种子”物品槽
    protected slotInt seedSlot = new slotInt(1, 1) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() instanceof ItemSeed &&
                    (this.getStackInSlot(0).isEmpty() ||
                            stack.getCount() + this.getStackInSlot(0).getCount() <= this.getStackInSlot(0).getMaxStackSize()) &&
                    slot == 0;
        }
    };
    // “加速快”物品槽
    protected slotInt blockSlot = new slotInt(5, 128) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            boolean canInsertItem = false;
            if (slot < this.stacks.size()) {
                Block block = Block.getBlockFromItem(stack.getItem());
                ItemStack itemStackOfSlot = this.getStackInSlot(slot);

                boolean hasEmptyCount = itemStackOfSlot.isEmpty() || stack.getCount() + itemStackOfSlot.getCount() <= itemStackOfSlot.getMaxStackSize();

                boolean isTrueBlock = false;
                if (block instanceof BlockAccelerator) {
                    isTrueBlock = slot == ((BlockAccelerator) block).getLevel() - 1;
                } else if (block instanceof com.blakebr0.mysticalagriculture.blocks.BlockAccelerator) {
                    isTrueBlock = slot == 0;
                }

                canInsertItem = isTrueBlock && hasEmptyCount;
            }
            return canInsertItem;
        }
    };
    // "输出"物品槽
    protected slotOut outputSlot = new slotOut(2) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return false;
        }

    };

    public TESeedCultivator() {
        this(10);
    }

    public TESeedCultivator(int level) {
        this.level = level;
    }


    @Override
    public void update() {
        if (!this.hasWorld() || this.getWorld().isRemote) {
            return;
        }
        if (this.seedSlot.getStackInSlot(0).isEmpty()) {
            timeRun = 0;
            tick = 0;
        }
        if (this.timeRun > 0) {
            int blockNum = 0;
            for (int i = 0; i < 5; i++) {
                float T = 1;
                Block block = Block.getBlockFromItem(blockSlot.getStackInSlot(i).getItem());
                if (block instanceof BlockAccelerator) {
                    BlockAccelerator blockAccelerator = (BlockAccelerator) block;
                    T += (blockAccelerator.getLevel() - 1) / 10.0F;
                }
                blockNum += (int) (blockSlot.getStackInSlot(i).getCount() * T);
            }
            timeRun -= (1 + blockNum) * this.level;
        } else {
            ItemSeed itemSeed = null;
            ItemStack itemEssence = ItemStack.EMPTY;
            if (!this.seedSlot.getStackInSlot(0).isEmpty()) {
                // 获取格子中的种子
                itemSeed = (ItemSeed) this.seedSlot.getStackInSlot(0).getItem();
                // 获取格子对应的可种植方块
                BlockMysticalCrop crop = (BlockMysticalCrop) itemSeed.getPlant(this.getWorld(), this.getPos()).getBlock();
                // 根据方块获取对应的 精华
                itemEssence = new ItemStack(crop.func_149865_P(), (new Random().nextInt(101) / 100) + 1, 0);
                if (crop instanceof BlockInferiumCrop)
                    itemEssence = new ItemStack(crop.func_149865_P(), itemSeed.getTier(), 0);
            }
            // 判断是否能生成 精华
            if (!itemEssence.isEmpty() && outputSlot.insertItemPrivate(0, itemEssence, true).isEmpty()) {
                timeRun = (int) this.getMaxTimeRun();
                if (tick == 1) {
                    outputSlot.insertItemPrivate(0, itemEssence, false);
                    int randInt = new Random().nextInt(101) / 100;
                    if (randInt == 1) {
                        ItemStack itemStackSeed = itemSeed.getDefaultInstance().copy();
                        itemStackSeed.setCount(1);
                        outputSlot.insertItemPrivate(1, itemStackSeed, false);
                    }
                } else {
                    tick = 1;
                }
            }

        }

        NBTTagCompound updateNBT = new NBTTagCompound();
        updateNBT.setTag("consciousness", this.getUpdateTag());
        Main.getNetwork().sendToAll(new PacketConsciousness(updateNBT));
    }

    public <T> T getTank(int slot) {
        switch (slot) {
            case 1:
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(seedSlot);
            case 2:
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(blockSlot);
            case 3:
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputSlot);
        }
        return null;
    }

    public float getMaxTimeRun() {
        ItemStack itemSeed = this.seedSlot.getStackInSlot(0);
        if (itemSeed.getItem() instanceof ItemSeed)
            return (float) (2000 * ((ItemSeed) itemSeed.getItem()).getTier());
        return 0.0f;
    }

    @ParametersAreNonnullByDefault
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new CombinedInvWrapper(seedSlot, blockSlot, outputSlot));
        }
        return super.getCapability(capability, facing);
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    // NBT
    @ParametersAreNonnullByDefault
    public void readFromNBT(NBTTagCompound compound) {
        // 读取相关数据到 实体 中
        super.readFromNBT(compound);

        NBTTagCompound slotNBT = compound.getCompoundTag("slot");
        seedSlot.deserializeNBT(slotNBT.getCompoundTag("seed"));
        blockSlot.deserializeNBT(slotNBT.getCompoundTag("block"));
        outputSlot.deserializeNBT(slotNBT.getCompoundTag("out"));

        timeRun = compound.getInteger("timeRun");
        tick = compound.getInteger("tick");
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // 将相关数据保存到 compound 中
        compound = super.writeToNBT(compound);
        NBTTagCompound slotNBT = new NBTTagCompound();

        slotNBT.setTag("seed", seedSlot.serializeNBT());
        slotNBT.setTag("block", blockSlot.serializeNBT());
        slotNBT.setTag("out", outputSlot.serializeNBT());
        compound.setTag("slot", slotNBT);

        compound.setInteger("timeRun", timeRun);
        compound.setInteger("tick", tick);

        return compound;
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
}
