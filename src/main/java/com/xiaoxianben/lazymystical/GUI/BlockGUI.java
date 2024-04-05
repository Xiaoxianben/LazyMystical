package com.xiaoxianben.lazymystical.GUI;

import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import com.xiaoxianben.lazymystical.util.ModInformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class BlockGUI extends GuiContainer {
    private static final ResourceLocation TEXTURES = new ResourceLocation(ModInformation.MOD_ID, "textures/gui/1.png");

    private final TESeedCultivator tileEntity;
    private final FontRenderer fontRenderer;


    public BlockGUI(Container container, TESeedCultivator tileEntity) {
        super(container);
        this.tileEntity = tileEntity;
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.mc.getTextureManager().bindTexture(TEXTURES);
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if (this.tileEntity.timeRun > -1) {
            float i1 = (tileEntity.getMaxTimeRun() - tileEntity.timeRun) / tileEntity.getMaxTimeRun();
            this.drawTexturedModalRect(this.guiLeft + 79, this.guiTop + 27, 176, 14, (int) (i1 * 24), 16);
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

}
