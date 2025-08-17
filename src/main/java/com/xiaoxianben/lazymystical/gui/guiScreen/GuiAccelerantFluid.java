package com.xiaoxianben.lazymystical.gui.guiScreen;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.gui.container.ContainerAccelerantFluid;
import com.xiaoxianben.lazymystical.gui.render.RendererFluid;
import com.xiaoxianben.lazymystical.tileEntity.TEAccelerantFluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class GuiAccelerantFluid extends GuiContainerBase<ContainerAccelerantFluid> {

    private static final ResourceLocation TEXTURES = new ResourceLocation(LazyMystical.MOD_ID, "textures/gui/2.png");


    private final TEAccelerantFluid tileEntity;


    public GuiAccelerantFluid(ContainerAccelerantFluid container) {
        super(container);
        this.tileEntity = container.getTE();
    }


    @Override
    protected ResourceLocation getTextureResourceLocation() {
        return TEXTURES;
    }

    @Override
    protected void drawBackgroundTexture() {
        if (this.tileEntity.isRunning) {
            this.drawTexturedModalRect(this.guiLeft + 63, this.guiTop + 33, 176, 0, 22, 16);
        }

        this.drawEnergyTexturedRect(this.tileEntity.energyStorage.getEnergyStored(), this.tileEntity.energyStorage.getMaxEnergyStored());

        RendererFluid.renderGuiTank(this.tileEntity.container.fluidTank, this.guiLeft + 95, this.guiTop + 14, 0, 16, 58);
    }

    @Override
    protected void drawBackgroundString() {
        // 文字
        String blockName = tileEntity.getBlockType().getLocalizedName();
        this.fontRenderer.drawString(blockName, this.guiLeft + (this.getXSize() - this.fontRenderer.getStringWidth(blockName)) / 2, this.guiTop + 4, 4210752);
    }

    @Override
    protected void drawForegroundTexture() {

    }

    @Override
    protected void drawForegroundString() {

    }

    @Override
    protected void drawHoveredTip(int mouseX, int mouseY) {
        this.drawMouseRect(mouseX, mouseY, 7, 4, 20, 72,
                String.format("FE:\n%sFE|%sFE",
                        this.tileEntity.energyStorage.getEnergyStored(),
                        this.tileEntity.energyStorage.getMaxEnergyStored()).split("\n")
        );


        FluidStack fluidTankFluid = this.tileEntity.container.fluidTank.getFluid();

        String fluidName = fluidTankFluid == null ? "null" : fluidTankFluid.getLocalizedName();

        this.drawMouseRect(mouseX, mouseY, 95, 14, 16, 58,
                String.format("%s:\n%s|%s",
                                fluidName,
                                this.tileEntity.container.fluidTank.getFluidAmount(),
                                this.tileEntity.container.fluidTank.getCapacity())
                        .split("\n")
        );

    }


    /**
     * 用于前景绘制
     */
    protected void drawMouseRect(int mouseX, int mouseY, int textureX, int textureY, int width, int height, String[] text) {
        int X = this.guiLeft + textureX;
        int Y = this.guiTop + textureY;
        if (mouseX > X && mouseX <= X + width &&
                mouseY > Y && mouseY <= Y + height
        ) {
            // 绘画图形的阴影
            drawRect(textureX, textureY,
                    textureX + width, textureY + height, 0x90FFFFFF);
            // 绘画文字框
            drawHoveringText(Arrays.stream(text).collect(Collectors.toList()), mouseX - this.guiLeft, mouseY - this.guiTop);
        }
    }

    protected void drawEnergyTexturedRect(float newInt, float maxInt) {
        if (newInt > 0) {
            int height = (int) ((newInt / maxInt) * 70); // 计算矩形的高
            int textureY = 70 - height; // 计算被截取的矩形的顶部的位置
            this.drawTexturedModalRect(this.guiLeft + 8, this.guiTop + 5 + textureY, 177, 17 + textureY, 18, height);
        }
    }

}
