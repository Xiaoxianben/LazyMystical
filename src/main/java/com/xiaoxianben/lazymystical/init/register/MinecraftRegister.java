package com.xiaoxianben.lazymystical.init.register;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.init.ModRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

import static com.blakebr0.mysticalagriculture.blocks.ModBlocks.blockMysticalMachineFrame;

public class MinecraftRegister implements IModRegister {

    public final int[] levels = new int[]{1, 2, 3, 4, 5};
    public final String[] oreBlocks = new String[]{"blockInferium", "blockPrudentium", "blockIntermedium", "blockSuperium", "blockSupremium"};
    public final EnumModRegister selfRegister = EnumModRegister.MINECRAFT;

    @Override
    public void preInit() {
        for (EnumBlockType blockType : EnumBlockType.values()) {
            List<Block> blocks = new ArrayList<>();
            for (int level : levels) {
                blocks.add(blockType.create(level));
            }
            blockType.addBlocks(selfRegister, blocks.toArray(new Block[0]));
        }
    }

    @Override
    public void init() {

        int iB = 0;
        Block[] blocks = EnumBlockType.Accelerator.getBlocks(selfRegister);
        GameRegistry.addShapelessRecipe(
                new ResourceLocation(blocks[0].getRegistryName().toString() + "_2"),
                new ResourceLocation(LazyMystical.MOD_ID, "accelerator1"),
                new ItemStack(Item.getItemFromBlock(blocks[0])),
                CraftingHelper.getIngredient(com.blakebr0.mysticalagriculture.blocks.ModBlocks.blockGrowthAccelerator)
        );
        for (Block block : blocks) {
            ModRecipe.instance.acceleratorRecipe(block, oreBlocks[iB], iB == 0 ? "gemDiamond" : blocks[iB - 1]);
            ++iB;
        }
        iB = 0;
        blocks = EnumBlockType.SeedCultivator.getBlocks(selfRegister);
        for (Block block : blocks) {
            ModRecipe.instance.SeedCultivatorRecipe(block, oreBlocks[iB], iB == 0 ? blockMysticalMachineFrame : blocks[iB - 1]);
            ++iB;
        }
    }

    @Override
    public void posInit() {

    }
}
