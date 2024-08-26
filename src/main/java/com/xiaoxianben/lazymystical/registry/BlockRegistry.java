package com.xiaoxianben.lazymystical.registry;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.item.ItemBlock;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LazyMystical.MODID);

    public static final RegistryObject<BlockSeedCultivator>[] SEED_CULTIVATOR = new RegistryObject[5];


    private void registerBlocks() {
        for (int i = 0; i < SEED_CULTIVATOR.length; i++) {
            final int finalI = i;
            final String name = ("seed_cultivator_" + (finalI + 1));

            SEED_CULTIVATOR[finalI] = BLOCKS.register(name, () -> new BlockSeedCultivator().setLevel(finalI + 1));
            ItemRegistry.ITEMS.register(name, () -> new ItemBlock(SEED_CULTIVATOR[finalI].get()));
        }
    }

    public void register(IEventBus modEvent) {
        registerBlocks();
        BLOCKS.register(modEvent);
    }
}
