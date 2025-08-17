package com.xiaoxianben.lazymystical;

import com.xiaoxianben.lazymystical.config.ConfigLoader;
import com.xiaoxianben.lazymystical.gui.GUIHandler;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.init.register.EnumModRegister;
import com.xiaoxianben.lazymystical.jsonRecipe.ModJsonRecipe;
import com.xiaoxianben.lazymystical.manager.seedManagr.SeedManager;
import com.xiaoxianben.lazymystical.network.MessageGui;
import com.xiaoxianben.lazymystical.proxy.CommonProxy;
import com.xiaoxianben.lazymystical.tileEntity.TEAccelerantFluid;
import com.xiaoxianben.lazymystical.tileEntity.TEGeneratorHandCrank;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
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

@Mod(
        modid = LazyMystical.MOD_ID,
        name = LazyMystical.NAME,
        version = LazyMystical.VERSION,
        dependencies = "required-after:mysticalagriculture;after:mysticalagradditions;after:jei"
)
public class LazyMystical {

    public static final String NAME = "Lazy Mystical";
    public static final String VERSION = "1.5.0";
    public static final String MOD_ID = "lazymystical";
    public static final CreativeTabs tab = new LazyMysticalTap();

    @Mod.Instance
    public static LazyMystical instance;
    @SidedProxy(clientSide = "com.xiaoxianben.lazymystical.proxy.ClientProxy", serverSide = "com.xiaoxianben.lazymystical.proxy.CommonProxy")
    public static CommonProxy proxy;


    public static SimpleNetworkWrapper getChannel() {
        return instance.channel;
    }


    protected final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(LazyMystical.MOD_ID);


    public LazyMystical() {
        FluidRegistry.enableUniversalBucket();
    }


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        new ConfigLoader(event).load();

        proxy.preInit();

        channel.registerMessage(MessageGui.MessageGuiHandler.class, MessageGui.class, 0, Side.SERVER);

        for (EnumModRegister modRegister : EnumModRegister.values()) {
            modRegister.setEnable();
            modRegister.preInit();
        }

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        for (EnumModRegister modRegister : EnumModRegister.values()) {
            modRegister.init();
        }

        ModJsonRecipe.init();
        SeedManager.init();

        // 注册物品
        NetworkRegistry.INSTANCE.registerGuiHandler(LazyMystical.instance, new GUIHandler());

        GameRegistry.registerTileEntity(TESeedCultivator.class, new ResourceLocation(MOD_ID, "te_seed_cultivator"));
        GameRegistry.registerTileEntity(TEAccelerantFluid.class, new ResourceLocation(MOD_ID, "te_accelerant_fluid"));
        GameRegistry.registerTileEntity(TEGeneratorHandCrank.class, new ResourceLocation(MOD_ID, "te_generator_hand_crank"));

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        for (EnumModRegister modRegister : EnumModRegister.values()) {
            modRegister.posInit();
        }

    }


    static class LazyMysticalTap extends CreativeTabs {

        public LazyMysticalTap() {
            super(LazyMystical.MOD_ID);
        }

        @Nonnull
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Item.getItemFromBlock(EnumBlockType.SeedCultivator.getBlocks(EnumModRegister.MINECRAFT)[0]));
        }
    }
}
