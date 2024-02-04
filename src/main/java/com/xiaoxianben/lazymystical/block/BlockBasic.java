package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.util.IHasModel;
import com.xiaoxianben.lazymystical.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBasic extends Block implements IHasModel {
    public BlockBasic(String name, Material materialIn, CreativeTabs tab) {
        this(name, materialIn, tab, 64);
    }

    public BlockBasic(String name, Material materialIn, CreativeTabs tab, int maxStackSize) {
        super(materialIn);
        setUnlocalizedName(Reference.MOD_ID + '.' + name);
        setRegistryName(name);
        setCreativeTab(tab);


        Main.BLOCKS.add(this);
        Main.ITEMS.add(new ItemBlock(this).setRegistryName(name).setMaxStackSize(maxStackSize));
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

}
