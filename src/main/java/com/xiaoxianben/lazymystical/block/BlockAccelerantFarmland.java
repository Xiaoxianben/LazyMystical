package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.api.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockAccelerantFarmland extends BlockFarmland implements IHasModel {

    protected final int level;

    public BlockAccelerantFarmland(int level) {
        super();

        setRegistryName("accelerant_farmland_" + level);
        setUnlocalizedName(LazyMystical.MOD_ID + '-' + "accelerant_farmland_" + level);
        setCreativeTab(LazyMystical.tab);
        setSoundType(SoundType.GROUND);
        setHardness(1F);
        this.setLightOpacity(0);
        this.setTickRandomly(false);

        this.level = level;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(MOISTURE) < 7) {
            worldIn.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
        }

        BlockAccelerator.growCropsNearby(worldIn, pos, state, this.level, 1);
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        entityIn.fall(fallDistance, 1.0F);
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getValue(MOISTURE) < 7) {
            worldIn.setBlockState(pos, state.withProperty(MOISTURE, 7), 2);
        }
        BlockAccelerator.worldUpdate(worldIn, pos, state);
    }

    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable) {
        return true;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Block.getBlockFromName(LazyMystical.MOD_ID + ":accelerant_dirt_" + level).getItemDropped(state, rand, fortune);
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        switch (side) {
            case UP:
                return true;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                BlockPos offsetPos = pos.offset(side);
                IBlockState iblockstate = blockAccess.getBlockState(offsetPos);
                AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(blockAccess, offsetPos);

                return !iblockstate.isOpaqueCube() && (iblockstate.getMaterial() == Material.AIR || axisalignedbb.minY > 0 || axisalignedbb.maxY < FARMLAND_AABB.maxY);
            default:
                return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (face == EnumFacing.DOWN) {
            return true;
        }
        return super.doesSideBlockRendering(state, world, pos, face);
    }

    @Override
    public boolean isFertile(World world, BlockPos pos) {
        return true;
    }

    @Override
    public void registerModels() {
        LazyMystical.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
