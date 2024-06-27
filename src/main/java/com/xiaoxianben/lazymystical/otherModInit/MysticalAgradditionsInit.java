package com.xiaoxianben.lazymystical.otherModInit;

import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.init.ModBlocks;
import com.xiaoxianben.lazymystical.init.ModRecipe;

public class MysticalAgradditionsInit implements IOtherModInit {

    public static BlockAccelerator MAdAccelerators;
    public static BlockSeedCultivator MAdSeedCultivators;


    @Override
    public void initBlocks() {

        MAdAccelerators = new BlockAccelerator(6);
        MAdSeedCultivators = new BlockSeedCultivator(6);

    }

    @Override
    public void initItems() {

    }

    @Override
    public void initOre() {

    }

    @Override
    public void initRecipes() {

        ModRecipe.acceleratorRecipe(MAdAccelerators, "blockInsanium", ModBlocks.accelerators[4]);
        ModRecipe.SeedCultivatorRecipe(MAdSeedCultivators, "blockInsanium", ModBlocks.seedCultivators[4]);

    }
}
