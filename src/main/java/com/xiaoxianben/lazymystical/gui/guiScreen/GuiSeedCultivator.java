package com.xiaoxianben.lazymystical.gui.guiScreen;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.gui.container.ContainerSeedCultivator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSeedCultivator extends GuiContainerBase<ContainerSeedCultivator> {

    private final static ResourceLocation TEXTURES = new ResourceLocation(LazyMystical.MOD_ID, "textures/gui/1.png");


    public GuiSeedCultivator(ContainerSeedCultivator container) {
        super(container);
    }


    @Override
    protected ResourceLocation getTextureResourceLocation() {
        return TEXTURES;
    }

    @Override
    protected void drawBackgroundTexture() {
        if (this.container.getTE().blockLevel >= 6) {
            this.drawTexturedModalRect(this.guiLeft + 66, this.guiTop + 32, 7, 83, 18, 18);
        }

        if (this.container.getTE().timeRun > -1) {
            float i1 = ((float) (container.getTE().maxTimeRun - container.getTE().timeRun)) / container.getTE().maxTimeRun;
            int weight = (int) (i1 * 22);
            this.drawTexturedModalRect(this.guiLeft + 64, this.guiTop + 15, 176, 0, Math.max(weight, 1), 16);
        }
    }

    @Override
    protected void drawBackgroundString() {
        // 绘制能量条等其他GUI元素
        String blockName = container.getTE().getBlockType().getLocalizedName();
        this.fontRenderer.drawString(blockName, this.guiLeft + (this.getXSize() - this.fontRenderer.getStringWidth(blockName)) / 2, this.guiTop + 4, 4210752);
        this.fontRenderer.drawString(I18n.format("container.inventory"), this.guiLeft + 8, this.guiTop + this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawForegroundTexture() {

    }

    @Override
    protected void drawForegroundString() {

    }

    @Override
    protected void drawHoveredTip(int mouseX, int mouseY) {

    }

}
