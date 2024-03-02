package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.util.IHasModel;
import com.xiaoxianben.lazymystical.util.Reference;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Random;

public class BlockTEBasic extends BlockContainer implements IHasModel {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    protected BlockTEBasic(String name, Material materialIn, SoundType soundType, CreativeTabs tab) {
        super(materialIn);
        setUnlocalizedName(Reference.MOD_ID + '.' + name);
        setRegistryName(name);
        setSoundType(soundType);
        setCreativeTab(tab);

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
    public void breakBlock(World world, BlockPos pos, IBlockState state) {

        TESeedCultivator compressor = (TESeedCultivator) world.getTileEntity(pos);

        if (compressor != null) {
            for (int i = 0; i < Objects.requireNonNull(compressor.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)).getSlots(); i++) {
                ItemStack itemstack = Objects.requireNonNull(compressor.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)).getStackInSlot(i);

                if (!itemstack.isEmpty()) {
                    Random rand = new Random();

                    EntityItem entityitem = new EntityItem(world, (float) pos.getX(), (float) pos.getY(), (float) pos.getZ(), new ItemStack(itemstack.getItem(), itemstack.getCount(), itemstack.getItemDamage()));

                    if (itemstack.getTagCompound() != null) {
                        entityitem.getItem().setTagCompound(itemstack.getTagCompound().copy());
                    }

                    float f3 = 0.02F;
                    entityitem.motionX = (float) rand.nextGaussian() * f3;
                    entityitem.motionY = (float) rand.nextGaussian() * f3 + 0.2F;
                    entityitem.motionZ = (float) rand.nextGaussian() * f3;
                    world.spawnEntity(entityitem);
                }
            }
        }
        super.breakBlock(world, pos, state);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()));
    }

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
