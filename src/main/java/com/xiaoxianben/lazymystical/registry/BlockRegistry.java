package com.xiaoxianben.lazymystical.registry;

import com.blakebr0.mysticalagriculture.block.GrowthAcceleratorBlock;
import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.item.ItemBlock;
import com.xiaoxianben.lazymystical.registry.register.AgradditionsRegister;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LazyMystical.MODID);

    public static final List<RegistryObject<BlockSeedCultivator>> SEED_CULTIVATOR = new ArrayList<>();
    public static RegistryObject<GrowthAcceleratorBlock> GROWTH_ACCELERATOR_6 = null;


    public BlockRegistry() {
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            final String name = ("seed_cultivator_" + (finalI + 1));

            SEED_CULTIVATOR.add(BLOCKS.register(name, () -> new BlockSeedCultivator(finalI + 1)));
            ItemRegistry.ITEMS.register(name, () -> new ItemBlock(SEED_CULTIVATOR.get(finalI).get()));
        }

        if (ModList.get().isLoaded("mysticalagradditions")) {
            new AgradditionsRegister();
        }
    }


    public void register(IEventBus modEvent) {
        BLOCKS.register(modEvent);
    }
}
