package com.xiaoxianben.lazymystical.api;

import net.minecraft.nbt.NBTTagCompound;

public interface IUpdateNBT {

    /**
     * 更新NBT
     */
    void updateNBT(NBTTagCompound NBT);

    /**
     * 获取进行网络更新的nbt
     */
    NBTTagCompound getUpdateNBT();

}
