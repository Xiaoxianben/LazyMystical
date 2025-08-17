package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.api.IHasHandlerComponent;
import com.xiaoxianben.lazymystical.config.ConfigLoader;
import com.xiaoxianben.lazymystical.item.compomemt.ItemComponent;
import com.xiaoxianben.lazymystical.manager.ItemStackManager;
import com.xiaoxianben.lazymystical.tileEntity.itemHandler.ItemHandlerComponent;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class BlockTEBasic extends BlockBasic implements ITileEntityProvider {


    public static final PropertyDirection FACING = BlockHorizontal.FACING;


    protected BlockTEBasic(String name, Material materialIn, SoundType soundType, CreativeTabs tab) {
        super(name, materialIn, soundType, tab);

        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

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

    @ParametersAreNonnullByDefault
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity == null) {
                ConfigLoader.logger.error("TileEntity is null, pos: {}, this isn't true", pos);
                return true;
            }
            ItemStack heldItemStack = playerIn.getHeldItem(hand);
            Item handItem = heldItemStack.getItem();
            boolean flag = false;

            if (playerIn.isCreative() && playerIn.isSneaking() && heldItemStack.isEmpty()) {
                NBTTagCompound compound = tileEntity.writeToNBT(new NBTTagCompound());
                playerIn.sendMessage(new TextComponentString(compound.toString()));

            } else if (handItem instanceof ItemComponent && tileEntity instanceof IHasHandlerComponent) {
                flag = ItemStackManager.tryInsertComponent(playerIn, heldItemStack, (IHasHandlerComponent) tileEntity);
            }

            IFluidHandler fluidHandler = tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
            flag = flag || (fluidHandler != null && FluidUtil.interactWithFluidHandler(playerIn, hand, fluidHandler));

            if (!flag) {
                return onBlockActivatedOnSlave(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
            }
            return true;
        }
        return true;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity != null && tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {

            IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

            ItemStackManager.spawnItemHandler(itemHandler, worldIn, pos);
        }
        if (tileEntity instanceof IHasHandlerComponent) {
            ItemHandlerComponent handlerComponent = ((IHasHandlerComponent) tileEntity).getHandlerComponent();
            ItemStackManager.spawnItemHandler(handlerComponent, worldIn, pos);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @ParametersAreNonnullByDefault
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }


    public abstract boolean onBlockActivatedOnSlave(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);
}
