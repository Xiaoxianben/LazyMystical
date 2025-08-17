package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.api.IHasItemBlock;
import com.xiaoxianben.lazymystical.api.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockBasic extends Block implements IHasModel, IHasItemBlock {

    public BlockBasic(String name, Material materialIn, SoundType soundType, CreativeTabs tab) {
        super(materialIn);
        setUnlocalizedName(LazyMystical.MOD_ID + '-' + name);
        setRegistryName(name);
        setCreativeTab(tab);

        this.setSoundType(soundType);

        this.setHardness(5.0F);
        this.setHarvestLevel("pickaxe", 1);
    }


    @Override
    public void registerModels() {
        LazyMystical.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

}
