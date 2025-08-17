package com.xiaoxianben.lazymystical.manager.seedManagr;

import com.blakebr0.mysticalagriculture.crafting.ReprocessorManager;
import com.blakebr0.mysticalagriculture.crafting.ReprocessorRecipe;
import com.blakebr0.mysticalagriculture.items.ItemSeed;
import com.blakebr0.mysticalagriculture.items.ModItems;
import com.xiaoxianben.lazymystical.block.BlockAccelerantDirt;
import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.jsonRecipe.ModJsonRecipe;
import com.xiaoxianben.lazymystical.jsonRecipe.recipeType.RecipeTypesOwn;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** {@link  ModJsonRecipe#seedManagerRecipe} 的包装类 */
public class SeedManager {

    protected static final Map<ItemStack, IBlockState> RootBlockRecipes = new HashMap<>();


    public static void init() {
        for (ReprocessorRecipe recipe : ReprocessorManager.getRecipes()) {
            ItemStack stackOutput = recipe.getOutput().copy();
            ItemStack seedItemStack = recipe.getInput().copy();

            if (stackOutput.getItem() != ModItems.itemCrafting || stackOutput.getMetadata() != 0)
                stackOutput.setCount(1);

            SeedRegisterRecipe seedRegisterRecipe = new SeedRegisterRecipe(seedItemStack, stackOutput);

            ModJsonRecipe.seedManagerRecipe.addRecipe(seedItemStack.getItem().getRegistryName().toString(), seedItemStack, seedRegisterRecipe);
        }

        if (Loader.isModLoaded("mysticalagradditions")) {
            new AgradditionsModManager().init(ModJsonRecipe.seedManagerRecipe, RootBlockRecipes);
        }

    }

    @Nullable
    public static ItemStack getResultItem(ItemStack seed) {
        try {
            SeedRegisterRecipe output = ModJsonRecipe.seedManagerRecipe.getOutput(seed);
            if (output == null) return null;
            return output.output.copy();
        } catch (Exception e) {
            return null;
        }
    }

    public static ItemStack[] getOtherResults(ItemStack seed) {
        return Objects.requireNonNull(ModJsonRecipe.seedManagerRecipe.getOutput(seed)).copy().outputOther;
    }

    public static int getResultItemCount(ItemStack seed) {
        try {
            return Objects.requireNonNull(getResultItem(seed)).getCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isTier6Seed(ItemStack seedStack) {
        return getRootBlock(seedStack) != null;
    }

    /**
     * @return 如果没有相应的rootBlock返回null, 否则返回相应的rootBlock
     */
    public static Block getRootBlock(ItemStack seedStack) {
        IBlockState rootBlockState = getRootBlockState(seedStack);
        if (rootBlockState == null) {
            return null;
        }
        return rootBlockState.getBlock();
    }

    @Nullable
    protected static IBlockState getRootBlockState(ItemStack seedStack) {
        for (ItemStack itemStack : RootBlockRecipes.keySet()) {
            if (RecipeTypesOwn.recipe_itemStack.equals(itemStack, seedStack)) {
                return RootBlockRecipes.get(itemStack);
            }
        }
        return null;
    }

    /**
     * @return 如果没有相应的rootBlockMeta返回 -1, 否则 返回相应的rootBlockMeta
     */
    public static int getRootBlockMeta(ItemStack seed) {
        IBlockState blockState = getRootBlockState(seed);
        if (blockState != null)
            return blockState.getBlock().getMetaFromState(blockState);
        return -1;
    }

    public static int getSeedTier(ItemStack seed) {
        if (seed.getItem() instanceof ItemSeed) {
            return ((ItemSeed) seed.getItem()).getTier();
        }
        if (isTier6Seed(seed)) {
            return 6;
        }
        return 1;
    }

    public static int getAcceleratorBlockLevel(Block block) {
        try {
            if (block instanceof BlockAccelerator) {
                return ((BlockAccelerator) block).level;
            } else if (block instanceof com.blakebr0.mysticalagriculture.blocks.BlockAccelerator) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getAccelerantDritLevel(Block block) {
        try {
            if (block instanceof BlockAccelerantDirt) {
                return ((BlockAccelerantDirt) block).level;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 用于存储种子培养器的注册配方，不用于使用。
     * */
    public static class SeedRegisterRecipe {
        @Nonnull
        public final ItemStack input;
        @Nonnull
        public final ItemStack output;
        @Nonnull
        public final ItemStack[] outputOther;

        @ParametersAreNonnullByDefault
        public SeedRegisterRecipe(Item input, ItemStack output, ItemStack... outputOther) {
            this(new ItemStack(input), output, outputOther);
        }

        @ParametersAreNonnullByDefault
        public SeedRegisterRecipe(ItemStack input, ItemStack output, ItemStack... outputOther) {
            this.input = input;
            this.output = output;
            this.outputOther = outputOther;
        }

        public SeedRegisterRecipe copy() {
            ItemStack[] itemStacks = new ItemStack[this.outputOther.length];
            for (int i = 0; i < itemStacks.length; i++) {
                itemStacks[i] = this.outputOther[i].copy();
            }
            return new SeedRegisterRecipe(this.input.copy(), this.output.copy(), itemStacks);
        }
    }
}
