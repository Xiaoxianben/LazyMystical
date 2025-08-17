package com.xiaoxianben.lazymystical.event;

import com.xiaoxianben.lazymystical.api.IHasItemBlock;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.init.EnumItemType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;
import java.util.stream.Stream;


@Mod.EventBusSubscriber
public class RegistryHandler {

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {

        for (EnumBlockType enumBlockType : EnumBlockType.values()) {
            for (Block[] blocks : enumBlockType.getAllBlocks()) {
                Stream<Block> blockStream = Arrays.stream(blocks);

                if (!enumBlockType.allHasItemBlock) {
                    // 不是所有实例有itemBlock时，寻找特例。
                    blockStream = blockStream.filter(block -> block instanceof IHasItemBlock);
                }

                blockStream
                        .map(block -> new ItemBlock(block).setRegistryName(block.getRegistryName()))
                        .forEach(item -> event.getRegistry().register(item));
            }
        }

        for (EnumItemType enumItemType : EnumItemType.values()) {
            for (Item[] item : enumItemType.getAllItems()) {
                event.getRegistry().registerAll(item);
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
