package com.xiaoxianben.lazymystical.network;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.gui.container.ContainerBase;
import com.xiaoxianben.lazymystical.network.data.DataGuiButton;
import com.xiaoxianben.lazymystical.network.data.DataGuiChange;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageGui implements IMessage {

    public NBTTagCompound nbt;


    public MessageGui() {
    }

    public MessageGui(NBTTagCompound nbt) {
        this.nbt = nbt;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, nbt);
    }


    public static class MessageGuiHandler implements IMessageHandler<MessageGui, IMessage> {

        @Override
        public IMessage onMessage(MessageGui message, MessageContext ctx) {
            // 判断是否为服务端（接收端）
            EntityPlayerMP player = ctx.getServerHandler().player;

            if (ctx.side == Side.SERVER) {
                int type = message.nbt.getInteger("type");
                if (type == DataGuiChange.typeGuiChange) {
                    DataGuiChange dataGuiChange = (DataGuiChange) new DataGuiChange().fromMessage(message);
                    BlockPos pos = dataGuiChange.pos;

                    player.getServerWorld().addScheduledTask(() -> player.openGui(LazyMystical.instance, dataGuiChange.guiId, player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ()));
                } else if (type == DataGuiButton.typeGuiButton) {
                    DataGuiButton dataGuiButton = (DataGuiButton) new DataGuiButton().fromMessage(message);
                    int id = dataGuiButton.buttonId;

                    player.getServerWorld().addScheduledTask(() -> {
                        ContainerBase<?> openContainer = (ContainerBase<?>) player.openContainer;
                        openContainer.handlerButtonClick(id, dataGuiButton.dataInts);
                    });
                }
            }
            return null;
        }
    }

}
