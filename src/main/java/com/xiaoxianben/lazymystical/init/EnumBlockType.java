package com.xiaoxianben.lazymystical.init;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.block.BlockAccelerantDirt;
import com.xiaoxianben.lazymystical.block.BlockAccelerantFarmland;
import com.xiaoxianben.lazymystical.block.BlockAccelerator;
import com.xiaoxianben.lazymystical.block.BlockHandCrankGenerator;
import com.xiaoxianben.lazymystical.block.machine.BlockAccelerantFluid;
import com.xiaoxianben.lazymystical.block.machine.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.init.register.EnumModRegister;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import java.util.Collection;
import java.util.EnumMap;
import java.util.function.Function;
import java.util.function.Supplier;

public enum EnumBlockType {
    Accelerator(int.class, BlockAccelerator::new),
    SeedCultivator(int.class, BlockSeedCultivator::new),
    AccelerantFluid(int.class, BlockAccelerantFluid::new),
    AccelerantDirt(int.class, BlockAccelerantDirt::new),
    AccelerantFarmland(int.class, BlockAccelerantFarmland::new),
    HandCrankGenerator(BlockHandCrankGenerator::new),
    BlockFluid(Fluid.class, fluid -> new BlockFluidClassic(fluid, Material.WATER).setRegistryName(LazyMystical.MOD_ID, fluid.getName()).setUnlocalizedName(LazyMystical.MOD_ID+"-"+fluid.getName()), false);


//    private final int index;
    private final EnumMap<EnumModRegister, Block[]> mapBlocks = new EnumMap<>(EnumModRegister.class);

    /** 使用 Object 作为输入，运行时转换 */
    private final Function<Object, ? extends Block> fun;
    private final Supplier<? extends Block> sup;
    /** 存储输入类型（如 Integer.class, String.class）*/
    public final Class<?> inputType;
    public final boolean allHasItemBlock;


    <input> EnumBlockType(Class<input> inputType, Function<input, ? extends Block> fun, boolean HasItemBlock) {
        this.inputType = inputType;
        this.fun = obj -> fun.apply((input) obj);
        this.sup = null;
        this.allHasItemBlock = HasItemBlock;
    }
    <input> EnumBlockType(Class<input> inputType, Function<input, ? extends Block> fun) {
        this(inputType, fun, true);
    }
    EnumBlockType(Supplier<? extends Block> sup) {
        this.inputType = null;
        this.fun = null;
        this.sup = sup;
        this.allHasItemBlock = true;
    }


    /**
     * 创建 Block，自动检查输入类型
     * @param input 输入参数（必须匹配枚举定义的输入类型）
     * @return 创建的 Block
     * @throws RuntimeException 如果输入类型不匹配
     */
    public <T> Block create(T input) {
        if (inputType == null) {
            try {
                return this.sup.get();
            } catch (Exception e) {
                throw new RuntimeException("block don't create with sup", e);
            }
        }
        try {
            return fun.apply(input);
        } catch (Exception e) {
            throw new RuntimeException("block don't create with fun", e);
        }
    }

    public void setBlocks(EnumModRegister register, Block... blocks) {
        mapBlocks.put(register, blocks);
    }

    public Block[] getBlocks(EnumModRegister register) {
        return this.mapBlocks.get(register);
    }
    public Collection<Block[]> getAllBlocks() {
        return this.mapBlocks.values();
    }
}