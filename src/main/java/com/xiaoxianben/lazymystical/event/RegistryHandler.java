package com.xiaoxianben.lazymystical.event;

import com.xiaoxianben.lazymystical.init.EnumBlockType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;


@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {

        for (EnumBlockType enumBlockType : EnumBlockType.values()) {
            for (Block[] blocks : enumBlockType.getAllBlocks()) {
                Arrays.stream(blocks)
                        .map(block -> new ItemBlock(block).setRegistryName(block.getRegistryName()))
                        .forEach(item -> event.getRegistry().register(item));
            }
        }

    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {

        for (EnumBlockType enumBlockType : EnumBlockType.values()) {
            for (Block[] blocks : enumBlockType.getAllBlocks()) {
                event.getRegistry().registerAll(blocks);
            }
        }

    }

}
