package com.xiaoxianben.lazymystical.date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiaoxianben.lazymystical.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BlockDropsTagProvider extends LootTableProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    public static Map<ResourceLocation, LootTable> tables = new HashMap<>();
    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    protected final DataGenerator generator;

    protected BlockDropsTagProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
        generator = dataGenerator;
    }

    public void run(@Nonnull DirectoryCache var1) {
        this.getLootTables(lootTables);
        lootTables.forEach((block, builder) -> tables.put(block.getLootTable(), builder.build()));
        saveTables(var1, tables);
    }

    private void saveTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, LootTableManager.serialize(lootTable), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

    protected void getLootTables(Map<Block, LootTable.Builder> lootTables) {
        BlockRegistry.BLOCKS.getEntries().forEach(blockRegistryObject -> {
                    Block block = blockRegistryObject.get();
                    LootPool.Builder builder = LootPool.lootPool()
                            .name(block.getRegistryName().toString())
                            .setRolls(new ConstantRange(1))
                            .add(ItemLootEntry.lootTableItem(block));
                    lootTables.put(block, LootTable.lootTable().withPool(builder));
                }
        );
    }
}
