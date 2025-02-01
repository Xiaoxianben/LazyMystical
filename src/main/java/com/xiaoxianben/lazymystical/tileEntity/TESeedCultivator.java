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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class TESeedCultivator extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    protected final InputItemHandler blockSlot = new InputItemHandler(1, this::updateThis) {
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return (SeedManager.getAcceleratorBlockLevel(Block.byItem(stack.getItem())) - 1) == slot;
        }
    };
    protected final SeedItemHandler seedSlot = new SeedItemHandler(1, this::updateThis);
    protected final OutputItemHandler outputSlot = new OutputItemHandler(2, this::updateThis);

    /**
     * 0=timeRun, 1=maxTimeRun
     */
    public final IntArraySeedCultivator intArray = new IntArraySeedCultivator(-1, 0);
    public int blockLevel = -1;
    public Item recipeInput = null;
    public ItemStack recipeOutput = null;
    public ItemStack recipeOutputOther = null;


    public TESeedCultivator() {
        super(TileEntityRegistry.TESeedCultivatorType.get());
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
            this.updateThisTime();
            return;
        }

        // 判断是否正在运行
        if (this.intArray.get(0) > 0) {
            this.intArray.modify(0, -(1 + this.getAllBlockLevel()));
            if (this.intArray.get(0) < 0) {
                this.intArray.set(0, 0);
            } else {
                this.sendUpdatePacket();
            }
        }

        switch (this.intArray.get(0)) {
            case 0:
                // 运行完成
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
                // 初次运行
                updateThisTime();
        }
    }

    protected void init() {
        if (this.blockLevel <= 0) {
            this.blockLevel = ((BlockSeedCultivator) this.getBlockState().getBlock()).getLevel();
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

    public boolean canRun() {
        if (this.recipeInput == null || this.seedSlot.getStackInSlot(0).isEmpty()) {
            return false;
        }
        if (!this.outputSlot.insertItemPrivate(0, this.recipeOutput, true).isEmpty()) {
            return false;
        }
        Block crux = SeedManager.getCrux(this.recipeInput);
        if (crux != null) {
            if (seedSlot.getSlots() == 2) {
                return crux.asItem() == this.seedSlot.getStackInSlot(1).getItem();
            }
            return false;
        }
        return true;
    }

    protected void updateThisTime() {
        if (canRun()) {
            this.intArray.set(0, this.getMaxTimeRun());
            this.intArray.set(1, this.getMaxTimeRun());
        } else {
            this.intArray.set(0, -1);
            this.intArray.set(1, 0);
        }
        this.sendUpdatePacket();
    }

    public int getAllBlockLevel() {
        int allBlockLevel = 0;
        for (int i = 0; i < this.blockSlot.getSlots(); i++) {

            Block block = Block.byItem(this.blockSlot.getStackInSlot(i).getItem());
            int blockLevel = SeedManager.getAcceleratorBlockLevel(block);
            if (blockLevel > 0) {
                int blockNum = this.blockSlot.getStackInSlot(i).getCount();

                allBlockLevel += (int) (blockNum * ConfigValue.acceleratorLevelMultiplier.get(blockLevel - 1));
            }
        }
        return allBlockLevel;
    }

    protected int getMaxEffectiveSeedCount() {
        ItemStack itemStackOutput = this.outputSlot.getStackInSlot(0);

        int maxEffectiveSeedCount = (itemStackOutput.getMaxStackSize() - itemStackOutput.getCount()) / SeedManager.getResultItemCount(recipeInput);
        if (SeedManager.getCrux(recipeInput) != null) {
            if (this.seedSlot.getSlots() == 2) {
                int rootBlockCount = this.seedSlot.getStackInSlot(1).getCount();
                maxEffectiveSeedCount = Math.min(maxEffectiveSeedCount, rootBlockCount);
            } else {
                maxEffectiveSeedCount = 0;
            }
        }
        return maxEffectiveSeedCount;
    }

    /**
     * @return [种子(ItemStack), 对应的精华(ItemStack)]
     */
    public int getMaxTimeRun() {
        ItemStack seedStack = this.seedSlot.getStackInSlot(0);
        if (recipeInput == null || seedStack.isEmpty()) {
            return 0;
        }

        int seedTier = SeedManager.getSeedTier(recipeInput);
        int effectiveSeedCount = Math.min(this.seedSlot.getStackInSlot(0).getCount(), getMaxEffectiveSeedCount());

        return (int) (ConfigValue.seedSpeed * 20.0f * ConfigValue.seedLevelMultiplier.get(seedTier - 1) * (1 + (effectiveSeedCount - 1) * ConfigValue.seedNumberMultiplier)) / this.blockLevel;
    }

    /**
     * 更新还在运行中的自身，重新改变运行状态。
     */
    public void updateThis() {
        if (this.intArray.get(1) > 0 && this.intArray.get(1) != this.getMaxTimeRun()) {
            updateThisTime();
        }
    }

    @Nullable
    private ItemStack[] getSeedAndEssence() {
        Item itemSeed = this.seedSlot.getStackInSlot(0).getItem();
        if (itemSeed == recipeInput) {
            return new ItemStack[]{recipeInput.getDefaultInstance(), recipeOutput, recipeOutputOther};
        }

        ItemStack[] items = null;

        // 判断种子槽是否为空
        if (itemSeed != Items.AIR && SeedManager.getResultItemCount(itemSeed) != 0) {
            // 获取格子中的种子
            items = new ItemStack[3];
            items[0] = itemSeed.getDefaultInstance();
            items[1] = SeedManager.getResultItem(itemSeed);
            items[2] = SeedManager.getOtherResults(itemSeed).get(new Random().nextInt(SeedManager.getOtherResults(itemSeed).size()));
        }

        return items;
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


    // NBT
    private void sendUpdatePacket() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
    }

    @Override
    public void onDataPacket(@Nonnull NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.load(this.getBlockState(), pkt.getTag().getCompound("NBT"));
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

        this.blockLevel = nbt.getInt("blockLevel");
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
        nbt.putInt("blockLevel", this.blockLevel);

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

    // Capability
    @ParametersAreNonnullByDefault
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> new CombinedInvWrapper(seedSlot, blockSlot, outputSlot)).cast();
        }
        return super.getCapability(cap, side);
    }

    // gui
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
