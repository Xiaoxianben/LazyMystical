package com.xiaoxianben.lazymystical.tileEntity.energy;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyStorageBetter implements IEnergyStorage, INBTSerializable<NBTTagCompound> {


    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;


    public EnergyStorageBetter(int capacity) {
        this.capacity = capacity;
        this.maxReceive = capacity;
        this.maxExtract = capacity;
        this.energy = 0;
    }


    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            energy += energyReceived;
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            energy -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }


    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("energy", energy);
        tag.setInteger("capacity", capacity);
        tag.setInteger("maxReceive", maxReceive);
        tag.setInteger("maxExtract", maxExtract);
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        energy = nbt.getInteger("energy");
        capacity = nbt.getInteger("capacity");
        maxReceive = nbt.getInteger("maxReceive");
        maxExtract = nbt.getInteger("maxExtract");
    }


    public int modifyEnergyStored(int energy) {
        if (energy > 0) {
            energy = Math.min(energy, this.capacity - this.energy);
        } else {
            energy = -Math.min(-energy, this.energy);
        }
        this.energy += energy;
        return energy;
    }
}
