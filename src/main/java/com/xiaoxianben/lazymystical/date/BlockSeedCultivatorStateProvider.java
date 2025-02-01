package com.xiaoxianben.lazymystical.date;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.registry.BlockRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockSeedCultivatorStateProvider extends BlockStateProvider {
    public BlockSeedCultivatorStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, LazyMystical.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (int i = 0; i < BlockRegistry.SEED_CULTIVATOR.size(); i++) {
            final String name = "block/seed_cultivator_" + (i + 1);
            Block block = BlockRegistry.SEED_CULTIVATOR.get(i).get();
            final ResourceLocation texture = this.modLoc("blocks/seed_cultivator/" + (i + 1));
            final ResourceLocation textureSide = this.modLoc("blocks/seed_cultivator/" + (i + 1) + "_side");

            VariantBlockStateBuilder blockStateBuilder = this.getVariantBuilder(block);
            this.models().orientable(name, textureSide, texture, textureSide);
            this.simpleBlockItem(block, this.models().getExistingFile(this.modLoc(name)));

            blockStateBuilder.forAllStates(state -> {

                ConfiguredModel.Builder<?> configuredModel = ConfiguredModel.builder().modelFile(this.models().getExistingFile(this.modLoc(name)));

                switch (state.getValue(BlockSeedCultivator.FACING)) {
                    case SOUTH:
                        return configuredModel.rotationY(180).build();
                    case EAST:
                        return configuredModel.rotationY(90).build();
                    case WEST:
                        return configuredModel.rotationY(270).build();
                    default:
                        return configuredModel.build();
                }

            });
        }
    }

}
