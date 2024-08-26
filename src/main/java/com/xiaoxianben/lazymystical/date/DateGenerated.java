package com.xiaoxianben.lazymystical.date;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DateGenerated {
    @SubscribeEvent
    public static void dataGen(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        dataGenerator.addProvider(new BlockSeedCultivatorStateProvider(dataGenerator, existingFileHelper));
        dataGenerator.addProvider(new BlockDropsTagProvider(dataGenerator));
    }
}
