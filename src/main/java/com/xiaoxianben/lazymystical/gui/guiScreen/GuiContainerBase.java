package com.xiaoxianben.lazymystical.gui.guiScreen;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.api.IHasHandlerComponent;
import com.xiaoxianben.lazymystical.gui.GUIHandler;
import com.xiaoxianben.lazymystical.gui.container.ContainerBase;
import com.xiaoxianben.lazymystical.gui.guiButton.ButtonComponentChange;
import com.xiaoxianben.lazymystical.network.data.DataGuiChange;
import com.xiaoxianben.lazymystical.tileEntity.TEBase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class GuiContainerBase<container extends ContainerBase<?>> extends GuiContainer {

    protected container container;

    public GuiContainerBase(container container) {
        super(container);

        this.container = container;
    }


    @Override
    public void initGui() {
        super.initGui();

        TEBase te = container.getTE();
        if (te instanceof IHasHandlerComponent && ((IHasHandlerComponent) te).getHandlerComponent().getSlots() > 0) {
            addButton(new ButtonComponentChange(0, this.guiLeft, this.guiTop));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            LazyMystical.getChannel().sendToServer(new DataGuiChange(GUIHandler.id_component, container.getTE().getPos()).toMessage());
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        bindDefaultTexture();

        drawForegroundTexture();
        drawForegroundString();
        drawHoveredTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        bindDefaultTexture();
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        drawBackgroundTexture();
        drawBackgroundString();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }


    protected abstract ResourceLocation getTextureResourceLocation();

    protected abstract void drawBackgroundTexture();

    protected abstract void drawBackgroundString();

    protected abstract void drawForegroundTexture();

    protected abstract void drawForegroundString();

    protected abstract void drawHoveredTip(int mouseX, int mouseY);

    protected void bindDefaultTexture() {
        this.mc.getTextureManager().bindTexture(getTextureResourceLocation());
    }

    public List<Rectangle> getGuiExtraAreas() {
        List<Rectangle> list = new ArrayList<>();

        TEBase te = container.getTE();
        if (te instanceof IHasHandlerComponent && ((IHasHandlerComponent) te).getHandlerComponent().getSlots() > 0) {
            list.add(new Rectangle(this.guiLeft + 176, this.guiTop + 25, 18, 18));
        }

        return list;
    }

    @Nullable
    public Object getIngredientUnderMouse(int mouseX, int mouseY) {
        return null;
    }
}
