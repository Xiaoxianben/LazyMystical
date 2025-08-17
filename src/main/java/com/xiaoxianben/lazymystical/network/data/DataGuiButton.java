package com.xiaoxianben.lazymystical.network.data;

import com.xiaoxianben.lazymystical.network.MessageGui;
import net.minecraft.nbt.NBTTagCompound;

public class DataGuiButton extends DataBase<MessageGui> {
    public static final int typeGuiButton = 1;

    public int buttonId;
    public int[] dataInts;

    public DataGuiButton() {
    }

    public DataGuiButton(int buttonId, int... dataInts) {
        this.buttonId = buttonId;
        this.dataInts = dataInts;
    }

    @Override
    public DataBase<MessageGui> fromMessage(MessageGui m) {
        buttonId = m.nbt.getInteger("buttonId");
        dataInts = m.nbt.getIntArray("dataInts");
        return this;
    }

    @Override
    public MessageGui toMessage() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("type", typeGuiButton);
        nbt.setInteger("buttonId", buttonId);
        nbt.setIntArray("dataInts", dataInts);
        return new MessageGui(nbt);
    }
}
