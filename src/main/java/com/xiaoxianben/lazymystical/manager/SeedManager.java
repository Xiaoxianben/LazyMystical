package com.xiaoxianben.lazymystical.manager;

import com.blakebr0.mysticalagriculture.api.crafting.IReprocessorRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.RecipeTypes;
import com.blakebr0.mysticalagriculture.block.InferiumCropBlock;
import com.blakebr0.mysticalagriculture.crafting.recipe.ReprocessorRecipe;
import com.blakebr0.mysticalagriculture.item.MysticalSeedsItem;
import com.xiaoxianben.lazymystical.recipe.ModRecipe;
import com.xiaoxianben.lazymystical.recipe.Recipe;
import com.xiaoxianben.lazymystical.recipe.recipeType.RecipeTypesOwn;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class SeedManager {


    private static final List<String> acceleratorBlockNames = Arrays.asList("inferium_growth_accelerator", "prudentium_growth_accelerator", "tertium_growth_accelerator", "imperium_growth_accelerator", "supremium_growth_accelerator");
    public static Map<Item, List<ItemStack>> recipes = new HashMap<>();

    public static void init() {
        if (ModList.get().isLoaded("mysticalagradditions")) {
        }
        for (Recipe<Item, ItemStack> recipe : ModRecipe.seedManagerRecipe) {
            recipes.put(recipe.inputs.get(0), recipe.outputs);
        }
        recipes = Collections.unmodifiableMap(recipes);
    }


    public static void addRecipe(List<Recipe<Item, ItemStack>> recipes) {
        LinkedHashMap<Item, List<ItemStack>> seedManagerMap = new LinkedHashMap<>();

        seedManagerMap.put(Items.WHEAT_SEEDS, Collections.singletonList(Items.WHEAT.getDefaultInstance())); // 小麦
        seedManagerMap.put(Items.BEETROOT_SEEDS, Collections.singletonList(Items.BEETROOT.getDefaultInstance())); // 甜菜
        seedManagerMap.put(Items.MELON_SEEDS, Collections.singletonList(new ItemStack(Items.MELON_SLICE, 3))); // 西瓜
        seedManagerMap.put(Items.PUMPKIN_SEEDS, Collections.singletonList(Items.PUMPKIN.getDefaultInstance())); // 南瓜
        seedManagerMap.put(Items.POTATO, Arrays.asList(Items.POTATO.getDefaultInstance(), Items.POISONOUS_POTATO.getDefaultInstance())); // 马铃薯
        seedManagerMap.put(Items.CARROT, Collections.singletonList(Items.CARROT.getDefaultInstance())); // 胡萝卜
        seedManagerMap.put(Items.COCOA_BEANS, Collections.singletonList(Items.COCOA_BEANS.getDefaultInstance())); // 可可
        seedManagerMap.put(Items.SUGAR_CANE, Collections.singletonList(Items.SUGAR_CANE.getDefaultInstance())); // 甘蔗
        seedManagerMap.put(Items.BAMBOO, Collections.singletonList(Items.BAMBOO.getDefaultInstance())); // 竹子
        seedManagerMap.put(Items.KELP, Collections.singletonList(Items.KELP.getDefaultInstance())); // 海带
        seedManagerMap.put(Items.SWEET_BERRIES, Collections.singletonList(Items.SWEET_BERRIES.getDefaultInstance())); // 甜浆果
        seedManagerMap.put(Items.CACTUS, Collections.singletonList(Items.CACTUS.getDefaultInstance())); // 仙人掌
        seedManagerMap.put(Items.NETHER_WART, Collections.singletonList(Items.NETHER_WART.getDefaultInstance())); // 地狱疣

        int i = 0;
        Recipe<Item, ItemStack> seedManagerRecipe = new Recipe<>(RecipeTypesOwn.recipe_item, RecipeTypesOwn.recipe_itemStack);
        for (Map.Entry<Item, List<ItemStack>> entry : seedManagerMap.entrySet()) {
            recipes.add(seedManagerRecipe.create(i, Collections.singletonList(entry.getKey()), entry.getValue()));
            ++i;
        }
    }


    public static ReprocessorRecipe getModReprocessorRecipe(World world, IInventory inventory) {
        IReprocessorRecipe recipe = world.getRecipeManager().getRecipeFor(RecipeTypes.REPROCESSOR, inventory, world).orElse(null);
        return recipe instanceof ReprocessorRecipe ? (ReprocessorRecipe) recipe : null;
    }

    @Nullable
    public static ItemStack getResultItem(World world, IInventory inventory, int slot) {
        ReprocessorRecipe recipe = getModReprocessorRecipe(world, inventory);
        if (recipe != null) {
            return recipe.getCraftingResult(null);
        }

        try {
            Item seed = inventory.getItem(slot).getItem();
            return recipes.get(seed).get(0).copy();
        } catch (Exception e) {
            return null;
        }
    }


    public static boolean isTrueSeed(World world, IInventory inventory, int slot) {
        return getResultItem(world, inventory, slot) != null;
    }


    public static int getResultItemCount(Item seed) {
        if (seed instanceof MysticalSeedsItem) {
            if (((MysticalSeedsItem) seed).getCrop().getCrop() instanceof InferiumCropBlock) {
                return ((MysticalSeedsItem) seed).getCrop().getTier().getValue();
            }
        }
        try {
            return recipes.get(seed).get(0).getCount();
        } catch (Exception e) {
            return 1;
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

    public static int getSeedTier(Item seed) {
        if (seed instanceof MysticalSeedsItem) {
            return ((MysticalSeedsItem) seed).getCrop().getTier().getValue();
        }
        return 1;
    }

    public static int getAcceleratorBlockLevel(Block block) {
        try {
            return acceleratorBlockNames.indexOf(block.getRegistryName().getPath());
        } catch (Exception e) {
            return 0;
        }
    }

}
