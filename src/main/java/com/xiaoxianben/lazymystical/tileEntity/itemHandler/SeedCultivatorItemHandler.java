package com.xiaoxianben.lazymystical.tileEntity.itemHandler;

import com.xiaoxianben.lazymystical.block.BlockAccelerantDirt;
import com.xiaoxianben.lazymystical.config.ConfigValue;
import com.xiaoxianben.lazymystical.jsonRecipe.recipeType.RecipeTypesOwn;
import com.xiaoxianben.lazymystical.manager.seedManagr.SeedManager;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * [种0] ===> [结3][结4] <pre></pre>
 * [土1] [核7] [结5][结6] <pre></pre>
 * [器2]
 */
public class SeedCultivatorItemHandler extends ItemHandlerBase {


    private final TESeedCultivator te;
    protected int effectiveSeedCount = 0;
    protected int effectiveBlockCount = 0;
    protected int maxTimeRun = 0;
    protected CultivationRecipe nowRecipe = null;


    public SeedCultivatorItemHandler(TESeedCultivator TE) {
        super(8);
        te = TE;
    }


    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return getSlotLimit(slot);
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot == 2) {
            ItemStack stackInSlot = getStackInSlot(0);
            return stackInSlot.getCount() * stackInSlot.getMaxStackSize();
        }
        return super.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        switch (slot) {
            case 0:
                return SeedManager.getResultItemCount(stack) != 0;
            case 1:
                return Block.getBlockFromItem(stack.getItem()) instanceof BlockAccelerantDirt;
            case 2:
                return SeedManager.getAcceleratorBlockLevel(Block.getBlockFromItem(stack.getItem())) != 0;
            case 7:
                return te.blockLevel >= 6 && !stack.isEmpty() && getNowRecipe() != null && getNowRecipe().rootBlockMetadata == stack.getMetadata();
            case 3:
            case 4:
            case 5:
            case 6:
            default:
                return false;
        }
    }

    @Override
    public boolean canExtractItem(int slot) {
        switch (slot) {
            case 0:
            case 1:
            case 2:
            case 7:
                return false;
            case 3:
            case 4:
            case 5:
            case 6:
            default:
                return true;
        }
    }

    protected void onContentsChanged(int slot) {

        switch (slot) {
            case 2:
                computeAllBlockLevel();
                computeMaxTimeRun();
                break;
            case 0:
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                effectiveSeedCount = computeEffectiveSeedCount();
                computeAllBlockLevel();
                computeMaxTimeRun();
                this.te.updateThis();
                break;
            default:
        }
    }


    /**
     * @return 如果当前存在配方，则返回配方，否则返回null
     */
    @Nullable
    public CultivationRecipe getNowRecipe() {
        if (this.getStackInSlot(0).isEmpty()) {
            nowRecipe = null;
        } else if (nowRecipe != null && RecipeTypesOwn.recipe_itemStack.equals(nowRecipe.recipeInput, nowRecipe.recipeInput)) {
            return nowRecipe;
        } else {
            nowRecipe = new CultivationRecipe(this.getStackInSlot(0));
        }
        return nowRecipe;
    }

    public ItemStack insertResultItem(@Nonnull ItemStack stack, boolean simulate) {
        ItemStack stack1 = insertItemInternal(3, stack, simulate);
        stack1 = insertItemInternal(4, stack1, simulate);
        stack1 = insertItemInternal(5, stack1, simulate);
        stack1 = insertItemInternal(6, stack1, simulate);
        return stack1;
    }

    public ItemStack getSeedItemStack() {
        return this.getStackInSlot(0);
    }

    public ItemStack getAccelerantDritItemStack() {
        return this.getStackInSlot(1);
    }

    public ItemStack getAcceleratorBlockItemStack() {
        return this.getStackInSlot(2);
    }

    /**
     * @return 获取 当前配方的结果物品 的 所有 ItemStack，如果没有则返回 ItemStack.EMPTY
     */
    public ItemStack getResultItemStack() {
        CultivationRecipe recipe = getNowRecipe();
        if (recipe == null) {
            return ItemStack.EMPTY;
        }
        ItemStack[] itemStacks = {this.getStackInSlot(3), this.getStackInSlot(4), this.getStackInSlot(5), this.getStackInSlot(6)};
        ItemStack itemStack1 = new ItemStack(recipe.recipeOutput.getItem(), 0);

        for (ItemStack itemStack2 : itemStacks) {
            if (itemStack2.getItem() == itemStack1.getItem()) {
                itemStack1.grow(itemStack2.getCount());
            }
        }
        return itemStack1;
    }

    public ItemStack getRootBlockItemStack() {
        return this.getStackInSlot(7);
    }

    public int getEmptySlotAmount() {
        int amount = 0;
        for (int i = 0; i < 4; i++) {
            if (this.getStackInSlot(i + 3).isEmpty()) {
                amount++;
            }
        }
        return amount;
    }

    /**
     * @return 当前种子的有效数量，即可以产生结果的种子数量
     */
    public int getEffectiveSeedCount() {
        return effectiveSeedCount;
    }

    /**
     * @return 所有加速器和加速土的等级
     */
    public int getAllBlockLevel() {
        return effectiveBlockCount;
    }

    public int getMaxTimeRun() {
        return maxTimeRun;
    }


    protected void computeAllBlockLevel() {
        ItemStack accelerantDritItemStack = this.getAccelerantDritItemStack();
        if (accelerantDritItemStack.isEmpty()) {
            this.effectiveBlockCount = 0;
            return;
        }
        int accelerantDritLevel = SeedManager.getAccelerantDritLevel(Block.getBlockFromItem(accelerantDritItemStack.getItem()));
        int accelerantDritAmount = accelerantDritItemStack.getCount();

        ItemStack acceleratorBlockItemStack = this.getAcceleratorBlockItemStack();
        if (acceleratorBlockItemStack.isEmpty()) {
            this.effectiveBlockCount = 0;
            return;
        }
        int acceleratorBlockLevel = SeedManager.getAcceleratorBlockLevel(Block.getBlockFromItem(acceleratorBlockItemStack.getItem()));
        int acceleratorBlockAmount = acceleratorBlockItemStack.getCount();

        this.effectiveBlockCount = (int) (ConfigValue.acceleratorLevelMultiplier[acceleratorBlockLevel - 1] * acceleratorBlockAmount +
                ConfigValue.accelerantDritLevelMultiplier[accelerantDritLevel - 1] * accelerantDritAmount);
    }

    protected int computeEffectiveSeedCount() {
        CultivationRecipe nowRecipe1 = getNowRecipe();
        if (nowRecipe1 == null) {
            return 0;
        }
        int amount = getSeedItemStack().getCount();
        amount = Math.min(getAccelerantDritItemStack().getCount(), amount);
        int maxStackSize = nowRecipe1.recipeOutput.getMaxStackSize();
        amount = Math.min(maxStackSize - (getResultItemStack().getCount() % maxStackSize) + (getEmptySlotAmount() * maxStackSize), amount);

        if (SeedManager.isTier6Seed(nowRecipe1.recipeInput)) {
            ItemStack rootBlockItemStack = getRootBlockItemStack();
            if (!rootBlockItemStack.isEmpty() && nowRecipe1.rootBlockMetadata == rootBlockItemStack.getMetadata()) {
                amount = Math.min(rootBlockItemStack.getCount(), amount);
            } else {
                amount = 0;
            }
        }

        return amount;
    }

    protected void computeMaxTimeRun() {
        SeedCultivatorItemHandler.CultivationRecipe nowRecipe = this.getNowRecipe();
        if (nowRecipe == null) {
            maxTimeRun = 0;
            return;
        }

        int seedTier = SeedManager.getSeedTier(nowRecipe.recipeInput);
        int effectiveSeedCount = this.getEffectiveSeedCount();
        if (effectiveSeedCount <= 0) {
            maxTimeRun = 0;
            return;
        }

        maxTimeRun = (int) (ConfigValue.seedSpeed * 20 * ConfigValue.seedLevelMultiplier[seedTier - 1] *
                (1 + (effectiveSeedCount - 1) * ConfigValue.seedNumberMultiplier) / this.te.blockLevel);
    }


    /**
     * 用来记录 itemHandler 的当前配方，
     */
    public static class CultivationRecipe {
        @Nonnull
        public final ItemStack recipeInput;
        @Nonnull
        public final ItemStack recipeOutput;
        @Nonnull
        public final ItemStack[] recipeOutputOther;
        public final int rootBlockMetadata;

        protected CultivationRecipe(@Nonnull ItemStack recipeInput) {
            this.recipeInput = recipeInput.copy();
            this.recipeOutput = Objects.requireNonNull(SeedManager.getResultItem(recipeInput));
            this.recipeOutputOther = Objects.requireNonNull(SeedManager.getOtherResults(recipeInput));
            this.rootBlockMetadata = SeedManager.getRootBlockMeta(recipeInput);
        }
    }
}
