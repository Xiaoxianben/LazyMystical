package com.xiaoxianben.lazymystical.tileEntity;

import com.xiaoxianben.lazymystical.tileEntity.energy.EnergyStorageBetter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Objects;

public class TEGeneratorHandCrank extends TEBase{

    public final EnergyStorageBetter energyStorage;
    /**
     * 每tick的将要传输能量的方向的列表
     */
    protected final HashSet<EnumFacing> transferEnergyFacings = new HashSet<>();
    /**
     * 每tick的传输能量的数值列表
     */
    protected final HashSet<Integer> finallyExtractEnergyList = new HashSet<>();
    public long finallyExtractEnergy = 0;


    public TEGeneratorHandCrank() {
        super();
        energyStorage = new EnergyStorageBetter(10000);
    }


    public void generatedOnceEnergy() {
        energyStorage.receiveEnergy(10, false);
    }

    // ------------------------------------------传输能量的部分-------------------------------------------------
    protected boolean updateTransferFacings(World world) {
        this.transferEnergyFacings.clear();
        this.finallyExtractEnergyList.clear();

        for (EnumFacing facing : EnumFacing.VALUES) {
            if (this.canTransferEnergy(world.getTileEntity(this.getPos().offset(facing)), facing.getOpposite())) {
                this.transferEnergyFacings.add(facing);
            }
        }
        return !this.transferEnergyFacings.isEmpty();
    }

    /**
     * @param TE     要传输能量的相邻TE
     * @param facing 相邻TE要传输能量的方向
     */
    protected boolean canTransferEnergy(TileEntity TE, EnumFacing facing) {
        if (TE != null && TE.hasCapability(CapabilityEnergy.ENERGY, facing)) {
            IEnergyStorage iEnergyStorage = Objects.requireNonNull(TE.getCapability(CapabilityEnergy.ENERGY, facing));
            return iEnergyStorage.receiveEnergy(this.energyStorage.getEnergyStored(), true) != 0;
        }
        return false;
    }

    protected void transferEnergy(TileEntity adjacentTE, EnumFacing facing) {
        IEnergyStorage iEnergyStorage = Objects.requireNonNull(adjacentTE.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()));

        int temp = iEnergyStorage.receiveEnergy(this.energyStorage.getEnergyStored(), false);

        this.finallyExtractEnergyList.add(this.energyStorage.modifyEnergyStored(-temp));
    }
    // ------------------------------------------------------------------------------------------------------

    @Override
    protected void updateInSever() {
        if (updateTransferFacings(this.getWorld())) {
            for (EnumFacing facing : this.transferEnergyFacings) {
                TileEntity tileEntity = this.getWorld().getTileEntity(this.getPos().offset(facing));
                transferEnergy(tileEntity, facing);
            }
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
        }
        return super.getCapability(capability, facing);
    }

    // -----------------------------------------------Nbt----------------------------------------------------
    @ParametersAreNonnullByDefault
    public void readFromNBT(NBTTagCompound compound) {
        // 读取相关数据到 实体 中
        super.readFromNBT(compound);

        energyStorage.deserializeNBT(compound.getCompoundTag("energyStorage"));
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // 将相关数据保存到 compound 中
        compound = super.writeToNBT(compound);

        compound.setInteger("blockLevel", blockLevel);
        compound.setTag("energyStorage", energyStorage.serializeNBT());

        return compound;
    }

    @Override
    public void handleNetworkUpdateNbt(NBTTagCompound NBT) {
        this.finallyExtractEnergy = NBT.getLong("finallyExtractEnergy");
    }

    @Nonnull
    @Override
    public NBTTagCompound getNetworkUpdateNbt() {
        NBTTagCompound NBT = new NBTTagCompound();

        for (Integer i : finallyExtractEnergyList) {
            this.finallyExtractEnergy += i;
        }
        NBT.setLong("finallyExtractEnergy", finallyExtractEnergy);

        return NBT;
    }
    // ------------------------------------------------------------------------------------------------------
}
