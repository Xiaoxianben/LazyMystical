package com.xiaoxianben.lazymystical.jsonRecipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.xiaoxianben.lazymystical.config.ConfigLoader;
import com.xiaoxianben.lazymystical.jsonRecipe.recipeType.RecipeTypesOwn;
import com.xiaoxianben.lazymystical.manager.seedManagr.SeedManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class ModJsonRecipe {


    public static Recipe<ItemStack, SeedManager.SeedRegisterRecipe> seedManagerRecipe = new Recipe<>(RecipeTypesOwn.recipe_itemStack, RecipeTypesOwn.recipe_seedCultivator);
    public static Recipe<ItemStack, Integer> accelerantFluidRecipe = new Recipe<>(RecipeTypesOwn.recipe_itemStack, RecipeTypesOwn.recipe_value);


    public static void init() {

        addSeedCultivatorRecipe(seedManagerRecipe);
        seedManagerRecipe = saveRecipes(seedManagerRecipe, "seedManagerRecipe");

        addAccelerantFluidRecipe(accelerantFluidRecipe);
        accelerantFluidRecipe = saveRecipes(accelerantFluidRecipe, "accelerantFluidRecipe");

    }

    static void addSeedCultivatorRecipe(Recipe<ItemStack, SeedManager.SeedRegisterRecipe> recipe) {
        LinkedHashMap<Item, List<ItemStack>> seedManagerMap = new LinkedHashMap<>();

        seedManagerMap.put(Items.WHEAT_SEEDS, Collections.singletonList(new ItemStack(Items.WHEAT))); // 小麦
        seedManagerMap.put(Items.BEETROOT_SEEDS, Collections.singletonList(new ItemStack(Items.BEETROOT))); // 甜菜
        seedManagerMap.put(Items.MELON_SEEDS, Collections.singletonList(new ItemStack(Items.MELON, 3))); // 西瓜
        seedManagerMap.put(Items.PUMPKIN_SEEDS, Collections.singletonList(new ItemStack(Blocks.PUMPKIN))); // 南瓜
        seedManagerMap.put(Items.POTATO, Arrays.asList(new ItemStack(Items.POTATO), new ItemStack(Items.POISONOUS_POTATO))); // 马铃薯
        seedManagerMap.put(Items.CARROT, Collections.singletonList(new ItemStack(Items.CARROT))); // 胡萝卜
//        seedManagerMap.put(Item.getItemFromBlock(Blocks.COCOA), Collections.singletonList(new ItemStack(Blocks.COCOA))); // 可可
        seedManagerMap.put(Items.REEDS, Collections.singletonList(new ItemStack(Items.REEDS))); // 甘蔗
        seedManagerMap.put(Item.getItemFromBlock(Blocks.CACTUS), Collections.singletonList(new ItemStack(Blocks.CACTUS))); // 仙人掌
        seedManagerMap.put(Items.NETHER_WART, Collections.singletonList(new ItemStack(Items.NETHER_WART))); // 地狱疣

        int i = 0;

        for (Map.Entry<Item, List<ItemStack>> entry : seedManagerMap.entrySet()) {
            ItemStack[] outputOther = new ItemStack[entry.getValue().size() - 1];
            if (outputOther.length > 0) {
                outputOther = entry.getValue().subList(1, entry.getValue().size()).toArray(new ItemStack[0]);
            }

            SeedManager.SeedRegisterRecipe seedRegisterRecipe = new SeedManager.SeedRegisterRecipe(entry.getKey(), entry.getValue().get(0), outputOther);
            recipe.addRecipe(Integer.toString(i), new ItemStack(entry.getKey()), seedRegisterRecipe);

            ++i;
        }
    }

    static void addAccelerantFluidRecipe(Recipe<ItemStack, Integer> recipe) {
        recipe.addRecipe("0", new ItemStack(Items.BONE), 9);
        recipe.addRecipe("1", new ItemStack(Items.DYE,1,15), 3);
        recipe.addRecipe("2", new ItemStack(Items.ROTTEN_FLESH), 9);
        recipe.addRecipe("3", new ItemStack(Items.SPIDER_EYE), 9);
        recipe.addRecipe("4", new ItemStack(Items.ENDER_PEARL), 18);
        recipe.addRecipe("5", new ItemStack(Items.GUNPOWDER), 9);
    }


    /**
     * 保存或加载配方信息的方法
     * 当配方文件已存在时，从文件中加载配方信息；否则，将默认的配方信息保存到文件中
     *
     * @param defaultValue 默认的配方列表，用于在文件不存在时保存
     * @param recipeName   配方的名称，用于定位配方文件
     * @return 返回加载的配方列表，若文件不存在则返回 defaultValue
     * @throws RuntimeException 当无法写入配方文件时抛出运行时异常
     */
    static <i, o> Recipe<i, o> saveRecipes(Recipe<i, o> defaultValue, String recipeName) {
        // 根据配方名称获取配方文件的路径
        File recipeFile = new File(getRecipePath(recipeName));
        // 如果配方文件已存在，则尝试从文件中加载配方信息
        if (recipeFile.exists()) {
            return loadRecipes(recipeFile, defaultValue);
        } else {
            recipeFile.getParentFile().mkdirs();
            // 如果文件不存在，则尝试创建并写入默认的配方信息
            try (FileWriter writer = new FileWriter(recipeFile)) {
                // 使用 Gson 进行格式化输出，增强文件的可读性
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(defaultValue.toJsonObject(), writer);
            } catch (Exception e) {
                // 如果发生异常，抛出运行时异常，提供详细的错误信息
                throw new RuntimeException("配方文件：" + recipeFile.getName() + " 无法写入", e);
            }
            // 返回默认的配方列表
            return defaultValue;
        }
    }


    /**
     * 加载配方文件
     *
     * @param recipeFile   配方文件路径
     * @param defaultValue 默认的配方对象，用于初始化配方类型
     * @param <i>          输入类型泛型
     * @param <o>          输出类型泛型
     * @return 返回加载的配方列表
     * @throws RuntimeException 如果无法读取配方文件，则抛出运行时异常
     */
    static <i, o> Recipe<i, o> loadRecipes(File recipeFile, final Recipe<i, o> defaultValue) {
        try (FileReader reader = new FileReader(recipeFile)) {
            // 创建一个宽容的Gson对象，用于解析JSON
            Gson gson = new GsonBuilder().setLenient().create();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            Recipe<i, o> recipe = new Recipe<>(defaultValue.getInputRecipeType(), defaultValue.getOutputRecipeType());
            return recipe.JsonObjectToRecipe(jsonObject);
        } catch (Exception e) {
            // 捕获异常，抛出运行时异常并包含异常信息和文件名
            throw new RuntimeException("配方文件：" + recipeFile.getName() + " 无法读取", e);
        }
    }

    static String getRecipePath(String recipeName) {
        return ConfigLoader.recipeDir + "/" + recipeName + ".json";
    }
}
