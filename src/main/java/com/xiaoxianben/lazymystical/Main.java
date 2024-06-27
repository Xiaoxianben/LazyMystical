package com.xiaoxianben.lazymystical;

import com.xiaoxianben.lazymystical.GUI.GUIHandler;
import com.xiaoxianben.lazymystical.api.IModInit;
import com.xiaoxianben.lazymystical.config.ConfigLoader;
import com.xiaoxianben.lazymystical.init.ModBlocks;
import com.xiaoxianben.lazymystical.init.ModRecipe;
import com.xiaoxianben.lazymystical.otherModInit.OtherInit;
import com.xiaoxianben.lazymystical.proxy.CommonProxy;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import com.xiaoxianben.lazymystical.util.ModInformation;
import com.xiaoxianben.lazymystical.util.seed.SeedUtil;
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
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashSet;

@Mod(
        modid = ModInformation.MOD_ID,
        name = ModInformation.NAME,
        version = ModInformation.VERSION,
        dependencies = "required-after:mysticalagriculture;after:mysticalagradditions;after:jei"
)
public class Main {


    @Nullable
    public static LinkedHashSet<Item> ITEMS = new LinkedHashSet<>();
    @Nullable
    public static LinkedHashSet<Block> BLOCKS = new LinkedHashSet<>();
    @Nullable
    public static LinkedHashSet<IModInit> modInit = new LinkedHashSet<>();


    @Mod.Instance
    public static Main instance;
    @SidedProxy(clientSide = ModInformation.CLIENT_PROXY_CLASS, serverSide = ModInformation.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;


    public static CreativeTabs tab = new CreativeTabs(ModInformation.MOD_ID) {
        @Nonnull
        @Override
        public ItemStack getTabIconItem() {
            return Item.getItemFromBlock(ModBlocks.seedCultivators[0]).getDefaultInstance();
        }
    };
//    private static SimpleNetworkWrapper network;


    public Main() {
        modInit.add(new ModBlocks());
        modInit.add(new ModRecipe());
        modInit.add(new OtherInit());
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigLoader.preInitConfigLoader(event);

        modInit.forEach(IModInit::preInit);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
//        network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInformation.MOD_ID);
//        network.registerMessage(new PacketConsciousness.Handler(), PacketConsciousness.class, 1, Side.CLIENT);

        modInit.forEach(IModInit::init);

        // 注册物品
        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GUIHandler());
        GameRegistry.registerTileEntity(TESeedCultivator.class, new ResourceLocation(ModInformation.MOD_ID, "block_mystical_tank"));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        modInit.forEach(IModInit::postInit);

        ITEMS = null;
        BLOCKS = null;
        modInit = null;
        SeedUtil.init();
    }

//    public static SimpleNetworkWrapper getNetwork() {
//        return network;
//    }

}
