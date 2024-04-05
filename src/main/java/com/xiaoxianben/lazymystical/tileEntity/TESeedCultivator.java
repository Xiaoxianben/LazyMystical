package com.xiaoxianben.lazymystical.tileEntity;

import com.blakebr0.mysticalagriculture.blocks.crop.BlockInferiumCrop;
import com.blakebr0.mysticalagriculture.items.ItemSeed;
import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.api.IUpdateNBT;
import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.event.PacketConsciousness;
import com.xiaoxianben.lazymystical.event.seedUtil;
import com.xiaoxianben.lazymystical.slot.slotInt;
import com.xiaoxianben.lazymystical.slot.slotOut;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import scala.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class TESeedCultivator extends TileEntity implements ITickable, IUpdateNBT {

    public int timeRun = -1;
    public int level;

    /**
     * “种子”物品槽
     */
    protected slotInt seedSlot = new slotInt(1, 64) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            Item item = stack.getItem();
            boolean isTureItem = item instanceof IPlantable;
            if (item instanceof ItemBlockSpecial) {
                isTureItem = ((ItemBlockSpecial) item).getBlock() instanceof IPlantable;
            }
            return isTureItem && slot == 0;
        }
    };
    /**
     * “加速快”物品槽
     */
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
    /**
     * "输出"物品槽
     * 0: 精华
     * 1: 种子
     */
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
        // 判断是否在 服务器端
        if (!this.hasWorld() || this.getWorld().isRemote) {
            return;
        }

        // 判断种子槽是否为空 和 输出槽是否满了
        if (this.seedSlot.getStackInSlot(0).isEmpty() || this.outputSlot.getStackInSlot(0).getCount() == this.outputSlot.getStackInSlot(0).getMaxStackSize()) {
            this.timeRun = -1;
            this.sendUpdatePacket();
            return;
        }

        // 判断是否正在运行
        if (this.timeRun > 0) {
            this.timeRun -= (1 + this.getAllBlockLevel()) * this.level;
            if (this.timeRun < 0) {
                this.timeRun = 0;
            }
        }

        switch (this.timeRun) {
            case 0:
                // 判断是否运行完成
                Item[] items = this.getSeedAndEssence();
                Item itemSeed = items[0];
                Item itemEssence = items[1];

                if (itemSeed != null && itemEssence != null) {
                    int itemSeedCount = this.seedSlot.getStackInSlot(0).getCount();
                    ItemStack itemEssenceStack = new ItemStack(itemEssence, itemSeedCount);

                    if (itemSeed instanceof IPlantable && ((IPlantable) itemSeed).getPlant(this.getWorld(), this.getPos()).getBlock() instanceof BlockInferiumCrop) {
                        itemEssenceStack.setCount(((ItemSeed) itemSeed).getTier() * itemSeedCount);
                    }

                    this.outputSlot.insertItemPrivate(0, itemEssenceStack, false);
                    this.outputSlot.insertItemPrivate(1, new ItemStack(itemSeed, (int) ((new Random().nextInt(101)) / 100.0f)), false);
                }
            case -1:
                // 判断是否运行结束
                this.timeRun = (int) this.getMaxTimeRun();
        }

        this.sendUpdatePacket();
    }

    private void sendUpdatePacket() {
        NBTTagCompound updateNBT = new NBTTagCompound();
        updateNBT.setTag("TileNBT", this.getUpdateTag());
        updateNBT.setTag("updateNBT", this.getUpdateNBT());
        Main.getNetwork().sendToAll(new PacketConsciousness(updateNBT));
    }

    public IItemHandler getTank(int slot) {
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
        ItemStack itemStackSeed = this.seedSlot.getStackInSlot(0);
        if (itemStackSeed.getItem() instanceof ItemSeed) {
            int itemSeedCount = itemStackSeed.getCount();
            int maxSeedCount = itemStackSeed.getMaxStackSize();

            if (((IPlantable) itemStackSeed.getItem()).getPlant(this.getWorld(), this.getPos()).getBlock() instanceof BlockInferiumCrop) {
                maxSeedCount = 64 / ((ItemSeed) itemStackSeed.getItem()).getTier();
            }

            return 2000.0f * 20 * ((ItemSeed) itemStackSeed.getItem()).getTier() * Math.min(itemSeedCount, maxSeedCount);
        }
        return 2000.0f * 20;
    }

    public int getAllBlockLevel() {
        int allBlockLevel = 0;
        for (int i = 0; i < this.blockSlot.getSlots(); i++) {

            Block block = Block.getBlockFromItem(this.blockSlot.getStackInSlot(i).getItem());
            int blockNum = this.blockSlot.getStackInSlot(i).getCount();
            int blockLevel = 0;

            if (block instanceof BlockAccelerator) {
                blockLevel = ((BlockAccelerator) block).getLevel() * blockNum;
            } else if (block instanceof com.blakebr0.mysticalagriculture.blocks.BlockAccelerator) {
                blockLevel = blockNum;
            }

            allBlockLevel += blockLevel;
        }
        return allBlockLevel;
    }

    /**
     * @return 格子中的 种子 和 对应的精华
     */
    private Item[] getSeedAndEssence() {
        Item[] items = new Item[]{null, null};
        Item Item;

        // 判断种子槽是否为空
        if (!this.seedSlot.getStackInSlot(0).isEmpty()) {
            // 获取格子中的种子
            Item = this.seedSlot.getStackInSlot(0).getItem();

            items[0] = Item;
            items[1] = seedUtil.getCrop(Item, this.getWorld(), this.getPos());

        }

        return items;
    }

    public boolean isTureSeedsItem(Item item) {
        boolean isTureItem = item instanceof IPlantable;
        if (item instanceof ItemBlockSpecial) {
            isTureItem = ((ItemBlockSpecial) item).getBlock() instanceof IPlantable;
        }
        return isTureItem;
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

        return compound;
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void updateNBT(NBTTagCompound NBT) {
        this.timeRun = NBT.getInteger("timeRun");
    }

    @Override
    public NBTTagCompound getUpdateNBT() {
        NBTTagCompound NBT = new NBTTagCompound();
        NBT.setInteger("timeRun", timeRun);
        return NBT;
    }
}
