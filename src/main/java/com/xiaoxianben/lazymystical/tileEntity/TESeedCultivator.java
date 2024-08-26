package com.xiaoxianben.lazymystical.tileEntity;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.config.ConfigValue;
import com.xiaoxianben.lazymystical.gui.ContainerSeedCultivator;
import com.xiaoxianben.lazymystical.manager.SeedManager;
import com.xiaoxianben.lazymystical.registry.TileEntityRegistry;
import com.xiaoxianben.lazymystical.tileEntity.ItemHandler.BaseItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.ItemHandler.InputItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.ItemHandler.OutputItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.ItemHandler.SeedItemHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.Set;

public class TESeedCultivator extends TileEntity implements ITickableTileEntity, INamedContainerProvider {


    /**
     * 0=timeRun, 1=maxTimeRun
     */
    public IntArraySeedCultivator intArray = new IntArraySeedCultivator(2);
    public int blockLevel = -1;

    protected SeedItemHandler seedSlot;
    protected InputItemHandler blockSlot;
    protected OutputItemHandler outputSlot;


    public TESeedCultivator() {
        super(TileEntityRegistry.TESeedCultivatorType.get());
        seedSlot = new SeedItemHandler(1, this::updateThis);
        blockSlot = new InputItemHandler(1, this::updateThis) {
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return SeedManager.getAcceleratorBlockLevel(Block.byItem(stack.getItem())) == slot;
            }
        };
        outputSlot = new OutputItemHandler(2, this::updateThis);
        intArray.set(0, -1);
        intArray.set(1, 0);
    }


    protected void init() {
        if (this.blockLevel <= 0) {
            this.blockLevel = ((BlockSeedCultivator) this.getBlockState().getBlock()).getLevel();
            int i1 = Math.min(this.blockLevel, blockSlot.getSlots());
            NonNullList<ItemStack> stacks = NonNullList.withSize(i1, ItemStack.EMPTY);
            for (int i = 0; i < i1; i++) {
                stacks.set(i, blockSlot.getStackInSlot(i));
            }
            blockSlot.setSize(this.blockLevel);
            for (int i = 0; i < i1; i++) {
                blockSlot.setStackInSlot(i, stacks.get(i));
            }
        }
    }


    @ParametersAreNonnullByDefault
    @Override
    public void load(BlockState blockState, CompoundNBT nbt) {
        super.load(blockState, nbt);

        CompoundNBT slotNBT = nbt.getCompound("slot");
        seedSlot.deserializeNBT(slotNBT.getCompound("seed"));
        blockSlot.deserializeNBT(slotNBT.getCompound("block"));
        outputSlot.deserializeNBT(slotNBT.getCompound("out"));


        this.intArray.set(0, nbt.getInt("timeRun"));
        this.intArray.set(1, nbt.getInt("maxTimeRun"));
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt = super.save(nbt);

        CompoundNBT slotNBT = new CompoundNBT();

        slotNBT.put("seed", seedSlot.serializeNBT());
        slotNBT.put("block", blockSlot.serializeNBT());
        slotNBT.put("out", outputSlot.serializeNBT());
        nbt.put("slot", slotNBT);

        nbt.putInt("timeRun", this.intArray.get(0));
        nbt.putInt("maxTimeRun", this.intArray.get(1));

        return nbt;
    }

    @Override
    @Nonnull
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT updateNBT = new CompoundNBT();

        updateNBT.put("NBT", this.save(new CompoundNBT()));
        // 发送更新标签
        return new SUpdateTileEntityPacket(this.getBlockPos(), 1, updateNBT);
    }

    @Override
    public void tick() {
        // 判断是否在 服务器端
        if (!this.hasLevel() || this.getLevel().isClientSide) {
            return;
        } else {
            this.init();
        }

        // 判断种子槽是否为空 和 输出槽是否满了
        if (!canRun()) {
            this.intArray.set(0, -1);
            this.intArray.set(1, 0);
            this.sendUpdatePacket();
            return;
        }

        // 判断是否正在运行
        if (this.intArray.get(0) > 0) {
            this.intArray.modify(0, -(1 + this.getAllBlockLevel()));
            if (this.intArray.get(0) < 0) {
                this.intArray.set(0, 0);
            }
        }

        switch (this.intArray.get(0)) {
            case 0:
                // 判断是否运行完成
                ItemStack[] items = this.getSeedAndEssence();
                if (items != null) {
                    Item itemSeed = items[0].getItem();
                    ItemStack itemStackEssence = items[1].copy();

                    int itemSeedCount = this.seedSlot.getStackInSlot(0).getCount();
                    itemStackEssence.setCount(Math.min(itemSeedCount, (itemSeed.getMaxStackSize() / SeedManager.getResultItemCount(itemSeed))) * SeedManager.getResultItemCount(itemSeed));

                    this.outputSlot.insertItemPrivate(0, itemStackEssence, false);
                    if (ConfigValue.seedProbability >= 1 && new Random().nextInt(ConfigValue.seedProbability) == 0) {
                        this.outputSlot.insertItemPrivate(1, items[2].copy(), false);
                    }
                }
            case -1:
                // 判断是否初次运行
                updateThisTime();
        }

        this.sendUpdatePacket();
    }

    /**
     * 更新还在运行中的自身，重新改变运行状态。
     */
    public void updateThis() {
        if (this.intArray.get(1) > 0 && this.canRun() && this.intArray.get(1) != this.getMaxTimeRun()) {
            updateThisTime();
        }
    }

    protected void updateThisTime() {
        this.intArray.set(0, this.getMaxTimeRun());
        this.intArray.set(1, this.getMaxTimeRun());
    }

    public boolean canRun() {
        ItemStack[] stack = this.getSeedAndEssence();
        if (stack == null) {
            return false;
        }
        if (!this.outputSlot.insertItemPrivate(0, stack[1], true).isEmpty()) {
            return false;
        }
//        ItemStack seed = this.seedSlot.getStackInSlot(0);
//        if (this.seedSlot.getSlots() == 2 &&
//                SeedManager.isTier6Seed(seed.getItem()) &&
//                (this.seedSlot.getStackInSlot(1).isEmpty() ||
//                        SeedManager.getRootBlockMeta(seed.getItem()) != this.seedSlot.getStackInSlot(1).getMetadata()
//                )
//        ) {
//            return false;
//        }
        return true;
    }

    public int getMaxTimeRun() {
        ItemStack itemStackSeed = this.seedSlot.getStackInSlot(0);
        ItemStack outputItemStack = this.outputSlot.getStackInSlot(0);

        int seedTier = SeedManager.getSeedTier(itemStackSeed.getItem());
        int maxEffectiveSeedCount = (outputItemStack.getMaxStackSize() - outputItemStack.getCount()) / SeedManager.getResultItemCount(itemStackSeed.getItem());
        int effectiveSeedCount = Math.min(itemStackSeed.getCount(), maxEffectiveSeedCount);

        return (int) (ConfigValue.seedSpeed * 20.0f * ConfigValue.seedLevelMultiplier.get(seedTier - 1) * (1 + (effectiveSeedCount - 1) * ConfigValue.seedNumberMultiplier)) / this.blockLevel;
    }

    public BaseItemHandler getItemHandler(int slot) {
        switch (slot) {
            case 0:
                return seedSlot;
            case 1:
                return blockSlot;
            case 2:
                return outputSlot;
        }
        return null;
    }

    public int getAllBlockLevel() {
        int allBlockLevel = 0;
        for (int i = 0; i < this.blockSlot.getSlots(); i++) {

            Block block = Block.byItem(this.blockSlot.getStackInSlot(i).getItem());
            if (block != Blocks.AIR) {
                int blockNum = this.blockSlot.getStackInSlot(i).getCount();
                int blockLevel = (int) (blockNum * ConfigValue.acceleratorLevelMultiplier.get(SeedManager.getAcceleratorBlockLevel(block)));

                allBlockLevel += blockLevel;
            }
        }
        return allBlockLevel;
    }

    /**
     * @return [种子(ItemStack), 对应的精华(ItemStack)]
     */
    @Nullable
    private ItemStack[] getSeedAndEssence() {
        ItemStack[] items = null;

        // 判断种子槽是否为空
        if (!this.seedSlot.getStackInSlot(0).isEmpty() && SeedManager.isTrueSeed(this.getLevel(), this.seedSlot.toIInventory(), 0)) {
            // 获取格子中的种子
            items = new ItemStack[3];
            items[0] = this.seedSlot.getStackInSlot(0);
            items[1] = SeedManager.getResultItem(this.getLevel(), this.seedSlot.toIInventory(), 0);
            Set<ItemStack> other = SeedManager.getOtherResults(items[0].getItem());
            items[2] = ((ItemStack) other.toArray()[new Random().nextInt(other.size())]).copy();
        }

        return items;
    }

    private void sendUpdatePacket() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    @ParametersAreNonnullByDefault
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> new CombinedInvWrapper(seedSlot, blockSlot, outputSlot)).cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onDataPacket(@Nonnull NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag().getCompound("NBT"));
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui." + LazyMystical.MODID + ".seed_cultivator");
    }

    @ParametersAreNonnullByDefault
    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerSeedCultivator(i, playerInventory, this.getBlockPos(), this.getLevel(), this.intArray);
    }
}
