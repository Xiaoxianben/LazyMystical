package com.xiaoxianben.lazymystical.fluid;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.init.register.EnumModRegister;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;

public class FluidRegisterer {
    public static Fluid registerFluid(EnumModRegister selfRegister, String fluidName) {
        Fluid fluid = new Fluid(fluidName, new ResourceLocation(LazyMystical.MOD_ID, "fluids/"+fluidName+"_still"), new ResourceLocation(LazyMystical.MOD_ID, "fluids/"+fluidName+"_flow"));
        FluidRegistry.addBucketForFluid(fluid);

        Block blockFluid = EnumBlockType.BlockFluid.create(fluid);
        EnumBlockType.BlockFluid.setBlocks(selfRegister, blockFluid);

        return fluid;
    }

    public static ItemStack getBucket(String fluidName) {
        return FluidUtil.getFilledBucket(FluidRegistry.getFluidStack(fluidName, 10000));
    }
}
