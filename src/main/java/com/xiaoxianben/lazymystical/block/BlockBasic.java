package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.api.IHasModel;
import com.xiaoxianben.lazymystical.util.ModInformation;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.LinkedHashSet;

public class BlockBasic extends Block implements IHasModel {

    public BlockBasic(String name, Material materialIn, SoundType soundType, CreativeTabs tab, LinkedHashSet<Block> linkedHashSet) {
        super(materialIn);
        setUnlocalizedName(ModInformation.MOD_ID + '-' + name);
        setRegistryName(name);
        setCreativeTab(tab);

        this.setSoundType(soundType);

        this.setHardness(10.0F);
        this.setHarvestLevel("pickaxe", 1);

        linkedHashSet.add(this);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

}
