package com.xiaoxianben.lazymystical.registry;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

public class TileEntityRegistry {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, LazyMystical.MODID);

    public static RegistryObject<TileEntityType<TESeedCultivator>> TESeedCultivatorType;

    public TileEntityRegistry() {
        TESeedCultivatorType = TILE_ENTITIES.register("te_seedcultivator", () -> TileEntityType.Builder.of(TESeedCultivator::new, Arrays.stream(BlockRegistry.SEED_CULTIVATOR).map(RegistryObject::get).toArray(Block[]::new)).build(null));
    }

    public void register(IEventBus modEvent) {
        TILE_ENTITIES.register(modEvent);
    }

}
