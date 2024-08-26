package com.xiaoxianben.lazymystical.registry;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.gui.ContainerSeedCultivator;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypeRegistry {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, LazyMystical.MODID);

    public static RegistryObject<ContainerType<ContainerSeedCultivator>> ContainerTypeSeedCultivator;

    public ContainerTypeRegistry() {
        ContainerTypeSeedCultivator = CONTAINERS.register("container_seed_cultivator", () -> IForgeContainerType.create(ContainerSeedCultivator::creat));
    }

    public void register(IEventBus modEvent) {
        CONTAINERS.register(modEvent);
    }
}
