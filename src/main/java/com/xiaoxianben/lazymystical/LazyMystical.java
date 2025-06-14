package com.xiaoxianben.lazymystical;

import com.xiaoxianben.lazymystical.config.ConfigValue;
import com.xiaoxianben.lazymystical.config.ModConfigs;
import com.xiaoxianben.lazymystical.gui.ScreenSeedCultivator;
import com.xiaoxianben.lazymystical.manager.SeedManager;
import com.xiaoxianben.lazymystical.recipe.ModRecipe;
import com.xiaoxianben.lazymystical.registry.BlockRegistry;
import com.xiaoxianben.lazymystical.registry.ContainerTypeRegistry;
import com.xiaoxianben.lazymystical.registry.ItemRegistry;
import com.xiaoxianben.lazymystical.registry.TileEntityRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nonnull;

@Mod(LazyMystical.MODID)
public class LazyMystical {

    public static final String MODID = "lazymystical";
    public static final String NAME = "Lazy Mystical";


    public static final ItemGroup GROUP = new ItemGroup(MODID) {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockRegistry.SEED_CULTIVATOR.get(0).get().asItem());
        }
    };


    public LazyMystical() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        new BlockRegistry().register(eventBus);
        new ItemRegistry().register(eventBus);
        new TileEntityRegistry().register(eventBus);
        new ContainerTypeRegistry().register(eventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigs.config);

        eventBus.addListener(this::onCommonSetupEvent);
        eventBus.addListener(this::onClientSetupEvent);
        eventBus.addListener(this::FMLLoadCompleteEvent);

    }

    @SubscribeEvent
    public void onCommonSetupEvent(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() ->
                ScreenManager.register(ContainerTypeRegistry.ContainerTypeSeedCultivator.get(), ScreenSeedCultivator::new)
        );
    }

    @SubscribeEvent
    public void FMLLoadCompleteEvent(FMLLoadCompleteEvent event) {
        ConfigValue.init();
        new ModRecipe();
        new SeedManager().init();
    }
}
