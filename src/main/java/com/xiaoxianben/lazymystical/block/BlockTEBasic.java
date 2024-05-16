package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.api.IHasModel;
import com.xiaoxianben.lazymystical.util.ModInformation;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BlockTEBasic extends BlockContainer implements IHasModel {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;


    protected BlockTEBasic(String name, Material materialIn, SoundType soundType, CreativeTabs tab) {
        super(materialIn);
        setUnlocalizedName(ModInformation.MOD_ID + '-' + name);
        setRegistryName(name);
        setSoundType(soundType);
        setCreativeTab(tab);

        this.setHardness(10.0F);
        this.setHarvestLevel("pickaxe", 1);

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

        Main.BLOCKS.add(this);
        Main.ITEMS.add(new ItemBlock(this).setRegistryName(name));
    }


    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @ParametersAreNonnullByDefault
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
