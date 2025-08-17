package com.xiaoxianben.lazymystical.item;

import com.blakebr0.mysticalagriculture.items.ItemMysticalFertilizer;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAccelerantGel extends ItemBase {

    public final int level;

    public ItemAccelerantGel(int level) {
        super("accelerant_gel_" + level);

        this.level = level;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState blockState = worldIn.getBlockState(pos);

        if (ItemMysticalFertilizer.applyFertilizer(player.getHeldItem(hand), worldIn, pos, player, hand)) {
            if (blockState.getBlock() instanceof BlockCrops) {
                int i = this.level - 1;
                ItemStack itemStack = new ItemStack(this, (int) Math.pow(this.level + i, 2));

                Iterable<BlockPos.MutableBlockPos> allInBoxMutable = BlockPos.getAllInBoxMutable(pos.add(i, 0, i), pos.add(-i, 0, -i));
                for (BlockPos.MutableBlockPos blockPos : allInBoxMutable) {
                    ItemMysticalFertilizer.applyFertilizer(itemStack, worldIn, blockPos, player, hand);
                }
            } else {
                for (int i = 0; i < this.level; i++) {
                    ItemMysticalFertilizer.applyFertilizer(new ItemStack(this), worldIn,
                            pos.add(worldIn.rand.nextInt(7)-3, worldIn.rand.nextInt(3)-1, worldIn.rand.nextInt(7)-3),
                            player, hand);
                }
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

}
