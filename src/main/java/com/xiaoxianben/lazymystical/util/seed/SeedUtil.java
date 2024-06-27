package com.xiaoxianben.lazymystical.util.seed;

import com.blakebr0.mysticalagriculture.blocks.crop.BlockInferiumCrop;
import com.blakebr0.mysticalagriculture.crafting.ReprocessorManager;
import com.blakebr0.mysticalagriculture.items.ItemSeed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistry;

import javax.annotation.Nullable;
import java.util.*;

public class SeedUtil {


    /**
     * 用于处理附属的模组
     */
    public static AgradditionsModUtil agradditionsModUtil = null;
    /**
     * 字典：用于获取种子对应的作物
     */
    protected static LinkedHashMap<Item, ItemStack> seedTOCrop = new LinkedHashMap<>();


    public static void init() {
        if (Loader.isModLoaded("mysticalagradditions")) {
            agradditionsModUtil = new AgradditionsModUtil(new HashMap<>());
        }

        ForgeRegistry<Item> registry = (ForgeRegistry<Item>) GameRegistry.findRegistry(Item.class);

        registry.getValuesCollection().stream()
                .filter(item -> getCropPrivate(item) != null)
                .sorted(Comparator.comparingInt(registry::getID))
                .forEach(seed -> {
                    seedTOCrop.put(seed, getCropPrivate(seed));
                    if (agradditionsModUtil != null) {
                        agradditionsModUtil.init(seed);
                    }
                });

    }


    @Nullable
    private static ItemStack getCropPrivate(Item seed) {
        try {
            Item crop = getCropPrivate(seed, new BlockPos(0, 0, 0));
            if (crop == Items.AIR || crop == null) {
                return null;
            }
            return new ItemStack(crop, getCropCount(seed));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isModSeed(Item seed) {
        return !ReprocessorManager.getOutput(seed.getDefaultInstance()).isEmpty();
    }

    @Nullable
    private static Item getCropPrivate(Item seed, BlockPos pos) {
        if (isModSeed(seed)) {
            return ReprocessorManager.getOutput(seed.getDefaultInstance()).getItem();
        }

        Block blockCrop = Block.getBlockFromItem(seed);
        if (seed instanceof IPlantable) {
            blockCrop = ((IPlantable) seed).getPlant(null, pos).getBlock();
        } else if (seed instanceof ItemBlockSpecial) {
            blockCrop = ((ItemBlockSpecial) seed).getBlock();
        } else if (!(blockCrop instanceof IPlantable || blockCrop instanceof IGrowable)) {
            blockCrop = null;
        }

        if (blockCrop == null || blockCrop instanceof BlockStem) {
            return null;
        }

        if (blockCrop instanceof BlockCrops) {
            return blockCrop.getItemDropped(((BlockCrops) blockCrop).withAge(((BlockCrops) blockCrop).getMaxAge()), new Random(), 1);
        }

        IProperty<?> ageProperty = blockCrop.getBlockState().getProperty("age");
        if (ageProperty != null) {
            IBlockState blockState = blockCrop.getDefaultState();
            PropertyInteger agePropertyInt = (PropertyInteger) ageProperty;
            blockState = blockState.withProperty(agePropertyInt, agePropertyInt.getAllowedValues().size() - 1);
            return blockCrop.getItemDropped(blockState, new Random(), 1);
        }

        return null;
    }


    @Nullable
    public static ItemStack getCrop(Item seed) {
        return seedTOCrop.get(seed);
    }

    public static Set<Item> getSeeds() {
        return seedTOCrop.keySet();
    }

    public static HashMap<Item, ItemStack> getSeedToCropMap() {
        return seedTOCrop;
    }

    public static int getCropCount(Item seed) {
        if (seed instanceof IPlantable) {
            if (((IPlantable) seed).getPlant(null, null).getBlock() instanceof BlockInferiumCrop) {
                return ((ItemSeed) seed).getTier();
            }
            if (agradditionsModUtil != null) {
                return agradditionsModUtil.getCropCount(seed);
            }
        }
        return 1;
    }

    public static boolean isTier6Seed(Item seed) {
        return agradditionsModUtil != null && agradditionsModUtil.isTier6Seed(seed);
    }

    /**
     * 如果没有相应的rootBlockMeta，返回 -1 <p></p>
     * 否则 返回 相应的rootBlockMeta
     */
    public static int getRootBlockMeta(Item seed) {
        return agradditionsModUtil == null ? -1 : agradditionsModUtil.getRootBlockMeta(seed);
    }

    /**
     * 是否为 rootBlock，6级种子的额外方块
     */
    public static boolean isRootBlock(ItemStack rootBlock) {
        return agradditionsModUtil != null && agradditionsModUtil.isRootBlock(rootBlock);
    }

    public static int getSeedTier(Item seed) {
        if (seed instanceof ItemSeed) {
            return ((ItemSeed) seed).getTier();
        }
        if (isTier6Seed(seed)) {
            return 6;
        }
        return 1;
    }

}
