package com.xiaoxianben.lazymystical.block.machine;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.api.ICanOpenGui;
import com.xiaoxianben.lazymystical.block.BlockTEBasic;
import com.xiaoxianben.lazymystical.tileEntity.TEBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockMachineBase extends BlockTEBasic implements ICanOpenGui {


    public final int level;


    protected BlockMachineBase(String name, int level) {
        super(name, Material.IRON, SoundType.METAL, LazyMystical.tab);
        this.level = level;
    }


    @Override
    public boolean onBlockActivatedOnSlave(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        openGui(worldIn, pos, playerIn);

        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

        if (worldIn.isRemote) return;

        TEBase tileEntity = (TEBase) worldIn.getTileEntity(pos);
        if (tileEntity == null) return;

        int gettingPowered = worldIn.isBlockIndirectlyGettingPowered(pos);
        tileEntity.isActive = gettingPowered == 0;
    }
}
