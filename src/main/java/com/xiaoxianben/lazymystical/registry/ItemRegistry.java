package com.xiaoxianben.lazymystical.registry;

import com.xiaoxianben.lazymystical.LazyMystical;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LazyMystical.MODID);

    private void registerItems() {
    }

    public void register(IEventBus modEvent) {
        registerItems();
        ITEMS.register(modEvent);
    }

}
