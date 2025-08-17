package com.xiaoxianben.lazymystical.gui;

import com.xiaoxianben.lazymystical.api.IHasHandlerComponent;
import com.xiaoxianben.lazymystical.gui.container.ContainerAccelerantFluid;
import com.xiaoxianben.lazymystical.gui.container.ContainerComponent;
import com.xiaoxianben.lazymystical.gui.container.ContainerSeedCultivator;
import com.xiaoxianben.lazymystical.gui.guiScreen.GuiAccelerantFluid;
import com.xiaoxianben.lazymystical.gui.guiScreen.GuiComponent;
import com.xiaoxianben.lazymystical.gui.guiScreen.GuiSeedCultivator;
import com.xiaoxianben.lazymystical.tileEntity.TEAccelerantFluid;
import com.xiaoxianben.lazymystical.tileEntity.TEBase;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler {

    //对每个自定义GUI进行序号编码
    public static final int id_seedCultivator = 1;
    public static final int id_accelerantFluid = 2;
    public static final int id_component = 3;


    //在服务端中运行的逻辑
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        //通过编码创建服务端的Container
        TEBase te = (TEBase) world.getTileEntity(new BlockPos(x, y, z));

        switch (ID) {
            case id_seedCultivator:
                return new ContainerSeedCultivator(player, (TESeedCultivator) te);
            case id_accelerantFluid:
                return new ContainerAccelerantFluid(player, (TEAccelerantFluid) te);
            case id_component:
                return new ContainerComponent<>(player, (TEBase & IHasHandlerComponent) te);
        }
        return null;
    }

    //在客户端中运行的逻辑
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TEBase te = (TEBase) world.getTileEntity(new BlockPos(x, y, z));

        //通过编码创建客户端的Container与GuiContainer（Forge会自动托管服务端到客户端的Container同步）
        switch (ID) {
            case id_seedCultivator:
                return new GuiSeedCultivator(new ContainerSeedCultivator(player, (TESeedCultivator) te));
            case id_accelerantFluid:
                return new GuiAccelerantFluid(new ContainerAccelerantFluid(player, (TEAccelerantFluid) te));
            case id_component:
                return new GuiComponent<>(new ContainerComponent<>(player, (TEBase & IHasHandlerComponent) te));
        }
        return null;
    }
}
