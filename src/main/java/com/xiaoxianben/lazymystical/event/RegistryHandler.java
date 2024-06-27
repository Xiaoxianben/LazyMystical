package com.xiaoxianben.lazymystical.event;

import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {

        event.getRegistry().registerAll(Main.ITEMS.toArray(new Item[0]));

    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {

        ModBlocks.addAllBlocks();
        event.getRegistry().registerAll(Main.BLOCKS.toArray(new Block[0]));

    }

}
