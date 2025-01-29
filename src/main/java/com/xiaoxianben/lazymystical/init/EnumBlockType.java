package com.xiaoxianben.lazymystical.init;

import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.init.register.EnumModRegister;
import net.minecraft.block.Block;

import java.util.Collection;
import java.util.EnumMap;
import java.util.function.Function;

public enum EnumBlockType {
    Accelerator(BlockAccelerator::new),
    SeedCultivator(BlockSeedCultivator::new);

//    private final int index;
    private final EnumMap<EnumModRegister, Block[]> mapBlocks = new EnumMap<>(EnumModRegister.class);

    private final Function<Integer, ? extends Block> fun;


    EnumBlockType(Function<Integer, ? extends Block> fun) {
        this.fun = fun;
    }

    public Block create(int level) {
        return fun.apply(level);
    }

    public void addBlocks(EnumModRegister register, Block[] blocks) {
        mapBlocks.put(register, blocks);
    }

    public Block[] getBlocks(EnumModRegister register) {
        return this.mapBlocks.get(register);
    }
    public Collection<Block[]> getAllBlocks() {
        return this.mapBlocks.values();
    }
}