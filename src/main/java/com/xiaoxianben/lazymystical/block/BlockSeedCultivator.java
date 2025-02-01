package com.xiaoxianben.lazymystical.block;

import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BlockSeedCultivator extends BlockBase {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    final int level;

    public BlockSeedCultivator(int level) {
        super(Material.METAL, SoundType.METAL);

        this.level = level;

        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    public int getLevel() {
        return this.level;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext blockItemUseContext) {
        return this.defaultBlockState().setValue(FACING, blockItemUseContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.setValue(FACING, mirror.mirror(blockState.getValue(FACING)));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TESeedCultivator();
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onRemove(BlockState oldBlockState, World world, BlockPos pos, BlockState newBlockState, boolean p_196243_5_) {
        if (!oldBlockState.is(newBlockState.getBlock())) {
            TileEntity tileentity = world.getBlockEntity(pos);
            IItemHandler handler = new ItemStackHandler();
            if (tileentity != null) {
                handler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(handler);
            }

            NonNullList<ItemStack> itemStacks = NonNullList.withSize(handler.getSlots() + 1, ItemStack.EMPTY);
            for (int i = 0; i < handler.getSlots() - 1; i++) {
                itemStacks.set(i, handler.getStackInSlot(i));
            }
            itemStacks.set(handler.getSlots(), this.asItem().getDefaultInstance());

            InventoryHelper.dropContents(world, pos, itemStacks);

            world.updateNeighbourForOutputSignal(pos, this);


            super.onRemove(oldBlockState, world, pos, newBlockState, p_196243_5_);
        }

    }

    @ParametersAreNonnullByDefault
    @Nonnull
    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isClientSide) {
            TESeedCultivator te = (TESeedCultivator) getMenuProvider(state, worldIn, pos);
            NetworkHooks.openGui((ServerPlayerEntity) player, te,
                    (PacketBuffer packerBuffer) -> packerBuffer.writeBlockPos(te.getBlockPos()).writeVarIntArray(te.intArray.toIntArray()));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return (TESeedCultivator) world.getBlockEntity(pos);
    }

}
