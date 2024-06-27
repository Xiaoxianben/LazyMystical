package com.xiaoxianben.lazymystical.GUI;

import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import com.xiaoxianben.lazymystical.util.ModInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BlockGUI extends GuiContainer {
    private static final ResourceLocation TEXTURES = new ResourceLocation(ModInformation.MOD_ID, "textures/gui/1.png");

    private final TESeedCultivator tileEntity;
    private final FontRenderer fontRenderer;


    public BlockGUI(Container container, TESeedCultivator tileEntity) {
        super(container);
        this.tileEntity = tileEntity;
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }


    public static Rectangle getRecipeClickArea() {
        return new Rectangle(63, 33, 22, 16);
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(TEXTURES);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        for (Rectangle guiExtraArea : this.getGuiExtraAreas()) {
            this.drawTexturedModalRect(guiExtraArea.x, guiExtraArea.y, 7, 83, guiExtraArea.width, guiExtraArea.height);
        }
        if (this.tileEntity.level == 6) {
            this.drawTexturedModalRect(this.guiLeft + 38, this.guiTop + 32 + 18, 7, 83, 18, 18);
        }
        if (this.tileEntity.timeRun > -1) {
            float i1 = ((float) (tileEntity.getMaxTimeRun() - tileEntity.timeRun)) / tileEntity.getMaxTimeRun();
            int weight = (int) (i1 * 22);
            this.drawTexturedModalRect(this.guiLeft + 63, this.guiTop + 33, 176, 0, Math.max(weight, 1), 16);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        // 绘制能量条等其他GUI元素
        String blockName = tileEntity.getBlockType().getLocalizedName();
        this.fontRenderer.drawString(blockName, this.guiLeft + (this.getXSize() - this.fontRenderer.getStringWidth(blockName)) / 2, this.guiTop + 4, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), this.guiLeft + 8, this.guiTop + this.ySize - 96 + 2, 4210752);
    }

    public List<Rectangle> getGuiExtraAreas() {
        List<Rectangle> guiExtraAreas = new ArrayList<>();
        for (Rectangle guiExtraArea : ((BlockContainer) this.inventorySlots).getGuiExtraAreas()) {
            guiExtraAreas.add(new Rectangle(guiExtraArea.x + this.guiLeft, guiExtraArea.y + this.guiTop, guiExtraArea.width, guiExtraArea.height));
        }
        return guiExtraAreas;
    }

}
