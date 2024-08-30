package com.xiaoxianben.lazymystical.init;

import com.xiaoxianben.lazymystical.api.IModInit;
import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.util.ModInformation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks implements IModInit {

    public void preInit() {

        for (EnumBlockType blockType : EnumBlockType.values()) {
            for (EnumBlockLevel blockLevel : EnumBlockLevel.values()) {
                blockType.create(blockLevel.getLevel());
            }
        }

    }

    public void init() {

        GameRegistry.addShapelessRecipe(
                new ResourceLocation(getAccelerator(0).getRegistryName().toString() + "_2"),
                new ResourceLocation(ModInformation.MOD_ID, "accelerator1"),
                Item.getItemFromBlock(getAccelerator(0)).getDefaultInstance(),
                CraftingHelper.getIngredient(com.blakebr0.mysticalagriculture.blocks.ModBlocks.blockGrowthAccelerator)
        );

        String[] strings = {"blockInferium", "blockPrudentium", "blockIntermedium", "blockSuperium", "blockSupremium", "blockInsanium"};

        for (EnumBlockType blockType : EnumBlockType.values()) {
            for (EnumBlockLevel blockLevel : EnumBlockLevel.values()) {
                int i = blockLevel.getLevel() - 1;
                if (i == 0) {
                    blockType.addRecipe(i, strings[i], new Object[]{"gemDiamond", com.blakebr0.mysticalagriculture.blocks.ModBlocks.blockMysticalMachineFrame}[blockType.getIndex()]);
                } else {
                    blockType.addRecipe(i, strings[i], blockType == EnumBlockType.SeedCultivator ? getSeedCultivator(i - 1) : getAccelerator(i - 1));
                }
            }
        }


    }

    @Override
    public void postInit() {
    }

    public BlockAccelerator getAccelerator(int in) {
        return (BlockAccelerator) EnumBlockType.Accelerator.getBlock(in);
    }

    public BlockSeedCultivator getSeedCultivator(int in) {
        return (BlockSeedCultivator) EnumBlockType.SeedCultivator.getBlock(in);
    }

}
