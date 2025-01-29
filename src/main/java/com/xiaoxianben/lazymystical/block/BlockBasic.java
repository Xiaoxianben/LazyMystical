package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.api.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BlockBasic extends Block implements IHasModel {

    private int level;

    public BlockBasic(String name, Material materialIn, SoundType soundType, CreativeTabs tab) {
        super(materialIn);
        setUnlocalizedName(LazyMystical.MOD_ID + '-' + name);
        setRegistryName(name);
        setCreativeTab(tab);

        this.setSoundType(soundType);

        this.setHardness(10.0F);
        this.setHarvestLevel("pickaxe", 1);

//        LazyMystical.BLOCKS.add(this);
//        LazyMystical.ITEMS.add(new ItemBlock(this).setRegistryName(name));
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level <= 0) throw new RuntimeException("level 超出范围, RegistryName: " + this.getRegistryName());
        this.level = level;
    }

    @Override
    public void registerModels() {
        LazyMystical.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

}
