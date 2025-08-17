package com.xiaoxianben.lazymystical.tileEntity;

import com.xiaoxianben.lazymystical.api.IAmHandlerNetwork;
import com.xiaoxianben.lazymystical.api.IHasHandlerComponent;
import com.xiaoxianben.lazymystical.block.machine.BlockMachineBase;
import com.xiaoxianben.lazymystical.config.ConfigValue;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public abstract class TEBase extends TileEntity implements ITickable, IAmHandlerNetwork {


    public int blockLevel = -1;
    public int tick = 0;
    public int openGuiCount = 0;
    public boolean isActive = true;


    public TEBase() {
        super();
    }


    @Override
    public void onLoad() {
        super.onLoad();
        setBlockLevel();
    }

    protected void setBlockLevel() {
        if (this.blockLevel <= 0 && this.world.getBlockState(this.pos).getBlock() instanceof BlockMachineBase) {
            this.blockLevel = ((BlockMachineBase) this.getBlockType()).level;
        }
    }

    @ParametersAreNonnullByDefault
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return this.getCapability(capability, facing) != null;
    }

    @Override
    public void update() {
        if (!this.hasWorld() || !this.isActive) {
            return;
        }
        if (this.getWorld().isRemote) {
            updateInClient();
        } else {
            updateInSever();
        }

    }

    // -----------------------------------------------Nbt----------------------------------------------------
    @ParametersAreNonnullByDefault
    public void readFromNBT(NBTTagCompound compound) {
        // 读取相关数据到 实体 中
        super.readFromNBT(compound);

        this.blockLevel = compound.getInteger("blockLevel");
        this.isActive = compound.getBoolean("isActive");
        if (this instanceof IHasHandlerComponent) {
            ((IHasHandlerComponent) this).getHandlerComponent().deserializeNBT(compound.getCompoundTag("itemHandlerComponent"));
        }
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // 将相关数据保存到 compound 中
        compound = super.writeToNBT(compound);

        compound.setInteger("blockLevel", blockLevel);
        compound.setBoolean("isActive", isActive);
        if (this instanceof IHasHandlerComponent) {
            compound.setTag("itemHandlerComponent", ((IHasHandlerComponent) this).getHandlerComponent().serializeNBT());
        }

        return compound;
    }

    public void sendUpdatePacket() {
        if (this.getWorld().isRemote) return;
        if (tick < ConfigValue.tileEntitySyncTick && openGuiCount <= 0) {
            ++tick;
            return;
        }
        tick = 0;
        SPacketUpdateTileEntity packet = this.getUpdatePacket();
        // 获取当前正在“追踪”目标 TileEntity 所在区块的玩家。
        // 之所以这么做，是因为在逻辑服务器上，不是所有的玩家都需要获得某个 TileEntity 更新的信息。
        // 比方说，有一个玩家和需要同步的 TileEntity 之间差了八千方块，或者压根儿就不在同一个维度里。
        // 这个时候就没有必要同步数据——强行同步数据实际上也没有什么用，因为大多数时候这样的操作都应会被
        // World.isBlockLoaded（func_175667_e）的检查拦截下来，避免意外在逻辑客户端上加载多余的区块。
        PlayerChunkMapEntry trackingEntry = ((WorldServer) this.world).getPlayerChunkMap().getEntry(this.pos.getX() >> 4, this.pos.getZ() >> 4);
        if (trackingEntry != null) {
            for (EntityPlayerMP player : trackingEntry.getWatchingPlayers()) {
                player.connection.sendPacket(packet);
            }
        }
    }

    @Override
    @Nonnull
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound updateNBT = new NBTTagCompound();

        updateNBT.setTag("updateNBT", this.getNetworkUpdateNbt());
        updateNBT.setTag("NBT", this.writeToNBT(new NBTTagCompound()));
        // 发送更新标签
        return new SPacketUpdateTileEntity(this.getPos(), 1, updateNBT);
    }

    @Override
    public void onDataPacket(@Nonnull net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound().getCompoundTag("NBT"));
        this.handleNetworkUpdateNbt(pkt.getNbtCompound().getCompoundTag("updateNBT"));
    }
    // ------------------------------------------------------------------------------------------------------

    protected abstract void updateInSever();

    protected abstract void updateInClient();
}
