package com.xiaoxianben.lazymystical.tileEntity;

import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.api.IUpdateNBT;
import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.event.ConfigLoader;
import com.xiaoxianben.lazymystical.event.PacketConsciousness;
import com.xiaoxianben.lazymystical.tileEntity.ItemHandler.InputItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.ItemHandler.OutputItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.ItemHandler.SeedItemHandler;
import com.xiaoxianben.lazymystical.util.seed.SeedUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class TESeedCultivator extends TileEntity implements ITickable, IUpdateNBT {

    public int timeRun = -1;
    public int level;

    /**
     * “种子”物品槽
     */
    protected SeedItemHandler seedSlot;
    /**
     * “加速快”物品槽
     */
    protected InputItemHandler blockSlot;
    /**
     * "输出"物品槽 <p>
     * 0: 精华 <p>
     * 1: 种子
     */
    protected OutputItemHandler outputSlot = new OutputItemHandler(2) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return false;
        }
    };


    @SuppressWarnings("unused")
    public TESeedCultivator() {
        this(10);
    }

    public TESeedCultivator(int level) {
        this.level = level;

        seedSlot = new SeedItemHandler(1 + level / 6);
        blockSlot = new InputItemHandler(this.level) {
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
    }


    @SuppressWarnings("deprecation")
    @Override
    public void update() {
        // 判断是否在 服务器端
        if (!this.hasWorld() || this.getWorld().isRemote) {
            return;
        }

        // 判断种子槽是否为空 和 输出槽是否满了
        if (!canRun()) {
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

        // 运行过程中, 拿走部分种子
        if (this.timeRun > this.getMaxTimeRun()) {
            this.timeRun = (int) this.getMaxTimeRun();
        }

        switch (this.timeRun) {
            case 0:
                // 判断是否运行完成
                Object[] items = this.getSeedAndEssence();
                if (items != null) {
                    Item itemSeed = (Item) items[0];
                    ItemStack itemStackEssence = (ItemStack) items[1];

                    int itemSeedCount = this.seedSlot.getStackInSlot(0).getCount();
                    ItemStack itemEssenceStack = itemStackEssence.copy();
                    itemEssenceStack.setCount(Math.min(itemSeedCount, (itemSeed.getItemStackLimit() / SeedUtil.getCropCount(itemSeed))) * SeedUtil.getCropCount(itemSeed));

                    this.outputSlot.insertItemPrivate(0, itemEssenceStack, false);
                    if (ConfigLoader.seedProbability >= 1) {
                        this.outputSlot.insertItemPrivate(1, new ItemStack(itemSeed, ((new Random()).nextInt(ConfigLoader.seedProbability) == 0) ? 1 : 0), false);
                    }
                }
            case -1:
                // 判断是否运行结束
                this.timeRun = (int) this.getMaxTimeRun();
        }

        this.sendUpdatePacket();
    }


    public boolean canRun() {
        if (this.getSeedAndEssence() == null) {
            return false;
        }
        if (!this.outputSlot.insertItemPrivate(0, (ItemStack) this.getSeedAndEssence()[1], true).isEmpty()) {
            return false;
        }
        ItemStack seed = this.seedSlot.getStackInSlot(0);
        if (this.seedSlot.getSlots() == 2 &&
                SeedUtil.getSeedToMeta().containsKey(seed.getItem()) &&
                (this.seedSlot.getStackInSlot(1).isEmpty() ||
                        SeedUtil.getRootBlockMeta(seed.getItem()) != this.seedSlot.getStackInSlot(1).getMetadata()
                )
        ) {
            return false;
        }
        return this.outputSlot.insertItemPrivate(0, (ItemStack) this.getSeedAndEssence()[1], true).isEmpty();
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
        int itemSeedCount = itemStackSeed.getCount();
        int maxSeedCount = itemStackSeed.getMaxStackSize();

        int seedTier = SeedUtil.getCropTier(itemStackSeed.getItem());
        int cropCount = SeedUtil.getCropCount(itemStackSeed.getItem());
        maxSeedCount = Math.min(maxSeedCount / cropCount, (maxSeedCount - this.outputSlot.getStackInSlot(0).getCount()) / cropCount);

        int effectiveSeedCount = Math.min(itemSeedCount, maxSeedCount);

        return ConfigLoader.seedSpeed * 20.0f * ConfigLoader.seedLevelMultiplier[seedTier - 1] * (1 + (effectiveSeedCount - 1) * ConfigLoader.seedNumberMultiplier);
    }

    public int getAllBlockLevel() {
        int allBlockLevel = 0;
        for (int i = 0; i < this.blockSlot.getSlots(); i++) {

            Block block = Block.getBlockFromItem(this.blockSlot.getStackInSlot(i).getItem());
            int blockNum = this.blockSlot.getStackInSlot(i).getCount();
            int blockLevel = 0;

            if (block instanceof BlockAccelerator) {
                blockLevel = (int) (blockNum * ConfigLoader.acceleratorLevelMultiplier[((BlockAccelerator) block).getLevel() - 1]);
            } else if (block instanceof com.blakebr0.mysticalagriculture.blocks.BlockAccelerator) {
                blockLevel = (int) (blockNum * ConfigLoader.acceleratorLevelMultiplier[0]);
            }

            allBlockLevel += blockLevel;
        }
        return allBlockLevel;
    }

    /**
     * @return [Item, ItemStack] 格子中的 种子(Item) 和 对应的精华(ItemStack)
     */
    @Nullable
    private Object[] getSeedAndEssence() {
        Object[] items = null;

        Item itemSeed = this.seedSlot.getStackInSlot(0).getItem();

        // 判断种子槽是否为空
        if (!this.seedSlot.getStackInSlot(0).isEmpty() && SeedUtil.getCrop(itemSeed) != null) {
            // 获取格子中的种子
            items = new Object[2];
            items[0] = itemSeed;
            items[1] = SeedUtil.getCrop(itemSeed);
        }

        return items;
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
        return this.getCapability(capability, facing) != null;
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

        this.level = compound.getInteger("level");
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

        compound.setInteger("level", this.level);

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
