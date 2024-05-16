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


    public static AgradditionsMoaUtil agradditionsMoaUtil = null;
    protected static LinkedHashMap<Item, ItemStack> seedTOCrop = new LinkedHashMap<>();
    protected static LinkedHashMap<Item, Integer> seedToMeta = new LinkedHashMap<>();


    public static void init() {
        if (Loader.isModLoaded("mysticalagradditions")) {
            agradditionsMoaUtil = new AgradditionsMoaUtil(seedToMeta);
        }

        ForgeRegistry<Item> registry = (ForgeRegistry<Item>) GameRegistry.findRegistry(Item.class);

        registry.getValuesCollection().stream()
                .filter(item -> getCropPrivate(item) != null)
                .sorted(Comparator.comparingInt(registry::getID))
                .forEach(seed -> {
                    seedTOCrop.put(seed, getCropPrivate(seed));
                    if (agradditionsMoaUtil != null) {
                        agradditionsMoaUtil.init(seed);
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

    public static HashMap<Item, Integer> getSeedToMeta() {
        return seedToMeta;
    }

    public static int getCropCount(Item seed) {
        if (seed instanceof IPlantable) {
            if (((IPlantable) seed).getPlant(null, null).getBlock() instanceof BlockInferiumCrop) {
                return ((ItemSeed) seed).getTier();
            }
            if (agradditionsMoaUtil != null) {
                return agradditionsMoaUtil.getCropCount(seed);
            }
        }
        return 1;
    }

    public static boolean isTier6Seed(Item seed) {
        return agradditionsMoaUtil != null && agradditionsMoaUtil.isTier6Seed(seed);
    }

    public static int getRootBlockMeta(Item seed) {
        return seedToMeta.get(seed) == null ? -1 : seedToMeta.get(seed);
    }

    public static boolean isRootBlock(ItemStack rootBlock) {
        return agradditionsMoaUtil != null && agradditionsMoaUtil.isRootBlock(rootBlock);
    }

    public static int getCropTier(Item seed) {
        if (seed instanceof ItemSeed) {
            return ((ItemSeed) seed).getTier();
        }
        if (isTier6Seed(seed)) {
            return 6;
        }
        return 1;
    }

}
