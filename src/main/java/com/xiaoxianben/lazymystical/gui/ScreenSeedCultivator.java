package com.xiaoxianben.lazymystical.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.tileEntity.IntArraySeedCultivator;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class ScreenSeedCultivator extends ContainerScreen<ContainerSeedCultivator> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(LazyMystical.MODID, "textures/gui/1.png");


    public ScreenSeedCultivator(ContainerSeedCultivator containerSeedCultivator, PlayerInventory inventory, ITextComponent textComponent) {
        super(containerSeedCultivator, inventory, textComponent);
    }


    @ParametersAreNonnullByDefault
    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(TEXTURES);
        this.blit(matrixStack, this.getGuiLeft(), this.getGuiTop(), 0, 0, this.getXSize(), this.getYSize());

//        for (Rectangle guiExtraArea : this.getGuiExtraAreas()) {
//            this.blit(matrixStack, guiExtraArea.x, guiExtraArea.y, 7, 83, guiExtraArea.width, guiExtraArea.height);
//        }
        IntArraySeedCultivator intArray = this.menu.intArray;
        if (intArray.get(0) > -1) {
            float i1 = ((float) (intArray.get(1) - intArray.get(0))) / intArray.get(1);
            int weight = (int) (i1 * 22);
            this.blit(matrixStack, this.getGuiLeft() + 63, this.getGuiTop() + 33, 176, 0, Math.max(weight, 1), 16);
        }
    }


    @ParametersAreNonnullByDefault
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        renderTooltip(matrixStack, mouseX, mouseY);
    }

}
