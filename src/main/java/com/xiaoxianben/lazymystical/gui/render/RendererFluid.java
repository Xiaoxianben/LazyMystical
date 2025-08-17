package com.xiaoxianben.lazymystical.gui.render;


import com.xiaoxianben.lazymystical.LazyMystical;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 参考 EnderCore 的 com.enderio.core.client.render.RenderUtil
 * <p>
 * 感谢 SleepyTrousers 团队 的 EnderCore 的 开源的代码
 */
@Mod.EventBusSubscriber(
        modid = LazyMystical.MOD_ID,
        value = Side.CLIENT
)
public class RendererFluid {

    private static final @Nonnull ResourceLocation BLOCK_TEX = TextureMap.LOCATION_BLOCKS_TEXTURE;
    private static TextureMap fluidTextures = null;


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        fluidTextures = event.getMap();
    }


    private static @Nonnull TextureManager engine() {
        return Minecraft.getMinecraft().renderEngine;
    }

    private static void bindBlockTexture() {
        engine().bindTexture(BLOCK_TEX);
    }

    private static @Nonnull TextureAtlasSprite getMissingSprite() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }

    private static @Nonnull TextureAtlasSprite getStillTexture(@Nonnull FluidStack fluidstack) {
        final Fluid fluid = fluidstack.getFluid();
        if (fluid == null) {
            return getMissingSprite();
        }
        return getStillTexture(fluid);
    }

    private static @Nonnull TextureAtlasSprite getStillTexture(@Nonnull Fluid fluid) {
        ResourceLocation iconKey = fluid.getStill();
        final TextureAtlasSprite textureExtry = fluidTextures.getTextureExtry(iconKey.toString());
        return textureExtry != null ? textureExtry : getMissingSprite();
    }

    public static void renderGuiTank(@Nonnull FluidTank tank, double x, double y, double zLevel, double width, double height) {
        renderGuiTank(tank.getFluid(), tank.getCapacity(), tank.getFluidAmount(), x, y, zLevel, width, height);
    }

    public static void renderGuiTank(@Nullable FluidStack fluid, int capacity, int amount, double x, double y, double zLevel, double width, double height) {
        if (fluid == null || fluid.getFluid() == null || amount <= 0) {
            return;
        }

        TextureAtlasSprite icon = getStillTexture(fluid);

        int renderAmount = (int) Math.max(Math.min(height, amount * height / capacity), 1);
        int posY = (int) (y + height - renderAmount);

        bindBlockTexture();
        int color = fluid.getFluid().getColor(fluid);
        GlStateManager.color((color >> 16 & 0xFF) / 255f, (color >> 8 & 0xFF) / 255f, (color & 0xFF) / 255f);

        GlStateManager.enableBlend();
        for (int i = 0; i < width; i += 16) {
            for (int j = 0; j < renderAmount; j += 16) {
                int drawWidth = (int) Math.min(width - i, 16);
                int drawHeight = Math.min(renderAmount - j, 16);

                int drawX = (int) (x + i);
                int drawY = posY + j;

                double minU = icon.getMinU();
                double maxU = icon.getMaxU();
                double minV = icon.getMinV();
                double maxV = icon.getMaxV();

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder tes = tessellator.getBuffer();
                tes.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

                double v = minV + (maxV - minV) * drawHeight / 16F;
                double u = minU + (maxU - minU) * drawWidth / 16F;
                tes.pos(drawX, drawY + drawHeight, zLevel).tex(minU, v).endVertex();
                tes.pos(drawX + drawWidth, drawY + drawHeight, 0).tex(u, v).endVertex();
                tes.pos(drawX + drawWidth, drawY, zLevel).tex(u, minV).endVertex();
                tes.pos(drawX, drawY, zLevel).tex(minU, minV).endVertex();
                tessellator.draw();
            }
        }
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1);
    }

}