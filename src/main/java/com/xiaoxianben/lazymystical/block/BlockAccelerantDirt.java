package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.LazyMystical;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAccelerantDirt extends BlockBasic {

    public final int level;

    public BlockAccelerantDirt(Integer level) {
        super("accelerant_dirt_" + level, Material.GROUND, SoundType.GROUND, LazyMystical.tab);

        setHardness(1F);
        this.level = level;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack stack = playerIn.getHeldItem(hand);
            if (stack.getItem() instanceof ItemHoe) {
                setBlock(stack, playerIn, worldIn, pos, Block.getBlockFromName(LazyMystical.MOD_ID + ":accelerant_farmland_" + level).getDefaultState());
                return true;
            }

            return false;
        }
        return true;
    }

    protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, state, 11);
            stack.damageItem(1, player);
        }
    }

}
