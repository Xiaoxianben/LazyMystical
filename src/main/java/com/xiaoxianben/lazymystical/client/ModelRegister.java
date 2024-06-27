package com.xiaoxianben.lazymystical.client;

import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.api.IHasModel;
import com.xiaoxianben.lazymystical.util.ModInformation;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(
        modid = ModInformation.MOD_ID,
        value = {Side.CLIENT}
)
public class ModelRegister {

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {

        for (Item item : Main.ITEMS) {
            if (item instanceof IHasModel) {
                ((IHasModel) item).registerModels();
            }
        }

        for (Block block : Main.BLOCKS) {
            if (block instanceof IHasModel) {
                ((IHasModel) block).registerModels();
            }
        }

    }

}
