package com.xiaoxianben.lazymystical.date;

import com.blakebr0.mysticalagriculture.block.GrowthAcceleratorBlock;
import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.registry.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockAcceleratorStateProvider extends BlockStateProvider {
    public BlockAcceleratorStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, LazyMystical.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        final String name = "block/growth_accelerator_6";
        final ResourceLocation texture = this.modLoc("blocks/accelerator/6");

        GrowthAcceleratorBlock block = BlockRegistry.GROWTH_ACCELERATOR_6.get();

        VariantBlockStateBuilder blockStateBuilder = this.getVariantBuilder(block);
        this.models().cubeAll(name, texture);
        this.simpleBlockItem(block, this.models().getExistingFile(this.modLoc(name)));
        blockStateBuilder.forAllStates(blockState -> ConfiguredModel.builder().modelFile(this.models().getExistingFile(this.modLoc(name))).build());

    }
}
