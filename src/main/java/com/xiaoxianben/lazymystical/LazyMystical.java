package com.xiaoxianben.lazymystical;

import com.xiaoxianben.lazymystical.config.ConfigLoader;
import com.xiaoxianben.lazymystical.gui.GUIHandler;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.init.ModRecipe;
import com.xiaoxianben.lazymystical.init.register.EnumModRegister;
import com.xiaoxianben.lazymystical.jsonRecipe.ModJsonRecipe;
import com.xiaoxianben.lazymystical.manager.SeedManager;
import com.xiaoxianben.lazymystical.proxy.CommonProxy;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
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

@Mod(
        modid = LazyMystical.MOD_ID,
        name = LazyMystical.NAME,
        version = LazyMystical.VERSION,
        dependencies = "required-after:mysticalagriculture;after:mysticalagradditions;after:jei"
)
public class LazyMystical {

    public static final String NAME = "Lazy Mystical";
    public static final String VERSION = "1.4.0";
    public static final String MOD_ID = "lazymystical";
    @Mod.Instance
    public static LazyMystical instance;
    @SidedProxy(clientSide = "com.xiaoxianben.lazymystical.proxy.ClientProxy", serverSide = "com.xiaoxianben.lazymystical.proxy.CommonProxy")
    public static CommonProxy proxy;
    public static final CreativeTabs tab = new CreativeTabs(MOD_ID) {
        @Nonnull
        @Override
        public ItemStack getTabIconItem() {
            return Item.getItemFromBlock(EnumBlockType.SeedCultivator.getBlocks(EnumModRegister.MINECRAFT)[4]).getDefaultInstance();
        }
    };
//    private static SimpleNetworkWrapper network;


    public LazyMystical() {
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        new ConfigLoader(event).load();

        for (EnumModRegister modRegister : EnumModRegister.values()) {
            modRegister.setEnable();
            modRegister.preInit();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
//        network = NetworkRegistry.INSTANCE.newSimpleChannel(ModInformation.MOD_ID);
//        network.registerMessage(new PacketConsciousness.Handler(), PacketConsciousness.class, 1, Side.CLIENT);

        ModRecipe.instance = new ModRecipe();
        for (EnumModRegister modRegister : EnumModRegister.values()) {
            modRegister.init();
        }

        SeedManager seedManager = new SeedManager();
        new ModJsonRecipe(seedManager::addRecipe);
        seedManager.init();

        // 注册物品
        NetworkRegistry.INSTANCE.registerGuiHandler(LazyMystical.instance, new GUIHandler());
        GameRegistry.registerTileEntity(TESeedCultivator.class, new ResourceLocation(MOD_ID, "te_seed_cultivator"));
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        for (EnumModRegister modRegister : EnumModRegister.values()) {
            modRegister.posInit();
        }

        ModRecipe.instance = null;

    }

//    public static SimpleNetworkWrapper getNetwork() {
//        return network;
//    }

}
