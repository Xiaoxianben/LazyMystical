package com.xiaoxianben.lazymystical.manager;

import com.blakebr0.mysticalagriculture.api.crafting.IReprocessorRecipe;
import com.blakebr0.mysticalagriculture.api.crafting.RecipeTypes;
import com.blakebr0.mysticalagriculture.crafting.recipe.ReprocessorRecipe;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import com.xiaoxianben.lazymystical.recipe.ModRecipe;
import com.xiaoxianben.lazymystical.recipe.RecipeJson;
import com.xiaoxianben.lazymystical.recipe.recipeType.RecipeTypesOwn;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class SeedManager {


    private static final List<String> acceleratorBlockNames = Arrays.asList("inferium_growth_accelerator", "prudentium_growth_accelerator", "tertium_growth_accelerator", "imperium_growth_accelerator", "supremium_growth_accelerator", "growth_accelerator_6");
    public static Map<Item, seedResultItem> recipes = new LinkedHashMap<>();

    public static void addRecipe(List<RecipeJson<Item, ItemStack>> recipeJsons) {
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
        RecipeJson<Item, ItemStack> seedManagerRecipeJson = new RecipeJson<>(RecipeTypesOwn.recipe_item, RecipeTypesOwn.recipe_itemStack);
        for (Map.Entry<Item, List<ItemStack>> entry : seedManagerMap.entrySet()) {
            recipeJsons.add(seedManagerRecipeJson.create(i, Collections.singletonList(entry.getKey()), entry.getValue()));
            ++i;
        }
    }

    public static ReprocessorRecipe getModReprocessorRecipe(World world, IInventory inventory) {
        IReprocessorRecipe recipe = world.getRecipeManager().getRecipeFor(RecipeTypes.REPROCESSOR, inventory, world).orElse(null);
        return recipe instanceof ReprocessorRecipe ? (ReprocessorRecipe) recipe : null;
    }

    public static boolean isTrueSeed(Item seed) {
        return recipes.containsKey(seed);
    }

    @Nullable
    public static ItemStack getResultItem(Item seed) {
        try {
            return recipes.get(seed).getResultItem().copy();
        } catch (Exception e) {
            return null;
        }
    }

    public static int getResultItemCount(Item seed) {
        try {
            return recipes.get(seed).getResultItem().getCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public static List<ItemStack> getOtherResults(Item seed) {
        List<ItemStack> newResults = recipes.get(seed).getOtherResults();

        // 添加种子的默认实例
        newResults.add(seed.getDefaultInstance());

        // 返回不可变集合
        return Collections.unmodifiableList(newResults);
    }

    public static int getSeedTier(Item seed) {
        try {
            return recipes.get(seed).getSeedLevel();
        } catch (Exception e) {
            return 0;
        }
    }

    public static Block getCrux(Item seed) {
        try {
            return recipes.get(seed).getCrux();
        } catch (Exception e) {
            return null;
        }
    }

    public static int getAcceleratorBlockLevel(Block block) {
        try {
            return acceleratorBlockNames.indexOf(block.getRegistryName().getPath()) + 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public void init() {
        for (RecipeJson<Item, ItemStack> recipeJson : ModRecipe.seedManagerRecipeJson) {
            recipes.put(recipeJson.inputs.get(0), new seedResultItem(1, recipeJson.outputs, null));
        }

        CropRegistry.getInstance().getCrops().forEach(iCrop -> {
            if (iCrop.isEnabled()) {
                recipes.put(iCrop.getSeeds(),
                        new seedResultItem(iCrop.getTier().getValue(),
                                Collections.singletonList(iCrop.getEssence().getDefaultInstance()),
                                iCrop.getCrux()
                        ));
            }
        });

        recipes = Collections.unmodifiableMap(recipes);
    }

    public static class seedResultItem {

        private final int seedLevel;
        private final List<ItemStack> resultItems;
        private final Block crux;

        public seedResultItem(int seedLevel, List<ItemStack> ResultItems, Block crux) {
            this.seedLevel = seedLevel;
            resultItems = ResultItems;
            this.crux = crux;
        }

        public ItemStack getResultItem() {
            return resultItems.get(0);
        }

        public List<ItemStack> getOtherResults() {
            if (resultItems.size() >= 2) {
                return new ArrayList<>(resultItems.subList(1, resultItems.size()));
            }
            return new ArrayList<>();
        }

        public Block getCrux() {
            return crux;
        }

        public int getSeedLevel() {
            return seedLevel;
        }
    }
}
