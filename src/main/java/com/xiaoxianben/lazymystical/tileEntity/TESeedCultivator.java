package com.xiaoxianben.lazymystical.tileEntity;

import com.xiaoxianben.lazymystical.api.IUpdateNBT;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.config.ConfigValue;
import com.xiaoxianben.lazymystical.manager.SeedManager;
import com.xiaoxianben.lazymystical.tileEntity.itemHandler.InputItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.itemHandler.OutputItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.itemHandler.SeedItemHandler;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class TESeedCultivator extends TileEntity implements ITickable, IUpdateNBT {

    protected final SeedItemHandler seedSlot;
    protected final InputItemHandler blockSlot;
    /**
     * "输出"物品槽 0: 精华 1: 种子
     */
    protected final OutputItemHandler outputSlot;
    public int timeRun = -1;
    public int maxTimeRun = 0;
    public int blockLevel = -1;
    public Item recipeInput = null;
    public ItemStack recipeOutput = null;
    public ItemStack recipeOutputOther = null;


    public TESeedCultivator() {
        seedSlot = new SeedItemHandler(1, this::updateThis);
        blockSlot = new InputItemHandler(1, this::updateThis) {
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return (SeedManager.getAcceleratorBlockLevel(Block.getBlockFromItem(stack.getItem())) - 1) == slot;
            }
        };
        outputSlot = new OutputItemHandler(2, this::updateThis);
    }


    @Override
    public void update() {
        // 判断是否在 服务器端
        if (!this.hasWorld() || this.getWorld().isRemote) {
            return;
        } else {
            this.init();
        }

        // 判断 是否可以运行
        if (!canRun()) {
            updateThisTime();
            return;
        }

        // 判断是否正在运行
        if (this.timeRun > 0) {
            this.timeRun -= (1 + this.getAllBlockLevel());
            if (this.timeRun < 0) {
                this.timeRun = 0;
            } else {
                this.sendUpdatePacket();
            }
        }

        switch (this.timeRun) {
            case 0:
                // 判断是否运行完成
                if (recipeInput != null) {
                    ItemStack itemEssenceStack = recipeOutput.copy();

                    int resultItemCount = recipeOutput.getCount();
                    itemEssenceStack.setCount(Math.min(this.seedSlot.getStackInSlot(0).getCount(), this.getMaxEffectiveSeedCount()) * resultItemCount);
                    this.outputSlot.insertItemPrivate(0, itemEssenceStack, false);

                    for (int i = 0; i < (itemEssenceStack.getCount() / resultItemCount); i++) {
                        if ((new Random().nextInt(ConfigValue.seedProbability) == 0)) {
                            this.outputSlot.insertItemPrivate(1, recipeOutputOther.copy(), false);
                        }
                    }
                }
            case -1:
                // 判断是否初次运行
                this.updateThisTime();
        }
    }

    /**
     * 更新方块等级和物品处理器
     */
    protected void init() {
        if (this.blockLevel <= 0) {
            this.blockLevel = ((BlockSeedCultivator) this.getBlockType()).getLevel();
            initItemHandler(this.blockLevel, this.blockSlot);
            initItemHandler(this.blockLevel > 5 ? 2 : 1, this.seedSlot);
        }
        ItemStack[] seedAndEssence = getSeedAndEssence();
        if (seedAndEssence != null) {
            this.recipeInput = seedAndEssence[0].getItem();
            this.recipeOutput = seedAndEssence[1];
            this.recipeOutputOther = seedAndEssence[2];
        } else {
            this.recipeInput = null;
            this.recipeOutput = null;
            this.recipeOutputOther = null;
        }
    }

    protected void initItemHandler(int maxInt, ItemStackHandler handler) {
        int i1 = Math.min(maxInt, handler.getSlots());
        NonNullList<ItemStack> stacks = NonNullList.withSize(i1, ItemStack.EMPTY);
        for (int i = 0; i < i1; i++) {
            stacks.set(i, handler.getStackInSlot(i));
        }
        handler.setSize(maxInt);
        for (int i = 0; i < i1; i++) {
            handler.setStackInSlot(i, stacks.get(i));
        }

    }

    /**
     * 更新还在运行中的自身，重新改变运行状态。
     */
    public void updateThis() {
        this.init();
        if (this.maxTimeRun > 0 && this.maxTimeRun != this.getMaxTimeRun()) {
            updateThisTime();
        }
    }

    protected void updateThisTime() {
        if (canRun()) {
            this.maxTimeRun = this.getMaxTimeRun();
            this.timeRun = this.maxTimeRun;
        } else {
            this.timeRun = -1;
            this.maxTimeRun = 0;
        }
        this.sendUpdatePacket();
    }


    public boolean canRun() {
        ItemStack item = this.seedSlot.getStackInSlot(0);
        if (this.recipeInput == null || item.isEmpty()) {
            return false;
        }
        if (!this.outputSlot.insertItemPrivate(0, this.recipeOutput, true).isEmpty()) {
            return false;
        }
        if (SeedManager.isTier6Seed(recipeInput)) {
            if (this.seedSlot.getSlots() == 2) {
                return !(this.seedSlot.getStackInSlot(1).isEmpty() ||
                        SeedManager.getRootBlockMeta(recipeInput) != this.seedSlot.getStackInSlot(1).getMetadata());
            }
            return false;
        }
        return true;
    }

    public int getMaxTimeRun() {
        if (recipeInput == null || this.seedSlot.getStackInSlot(0).isEmpty()) {
            return 0;
        }

        int seedTier = SeedManager.getSeedTier(recipeInput);
        int effectiveSeedCount = Math.min(this.seedSlot.getStackInSlot(0).getCount(), getMaxEffectiveSeedCount());

        return (int) (ConfigValue.seedSpeed * 20 * ConfigValue.seedLevelMultiplier[seedTier - 1] * (1 + (effectiveSeedCount - 1) * ConfigValue.seedNumberMultiplier)) / this.blockLevel;
    }

    protected int getMaxEffectiveSeedCount() {
        ItemStack itemStackOutput = this.outputSlot.getStackInSlot(0);

        int maxEffectiveSeedCount = (itemStackOutput.getMaxStackSize() - itemStackOutput.getCount()) / SeedManager.getResultItemCount(recipeInput);
        if (SeedManager.isTier6Seed(recipeInput)) {
            if (this.seedSlot.getSlots() == 2) {
                int rootBlockCount = this.seedSlot.getStackInSlot(1).getCount();
                maxEffectiveSeedCount = Math.min(maxEffectiveSeedCount, rootBlockCount);
            } else {
                maxEffectiveSeedCount = 0;
            }
        }
        return maxEffectiveSeedCount;
    }

    public int getAllBlockLevel() {
        int allBlockLevel = 0;
        for (int i = 0; i < this.blockSlot.getSlots(); i++) {

            int blockL = SeedManager.getAcceleratorBlockLevel(Block.getBlockFromItem(this.blockSlot.getStackInSlot(i).getItem()));
            int blockNum = this.blockSlot.getStackInSlot(i).getCount();
            int blockLevel = 0;

            if (blockL != 0) {
                blockLevel = (int) (blockNum * ConfigValue.acceleratorLevelMultiplier[blockL - 1]);
            }

            allBlockLevel += blockLevel;
        }
        return allBlockLevel;
    }

    /**
     * @return [Item, ItemStack] 格子中的 种子(Item) 和 对应的精华(ItemStack)
     */
    @Nullable
    private ItemStack[] getSeedAndEssence() {
        Item itemSeed = this.seedSlot.getStackInSlot(0).getItem();
        if (itemSeed == recipeInput) {
            return new ItemStack[]{new ItemStack(recipeInput), recipeOutput, recipeOutputOther};
        }

        ItemStack[] items = null;
        // 判断种子槽是否为空
        if (itemSeed != Items.AIR && SeedManager.getResultItemCount(itemSeed) != 0) {
            // 获取格子中的种子
            items = new ItemStack[3];
            items[0] = new ItemStack(itemSeed);
            items[1] = SeedManager.getResultItem(itemSeed);
            Set<ItemStack> otherResults = SeedManager.getOtherResults(itemSeed);
            items[2] = otherResults.toArray(new ItemStack[0])[new Random().nextInt(otherResults.size())];
        }

        return items;
    }

    public IItemHandler getItemHandler(int slot) {
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

    private void sendUpdatePacket() {
        if (this.getWorld().isRemote) return;
        SPacketUpdateTileEntity packet = this.getUpdatePacket();
        // 获取当前正在“追踪”目标 TileEntity 所在区块的玩家。
        // 之所以这么做，是因为在逻辑服务器上，不是所有的玩家都需要获得某个 TileEntity 更新的信息。
        // 比方说，有一个玩家和需要同步的 TileEntity 之间差了八千方块，或者压根儿就不在同一个维度里。
        // 这个时候就没有必要同步数据——强行同步数据实际上也没有什么用，因为大多数时候这样的操作都应会被
        // World.isBlockLoaded（func_175667_e）的检查拦截下来，避免意外在逻辑客户端上加载多余的区块。
        PlayerChunkMapEntry trackingEntry = ((WorldServer) this.world).getPlayerChunkMap().getEntry(this.pos.getX() >> 4, this.pos.getZ() >> 4);
        if (trackingEntry != null) {
            for (EntityPlayerMP player : trackingEntry.getWatchingPlayers()) {
                player.connection.sendPacket(packet);
            }
        }
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
        this.blockLevel = compound.getInteger("blockLevel");
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

        compound.setInteger("blockLevel", blockLevel);

        return compound;
    }

    @Override
    @Nonnull
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound updateNBT = new NBTTagCompound();

        updateNBT.setTag("updateNBT", this.getUpdateNBT());
        updateNBT.setTag("NBT", this.writeToNBT(new NBTTagCompound()));
        // 发送更新标签
        return new SPacketUpdateTileEntity(this.getPos(), 1, updateNBT);
    }

    @Override
    public void onDataPacket(@Nonnull net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound().getCompoundTag("NBT"));
        this.updateNBT(pkt.getNbtCompound().getCompoundTag("updateNBT"));
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return this.getCapability(capability, facing) != null;
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

    @Override
    public void updateNBT(NBTTagCompound NBT) {
        this.timeRun = NBT.getInteger("timeRun");
        this.maxTimeRun = NBT.getInteger("maxTimeRun");

        if (!NBT.hasKey("recipe")) {
            recipeInput = Item.getByNameOrId(NBT.getString("recipeInput"));
            recipeOutput = new ItemStack(NBT.getCompoundTag("recipeOutput"));
            recipeOutputOther = new ItemStack(NBT.getCompoundTag("recipeOutputOther"));
        }
    }

    @Override
    public NBTTagCompound getUpdateNBT() {
        NBTTagCompound NBT = new NBTTagCompound();
        NBT.setInteger("timeRun", timeRun);
        NBT.setInteger("maxTimeRun", maxTimeRun);

        if (recipeInput != null) {
            NBT.setString("recipeInput", Objects.requireNonNull(recipeInput.getRegistryName()).toString());
            NBT.setTag("recipeOutput", recipeOutput.writeToNBT(new NBTTagCompound()));
            NBT.setTag("recipeOutputOther", recipeOutputOther.writeToNBT(new NBTTagCompound()));
        } else {
            NBT.setString("recipe", "null");
        }
        return NBT;
    }
}
