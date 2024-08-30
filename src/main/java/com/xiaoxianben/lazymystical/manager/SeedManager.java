package com.xiaoxianben.lazymystical.manager;

import com.blakebr0.mysticalagriculture.blocks.crop.BlockInferiumCrop;
import com.blakebr0.mysticalagriculture.crafting.ReprocessorManager;
import com.blakebr0.mysticalagriculture.crafting.ReprocessorRecipe;
import com.blakebr0.mysticalagriculture.items.ItemSeed;
import com.blakebr0.mysticalagriculture.items.ModItems;
import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.jsonRecipe.ModJsonRecipe;
import com.xiaoxianben.lazymystical.jsonRecipe.Recipe;
import com.xiaoxianben.lazymystical.jsonRecipe.recipeType.RecipeTypesOwn;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class SeedManager {

    /**
     * 字典：用于获取种子对应的作物
     */
    protected static LinkedHashMap<Item, List<ItemStack>> recipes = new LinkedHashMap<>();
    protected static Map<Item, IBlockState> RootBlockRecipes = new HashMap<>();


    public void init() {
        if (Loader.isModLoaded("mysticalagradditions")) {
            new AgradditionsModManager().init(recipes, RootBlockRecipes);
        }

        for (ReprocessorRecipe recipe : ReprocessorManager.getRecipes()) {
            ItemStack stackOutput = recipe.getOutput().copy();
            if (stackOutput.getItem() != ModItems.itemCrafting || stackOutput.getMetadata() != 0)
                stackOutput.setCount(1);
            recipes.put(recipe.getInput().getItem(), Collections.singletonList(stackOutput));
        }

        for (Recipe<Item, ItemStack> recipe : ModJsonRecipe.seedManagerRecipe) {
            recipes.put(recipe.inputs.get(0), recipe.outputs);
        }
    }


    public void addRecipe(List<Recipe<Item, ItemStack>> recipes) {
        LinkedHashMap<Item, List<ItemStack>> seedManagerMap = new LinkedHashMap<>();

        seedManagerMap.put(Items.WHEAT_SEEDS, Collections.singletonList(Items.WHEAT.getDefaultInstance())); // 小麦
        seedManagerMap.put(Items.BEETROOT_SEEDS, Collections.singletonList(Items.BEETROOT.getDefaultInstance())); // 甜菜
        seedManagerMap.put(Items.MELON_SEEDS, Collections.singletonList(new ItemStack(Items.MELON, 3))); // 西瓜
        seedManagerMap.put(Items.PUMPKIN_SEEDS, Collections.singletonList(new ItemStack(Blocks.PUMPKIN))); // 南瓜
        seedManagerMap.put(Items.POTATO, Arrays.asList(Items.POTATO.getDefaultInstance(), Items.POISONOUS_POTATO.getDefaultInstance())); // 马铃薯
        seedManagerMap.put(Items.CARROT, Collections.singletonList(Items.CARROT.getDefaultInstance())); // 胡萝卜
//        seedManagerMap.put(Item.getItemFromBlock(Blocks.COCOA), Collections.singletonList(new ItemStack(Blocks.COCOA))); // 可可
        seedManagerMap.put(Items.REEDS, Collections.singletonList(Items.REEDS.getDefaultInstance())); // 甘蔗
        seedManagerMap.put(Item.getItemFromBlock(Blocks.CACTUS), Collections.singletonList(new ItemStack(Blocks.CACTUS))); // 仙人掌
        seedManagerMap.put(Items.NETHER_WART, Collections.singletonList(new ItemStack(Items.NETHER_WART))); // 地狱疣

        int i = 0;
        Recipe<Item, ItemStack> seedManagerRecipe = new Recipe<>(RecipeTypesOwn.recipe_item, RecipeTypesOwn.recipe_itemStack);
        for (Map.Entry<Item, List<ItemStack>> entry : seedManagerMap.entrySet()) {
            recipes.add(seedManagerRecipe.create(i, Collections.singletonList(entry.getKey()), entry.getValue()));
            ++i;
        }
    }


    @Nullable
    public static ItemStack getResultItem(Item seed) {
        try {
            return recipes.get(seed).get(0).copy();
        } catch (Exception e) {
            return null;
        }
    }

    public static Set<ItemStack> getOtherResults(Item seed) {
        List<ItemStack> results = recipes.get(seed);
        List<ItemStack> newResults = new ArrayList<>();

        // 如果结果不为空，则添加除了第一个以外的所有元素
        if (results != null && !results.isEmpty()) {
            newResults.addAll(results.subList(1, results.size()));
        }

        // 添加种子的默认实例
        newResults.add(seed.getDefaultInstance());

        // 返回不可变集合
        return Collections.unmodifiableSet(new HashSet<>(newResults.stream().map(ItemStack::copy).collect(Collectors.toSet())));
    }

    public static Map<Item, List<ItemStack>> getSeedToCropMap() {
        return recipes;
    }

    public static int getResultItemCount(Item seed) {
        if (seed instanceof IPlantable) {
            if (((IPlantable) seed).getPlant(null, null).getBlock() instanceof BlockInferiumCrop) {
                return ((ItemSeed) seed).getTier();
            }
        }
        try {
            return recipes.get(seed).get(0).getCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isTier6Seed(Item seed) {
        return !RootBlockRecipes.isEmpty() && RootBlockRecipes.get(seed) != null;
    }

    /**
     * @return 如果没有相应的rootBlock返回null, 否则返回相应的rootBlock
     */
    public static Block getRootBlock(Item seed) {
        if (RootBlockRecipes.get(seed) != null)
            return RootBlockRecipes.get(seed).getBlock();
        return null;
    }

    /**
     * @return 如果没有相应的rootBlockMeta返回 -1, 否则 返回相应的rootBlockMeta
     */
    public static int getRootBlockMeta(Item seed) {
        if (getRootBlock(seed) != null)
            return getRootBlock(seed).getMetaFromState(RootBlockRecipes.get(seed));
        return -1;
    }

    public static int getSeedTier(Item seed) {
        if (seed instanceof ItemSeed) {
            return ((ItemSeed) seed).getTier();
        }
        if (isTier6Seed(seed)) {
            return 6;
        }
        return 1;
    }

    public static int getAcceleratorBlockLevel(Block block) {
        try {
            if (block instanceof BlockAccelerator) {
                return ((BlockAccelerator) block).getLevel();
            } else if (block instanceof com.blakebr0.mysticalagriculture.blocks.BlockAccelerator) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

}
