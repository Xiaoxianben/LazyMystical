package com.xiaoxianben.lazymystical.date;

import com.xiaoxianben.lazymystical.LazyMystical;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockItemStateSimple extends BlockStateProvider {
    public BlockItemStateSimple(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, LazyMystical.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
