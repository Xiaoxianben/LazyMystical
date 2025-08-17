package com.xiaoxianben.lazymystical.api;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public interface IAmHandlerNetwork {

    /**
     * 根据NBT更新this，一般情况下，是客户端盖启用这方法。
     */
    void handleNetworkUpdateNbt(NBTTagCompound NBT);

    /**
     * 获取进行网络更新的nbt，一般情况下，是服务端盖启用这方法。
     */
    @Nonnull
    NBTTagCompound getNetworkUpdateNbt();

}
