package com.xiaoxianben.lazymystical.tileEntity.handler;

import com.xiaoxianben.lazymystical.jsonRecipe.ModJsonRecipe;
import com.xiaoxianben.lazymystical.manager.ItemStackManager;
import com.xiaoxianben.lazymystical.tileEntity.TEAccelerantFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HandlerAccelerantFluid implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

    private final TEAccelerantFluid teAccelerantFluid;
    public final FluidTank fluidTank;
    public final ItemStackHandler itemStackHandler;
    int itemAccelerantFluidValue = -1;


    public HandlerAccelerantFluid(TEAccelerantFluid te) {
        this.teAccelerantFluid = te;
        fluidTank = new FluidTank(10000) {
            {
                setCanDrain(true);
                setCanFill(false);
            }

            public boolean canDrainFluidType(@Nullable FluidStack fluid) {
                return fluid != null && fluid.getFluid() == FluidRegistry.getFluid("accelerant_fluid");
            }
        };
        itemStackHandler = new ItemStackHandler() {
            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                boolean isTrueItem = ModJsonRecipe.accelerantFluidRecipe.inputs.stream().anyMatch(stack1 -> stack1.isItemEqual(stack));
                return slot == 0 && isTrueItem;
            }

            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                updateRecipe();
            }
        };
    }


    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(fluidTank);
        } else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        }
        return null;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbtHandler = new NBTTagCompound();
        ItemStackManager.writeToNBTOfItemStackHandler(itemStackHandler, nbtHandler);

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("itemStackHandler", nbtHandler);
        nbt.setTag("fluidTank", fluidTank.writeToNBT(new NBTTagCompound()));

        return nbt;
    }

    public void deserializeNBT(NBTTagCompound nbt) {
        ItemStackManager.readFromNBTOfItemStackHandler(itemStackHandler, nbt.getCompoundTag("itemStackHandler"));
        fluidTank.readFromNBT(nbt.getCompoundTag("fluidTank"));
    }


    /**
     * 更新配方
     */
    void updateRecipe() {
        ItemStack stackInSlot = itemStackHandler.getStackInSlot(0);
        if (stackInSlot.isEmpty()) {
            itemAccelerantFluidValue = -1;
            return;
        }
        Integer recipeOutput = ModJsonRecipe.accelerantFluidRecipe.getOutput(stackInSlot);
        if (recipeOutput == null) {
            itemAccelerantFluidValue = -1;
            return;
        }

        itemAccelerantFluidValue = recipeOutput;
    }

    public boolean canRunNumber() {
        ItemStack stackInSlot = itemStackHandler.getStackInSlot(0);
        if (stackInSlot.isEmpty()) {
            return false;
        }
        if (teAccelerantFluid.energyStorage.getEnergyStored() < (itemAccelerantFluidValue / 3)) {
            return false;
        }
        FluidStack fluidTankFluid = fluidTank.getFluid();
        int fluidEmptyAmount = fluidTank.getCapacity();
        if (fluidTankFluid != null) {
            fluidEmptyAmount -= fluidTankFluid.amount;
        }
        return Math.min(stackInSlot.getCount(), fluidEmptyAmount / itemAccelerantFluidValue) > 0;
    }

    void run() {
        fluidTank.fillInternal(FluidRegistry.getFluidStack("accelerant_fluid", itemAccelerantFluidValue), true);
        itemStackHandler.extractItem(0, 1, false);
    }

    /**
     * 更新容器，如果存在配方，将执行配方
     */
    public void update() {
        if (canRunNumber()) {
            teAccelerantFluid.isRunning = true;
            teAccelerantFluid.energyStorage.extractEnergy(itemAccelerantFluidValue / 3, false);
            run();
            teAccelerantFluid.sendUpdatePacket();
        } else {
            teAccelerantFluid.isRunning = false;
        }
    }

    public int getItemAccelerantFluidValue() {
        return itemAccelerantFluidValue;
    }
}
