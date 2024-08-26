package com.xiaoxianben.lazymystical.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.config.ModConfigs;
import com.xiaoxianben.lazymystical.manager.SeedManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ModRecipe {


    public static List<Recipe<Item, ItemStack>> seedManagerRecipe;


    public ModRecipe() {

        List<Recipe<Item, ItemStack>> seedManager = new ArrayList<>();
        SeedManager.addRecipe(seedManager);
        seedManagerRecipe = saveRecipes(seedManager, "seedManagerRecipe");

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
    public <i, o> List<Recipe<i, o>> saveRecipes(List<Recipe<i, o>> defaultValue, String recipeName) {
        // 根据配方名称获取配方文件的路径
        File recipeFile = new File(getRecipePath(recipeName));
        // 如果配方文件已存在，则尝试从文件中加载配方信息
        if (recipeFile.exists()) {
            return loadRecipes(recipeFile, defaultValue.get(0));
        } else {
            recipeFile.getParentFile().mkdirs();
            // 如果文件不存在，则尝试创建并写入默认的配方信息
            try (FileWriter writer = new FileWriter(recipeFile)) {
                // 将对象转换为 JSON 格式并写入文件
                JsonArray jsonArray = new JsonArray();
                for (Recipe<i, o> item : defaultValue) {
                    jsonArray.add(item.toJsonObject());
                }
                // 使用 Gson 进行格式化输出，增强文件的可读性
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(jsonArray, writer);
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
    public <i, o> List<Recipe<i, o>> loadRecipes(File recipeFile, final Recipe<i, o> defaultValue) {
        try (FileReader reader = new FileReader(recipeFile)) {
            // 创建一个宽容的Gson对象，用于解析JSON
            Gson gson = new GsonBuilder().setLenient().create();
            // 将文件内容解析为JsonArray
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            // 使用默认值初始化一个配方对象
            Recipe<i, o> recipe = new Recipe<>(defaultValue.getInputRecipeType(), defaultValue.getOutputRecipeType());
            // 初始化配方列表
            List<Recipe<i, o>> value = new ArrayList<>();
            // 遍历JsonArray，将每个元素转换为Recipe对象并添加到列表中
            jsonArray.forEach(jsonElement -> value.add(recipe.JsonObjectToRecipe(jsonElement.getAsJsonObject())));
            return value;
        } catch (Exception e) {
            // 捕获异常，抛出运行时异常并包含异常信息和文件名
            throw new RuntimeException("配方文件：" + recipeFile.getName() + " 无法读取", e);
        }
    }

    String getRecipePath(String recipeName) {
        return ModConfigs.getConfigDirectory().getPath() + "/" + LazyMystical.MODID + "/" + recipeName + ".json";
    }
}
