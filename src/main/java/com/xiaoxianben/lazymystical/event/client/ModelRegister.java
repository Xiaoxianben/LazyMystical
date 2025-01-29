package com.xiaoxianben.lazymystical.event.client;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.api.IHasModel;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import net.minecraft.block.Block;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(
        modid = LazyMystical.MOD_ID,
        value = {Side.CLIENT}
)
public class ModelRegister {

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {

        for (EnumBlockType enumBlockType : EnumBlockType.values()) {
            for (Block[] blocks : enumBlockType.getAllBlocks()) {
                for (Block block : blocks) {
                    if (block instanceof IHasModel) {
                        ((IHasModel) block).registerModels();
                    }
                }
            }
        }

    }

}
