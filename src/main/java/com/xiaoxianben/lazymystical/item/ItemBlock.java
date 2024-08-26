package com.xiaoxianben.lazymystical.item;

import com.xiaoxianben.lazymystical.LazyMystical;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class ItemBlock extends BlockItem {
    public ItemBlock(Block block) {
        super(block, new Properties().tab(LazyMystical.GROUP));
    }
}
