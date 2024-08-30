package com.xiaoxianben.lazymystical.init;

import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public enum EnumBlockType {
    Accelerator(BlockAccelerator::new, 0, (param) -> ModRecipe.acceleratorRecipe((Block) param[0], (String) param[1], param[2])),
    SeedCultivator(BlockSeedCultivator::new, 1, (param) -> ModRecipe.SeedCultivatorRecipe((Block) param[0], (String) param[1], (Block) param[2]));

    private final int index;
    private final List<Block> blocks = new ArrayList<>();
    private final Consumer<Object[]> recipeConsumer;
    private final Function<Integer, ? extends Block> fun;


    EnumBlockType(Function<Integer, ? extends Block> fun, int index, Consumer<Object[]> recipeConsumer) {
        this.fun = fun;
        this.index = index;
        this.recipeConsumer = recipeConsumer;
    }

    public Block create(int level) {
        Block block = fun.apply(level);
        blocks.add(block);
        return block;
    }

    public void addRecipe(int outputBlockIndex, Object... param) {
        Object[] p1 = new Object[param.length + 1];
        p1[0] = blocks.get(outputBlockIndex);
        System.arraycopy(param, 0, p1, 1, param.length);
        this.recipeConsumer.accept(p1);
    }

    public Block getBlock(int index) {
        return this.blocks.get(index);
    }
    
    public int getIndex() {
        return index;
    }

    public static EnumBlockType getType(int index) {
        return values()[index];
    }
}