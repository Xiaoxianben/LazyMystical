package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.GUI.GUIHandler;
import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

public class BlockSeedCultivator extends BlockTEBasic implements ITileEntityProvider {

    public int level;

    public BlockSeedCultivator(String name, int level) {
        super(name, Material.IRON, SoundType.METAL, Main.tab);
        this.level = level;
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()) {
                NBTTagCompound compound = Objects.requireNonNull(worldIn.getTileEntity(pos)).writeToNBT(new NBTTagCompound());
                playerIn.sendMessage(new TextComponentString(compound.toString()));
            }
            int id = GUIHandler.BlockGUI;
            playerIn.openGui(Main.instance, id, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @ParametersAreNonnullByDefault
    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @ParametersAreNonnullByDefault
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        // 获取TileEntity
        TESeedCultivator compressor = (TESeedCultivator) world.getTileEntity(pos);

        if (compressor != null) {
            for (int i = 0; i < Objects.requireNonNull(compressor.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)).getSlots(); i++) {
                ItemStack itemstack = Objects.requireNonNull(compressor.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)).getStackInSlot(i);

                if (!itemstack.isEmpty()) {
                    ItemStack dropItem = itemstack.copy();

                    EntityItem entityitem = new EntityItem(
                            world,
                            (float) pos.getX(),
                            (float) pos.getY(),
                            (float) pos.getZ(),
                            dropItem
                    );

                    world.spawnEntity(entityitem);
                }
            }
        }

        super.breakBlock(world, pos, state);
    }

    @ParametersAreNonnullByDefault
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @ParametersAreNonnullByDefault
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TESeedCultivator(this.level);
    }
}
