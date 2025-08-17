package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.tileEntity.TEGeneratorHandCrank;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHandCrankGenerator extends BlockTEBasic {


    public BlockHandCrankGenerator() {
        super("hand_crank_generator", Material.IRON, SoundType.METAL, LazyMystical.tab);
    }


    @Override
    public boolean onBlockActivatedOnSlave(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null && facing == state.getValue(FACING)) {
            ((TEGeneratorHandCrank) tileEntity).generatedOnceEnergy();
            return true;
        }
        return false;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if (worldIn.isRemote) {
            return;
        }
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null) {
            ((TEGeneratorHandCrank) tileEntity).generatedOnceEnergy();
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TEGeneratorHandCrank();
    }
}
