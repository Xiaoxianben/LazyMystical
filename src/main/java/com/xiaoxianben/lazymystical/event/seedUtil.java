package com.xiaoxianben.lazymystical.event;

import com.blakebr0.mysticalagriculture.crafting.ReprocessorManager;
import com.blakebr0.mysticalagriculture.items.ItemSeed;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Objects;

public class seedUtil {
    @Nullable
    public static Item getCrop(Item seed, World world, BlockPos pos) {
        if (seed instanceof ItemSeed) {
            return ReprocessorManager.getOutput(seed.getDefaultInstance()).getItem();
        }

        // TODO: 1.12.2 判断是否为作物
        Block blockCrop = Block.getBlockFromItem(seed);

        // 判断是否为作物
        if (seed instanceof IPlantable) {
            blockCrop = ((IPlantable) seed).getPlant(world, pos).getBlock();
        } else if (seed instanceof ItemBlockSpecial && (((ItemBlockSpecial) seed).getBlock() instanceof IPlantable || ((ItemBlockSpecial) seed).getBlock() instanceof IGrowable)) {
            blockCrop = ((ItemBlockSpecial) seed).getBlock();
        } else if (!(blockCrop instanceof IPlantable) && !(blockCrop instanceof IGrowable)) {
            blockCrop = null;
        }

        if (blockCrop == null) {
            return null;
        }

        // 处理
        if (blockCrop.getBlockState().getProperty("age") != null) {
            IBlockState blockState = blockCrop.getDefaultState();
            blockState = blockState.withProperty((PropertyInteger) Objects.requireNonNull(blockCrop.getBlockState().getProperty("age")), (Objects.requireNonNull(blockCrop.getBlockState().getProperty("age"))).getAllowedValues().size() - 1);

            return blockCrop.getItemDropped(blockState, world.rand, 1);
        }

        if (blockCrop instanceof IPlantable) {
            IPlantable block = ((IPlantable) blockCrop);
            return block.getPlant(world, pos).getBlock().getItemDropped(blockCrop.getDefaultState(), world.rand, 0);
        }

        if (blockCrop instanceof IGrowable) {
            return blockCrop.getItemDropped(blockCrop.getDefaultState(), world.rand, 1);
        }

        return null;
    }
}
