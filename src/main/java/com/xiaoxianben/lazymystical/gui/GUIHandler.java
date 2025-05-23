package com.xiaoxianben.lazymystical.gui;

import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {

    //对每个自定义GUI进行序号编码
    public static final int BlockGUI = 1;

    //在服务端中运行的逻辑
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        //通过编码创建服务端的Container
        TESeedCultivator teSeedCultivator = (TESeedCultivator) world.getTileEntity(new BlockPos(x, y, z));
        if (ID == BlockGUI) {
            return new BlockContainer(player, teSeedCultivator);
        }
        return null;
    }

    //在客户端中运行的逻辑
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TESeedCultivator teSeedCultivator = (TESeedCultivator) world.getTileEntity(new BlockPos(x, y, z));
        //通过编码创建客户端的Container与GuiContainer（Forge会自动托管服务端到客户端的Container同步）
        if (ID == BlockGUI) {
            return new BlockGUI(new BlockContainer(player, teSeedCultivator), teSeedCultivator);
        }
        return null;
    }
}
