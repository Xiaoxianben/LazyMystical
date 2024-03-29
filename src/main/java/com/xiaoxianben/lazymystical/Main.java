package com.xiaoxianben.lazymystical;

import com.xiaoxianben.lazymystical.GUI.GUIHandler;
import com.xiaoxianben.lazymystical.block.TESeedCultivator;
import com.xiaoxianben.lazymystical.event.ConfigLoader;
import com.xiaoxianben.lazymystical.event.PacketConsciousness;
import com.xiaoxianben.lazymystical.init.ModBlocks;
import com.xiaoxianben.lazymystical.init.ModRecipe;
import com.xiaoxianben.lazymystical.proxy.CommonProxy;
import com.xiaoxianben.lazymystical.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:mysticalagriculture")
public class Main {

    public static final List<Item> ITEMS = new ArrayList<>();
    public static final List<Block> BLOCKS = new ArrayList<>();
    private static SimpleNetworkWrapper network;

    @Mod.Instance
    public static Main instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        ConfigLoader.preInitConfigLoader(event);
        ModBlocks.preInit();
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
        network.registerMessage(new PacketConsciousness.Handler(), PacketConsciousness.class, 1, Side.CLIENT);
        ModBlocks.init();
        ModRecipe.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GUIHandler());
        GameRegistry.registerTileEntity(TESeedCultivator.class, new ResourceLocation(Reference.MOD_ID, "block_mystical_tank"));
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
    }

    public static SimpleNetworkWrapper getNetwork() {
        return network;
    }

    public static CreativeTabs tab = new CreativeTabs(Reference.MOD_ID) {
        @Nonnull
        @Override
        public ItemStack getTabIconItem() {
            return Item.getItemFromBlock(ModBlocks.inferiumSeedCultivator).getDefaultInstance();
        }
    };

}
