package com.xiaoxianben.lazymystical.GUI;

import com.xiaoxianben.lazymystical.block.TESeedCultivator;
import com.xiaoxianben.lazymystical.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class BlockGUI extends GuiContainer {
    private static final ResourceLocation TEXTURES = new ResourceLocation(Reference.MOD_ID, "textures/gui/1.png");

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
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

        if (this.tileEntity.tick == 1) {
            float i1 = (tileEntity.getMaxTimeRun() - tileEntity.timeRun) / tileEntity.getMaxTimeRun();
            this.drawTexturedModalRect(x + 79, y + 27, 176, 14, (int) (i1 * 22 + 2), 16);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        // 绘制能量条等其他GUI元素
        this.fontRenderer.drawString(tileEntity.level + "级种子培养器", this.width / 2 - 46 / 2, y + 6, 4210752);
        this.fontRenderer.drawString("物品栏", x + 8, y + this.ySize - 96 + 2, 4210752);
    }

}
