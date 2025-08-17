package com.xiaoxianben.lazymystical.event.client;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.api.IHasModel;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.init.EnumItemType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
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

        for (Block[] blockFluids : EnumBlockType.BlockFluid.getAllBlocks()) {
            for (Block blockFluid : blockFluids) {
                ModelLoader.setCustomStateMapper(blockFluid, new StateMapperBase() {
                    @Override
                    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                        return new ModelResourceLocation(new ResourceLocation(LazyMystical.MOD_ID, "fluids"), blockFluid.getRegistryName().getResourcePath());
                    }
                });
            }
        }

        for (EnumBlockType enumBlockType : EnumBlockType.values()) {
            for (Block[] blocks : enumBlockType.getAllBlocks()) {
                for (Block block : blocks) {
                    if (block instanceof IHasModel) {
                        ((IHasModel) block).registerModels();
                    }
                }
            }
        }

        for (EnumItemType enumItemType : EnumItemType.values()) {
            for (Item[] items : enumItemType.getAllItems()) {
                for (Item item : items) {
                    if (item instanceof IHasModel) {
                        ((IHasModel) item).registerModels();
                    }
                }
            }
        }

    }

}
