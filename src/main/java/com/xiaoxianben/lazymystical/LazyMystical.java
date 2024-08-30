package com.xiaoxianben.lazymystical;

import com.xiaoxianben.lazymystical.api.IModInit;
import com.xiaoxianben.lazymystical.config.ConfigLoader;
import com.xiaoxianben.lazymystical.gui.GUIHandler;
import com.xiaoxianben.lazymystical.init.EnumBlockLevel;
import com.xiaoxianben.lazymystical.init.ModBlocks;
import com.xiaoxianben.lazymystical.init.ModRecipe;
import com.xiaoxianben.lazymystical.jsonRecipe.ModJsonRecipe;
import com.xiaoxianben.lazymystical.manager.SeedManager;
import com.xiaoxianben.lazymystical.proxy.CommonProxy;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import com.xiaoxianben.lazymystical.util.ModInformation;
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
import java.util.ArrayList;
import java.util.List;

@Mod(
        modid = ModInformation.MOD_ID,
        name = ModInformation.NAME,
        version = ModInformation.VERSION,
        dependencies = "required-after:mysticalagriculture;after:mysticalagradditions;after:jei"
)
public class LazyMystical {


    public static List<Item> ITEMS = new ArrayList<>();
    public static List<Block> BLOCKS = new ArrayList<>();
    public List<IModInit> modInit = new ArrayList<>();


    @Mod.Instance
    public static LazyMystical instance;
    @SidedProxy(clientSide = ModInformation.CLIENT_PROXY_CLASS, serverSide = ModInformation.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;


    public static CreativeTabs tab = new CreativeTabs(ModInformation.MOD_ID) {
        @Nonnull
        @Override
        public ItemStack getTabIconItem() {
            return Item.getItemFromBlock(BLOCKS.get(EnumBlockLevel.enableNumber())).getDefaultInstance();
        }
    };
//    private static SimpleNetworkWrapper network;


    public LazyMystical() {
        modInit.add(new ModBlocks());
        modInit.add(new ModRecipe());
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigLoader.preInitConfigLoader(event);

        for (EnumBlockLevel value : EnumBlockLevel.values()) {
            value.setEnable();
        }

        modInit.forEach(IModInit::preInit);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
//        network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInformation.MOD_ID);
//        network.registerMessage(new PacketConsciousness.Handler(), PacketConsciousness.class, 1, Side.CLIENT);

        modInit.forEach(IModInit::init);

        // 注册物品
        NetworkRegistry.INSTANCE.registerGuiHandler(LazyMystical.instance, new GUIHandler());
        GameRegistry.registerTileEntity(TESeedCultivator.class, new ResourceLocation(ModInformation.MOD_ID, "te_seed_cultivator"));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        modInit.forEach(IModInit::postInit);

        modInit = null;
        SeedManager seedManager = new SeedManager();
        new ModJsonRecipe(seedManager::addRecipe);
        seedManager.init();
    }

//    public static SimpleNetworkWrapper getNetwork() {
//        return network;
//    }

}
