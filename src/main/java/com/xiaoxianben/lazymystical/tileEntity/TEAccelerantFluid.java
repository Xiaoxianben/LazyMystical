package com.xiaoxianben.lazymystical.tileEntity;

import com.xiaoxianben.lazymystical.api.IHasHandlerComponent;
import com.xiaoxianben.lazymystical.item.compomemt.EnumTypeComponent;
import com.xiaoxianben.lazymystical.tileEntity.energy.EnergyStorageBetter;
import com.xiaoxianben.lazymystical.tileEntity.handler.HandlerAccelerantFluid;
import com.xiaoxianben.lazymystical.tileEntity.itemHandler.ItemHandlerComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class TEAccelerantFluid extends TEBase implements IHasHandlerComponent {

    public final HandlerAccelerantFluid container;
    public final EnergyStorageBetter energyStorage;
    public final ItemHandlerComponent itemHandlerComponent;
    /** 是否正在运行，应该由内部修改，其它修改请谨慎 */
    public boolean isRunning = false;


    public TEAccelerantFluid() {
        super();
        container = new HandlerAccelerantFluid(this);
        itemHandlerComponent = new ItemHandlerComponent(EnumTypeComponent.Speed);
        energyStorage = new EnergyStorageBetter(10000);
    }


    @Override
    protected void updateInSever() {
        int i = -1;
        for (; i < getHandlerComponent().getComponentCountAndLevel(EnumTypeComponent.Speed); i++) {
            if (this.energyStorage.getEnergyStored() < (this.container.getItemAccelerantFluidValue() / 3)) {
                break;
            }
            container.update();
        }
        if (i > -1) {
            sendUpdatePacket();
        }
    }

    @Override
    protected void updateInClient() {
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return CapabilityEnergy.ENERGY.cast(energyStorage);
        } else if (container.hasCapability(capability, facing)) {
            return container.getCapability(capability, facing);
        }
        return super.getCapability(capability, facing);
    }

    // NBT
    @ParametersAreNonnullByDefault
    public void readFromNBT(NBTTagCompound compound) {
        // 读取相关数据到 实体 中
        super.readFromNBT(compound);

        container.deserializeNBT(compound.getCompoundTag("container"));
        energyStorage.deserializeNBT(compound.getCompoundTag("energyStorage"));
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // 将相关数据保存到 compound 中
        compound = super.writeToNBT(compound);

        compound.setTag("container", container.serializeNBT());
        compound.setTag("energyStorage", energyStorage.serializeNBT());

        return compound;
    }

    @Override
    public void handleNetworkUpdateNbt(NBTTagCompound NBT) {
        isRunning = NBT.getBoolean("isRunning");
    }

    @Nonnull
    @Override
    public NBTTagCompound getNetworkUpdateNbt() {
        NBTTagCompound NBT = new NBTTagCompound();
        NBT.setBoolean("isRunning", isRunning);
        return NBT;
    }

    @Nonnull
    @Override
    public ItemHandlerComponent getHandlerComponent() {
        return itemHandlerComponent;
    }
}
