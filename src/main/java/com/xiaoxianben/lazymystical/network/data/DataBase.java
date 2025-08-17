package com.xiaoxianben.lazymystical.network.data;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class DataBase<message extends IMessage> {

    /**
     * @param m the message
     * @return it should be this.
     */
    public abstract DataBase<message> fromMessage(message m);
    public abstract message toMessage();
}
