package com.xiaoxianben.lazymystical.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
    }

    public void registerItemRenderer(Item item, int meta, String id) {

        if (item.getRegistryName() != null) {
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
        }
    }

}
