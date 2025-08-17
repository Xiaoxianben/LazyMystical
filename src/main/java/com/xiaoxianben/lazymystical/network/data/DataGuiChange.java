package com.xiaoxianben.lazymystical.network.data;

import com.xiaoxianben.lazymystical.network.MessageGui;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class DataGuiChange extends DataBase<MessageGui> {
    public static final int typeGuiChange = 0;


    public int guiId;
    public BlockPos pos;

    public DataGuiChange() {
    }

    public DataGuiChange(int id, BlockPos pos) {
        this.guiId = id;
        this.pos = pos;
    }

    protected NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("type", typeGuiChange);
        nbt.setInteger("guiId", guiId);
        nbt.setLong("pos", pos.toLong());
        return nbt;
    }

    @Override
    public DataBase<MessageGui> fromMessage(MessageGui m) {
        NBTTagCompound nbt = m.nbt;
        guiId = nbt.getInteger("guiId");
        pos = BlockPos.fromLong(nbt.getLong("pos"));
        return this;
    }

    public MessageGui toMessage() {
        return new MessageGui(toNbt());
    }
}
