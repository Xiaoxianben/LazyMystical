package com.xiaoxianben.lazymystical.block;

import com.blakebr0.mysticalagriculture.config.ModConfig;
import com.blakebr0.mysticalagriculture.lib.Tooltips;
import com.xiaoxianben.lazymystical.Main;
import com.xiaoxianben.lazymystical.event.ConfigLoader;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

public class BlockAccelerator extends BlockBasic {

    private final int level;

    public BlockAccelerator(int level) {
        super("accelerator" + level, Material.ROCK, Main.tab, 64);
        this.setSoundType(SoundType.STONE);
        this.setHardness(5.0F);
        this.setResistance(8.0F);
        this.setTickRandomly(false);

        this.level = level;
    }

    @ParametersAreNonnullByDefault
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(Tooltips.GROWTH_ACCELERATOR);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        world.scheduleBlockUpdate(pos, state.getBlock(), 1, 1);
        super.onBlockAdded(world, pos, state);
    }

    /**
     * 方在世界更新时，启动块更新
     */
    @ParametersAreNonnullByDefault
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote) {
            this.growCropsNearby(world, pos, state);
        }
    }

    private void growCropsNearby(World world, BlockPos pos, IBlockState state) {
        Iterable<BlockPos> blocks = BlockPos.getAllInBox(pos.add(0, 1, 0), pos.add(0, 12 * this.level, 0));
        if (!blocks.iterator().hasNext() || !isEnabled()) {
            world.scheduleBlockUpdate(pos, state.getBlock(), 1, 1);
        } else {
            for (BlockPos aoePos : blocks) {
                IBlockState cropState = world.getBlockState(aoePos);
                Block cropBlock = cropState.getBlock();

                if (cropBlock instanceof IGrowable || cropBlock instanceof IPlantable) {
                    for (int i = 0; i < this.getLevel(); i++) {
                        cropBlock.randomTick(world, aoePos, cropState, world.rand);
                    }
                    break;
                }
            }
            worldUpdate(world, pos, state);
        }
    }

    private void worldUpdate(World world, BlockPos pos, IBlockState state) {
        int time = (int) (ConfigLoader.acceleratorSpeed * 20 * ((world.rand.nextInt(3) + 9) / 10.0F) * ((world.rand.nextInt(3) + 9) / 10.0F));
//        System.out.println("Growth Accelerator: " + time);
        world.scheduleBlockUpdate(pos, state.getBlock(), time, 1);
    }

    public int getLevel() {
        return this.level;
    }

    public boolean isEnabled() {
        return ModConfig.confGrowthAccelerator;
    }
}
