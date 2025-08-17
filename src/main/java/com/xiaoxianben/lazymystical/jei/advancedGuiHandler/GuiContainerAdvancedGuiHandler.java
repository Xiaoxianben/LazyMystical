package com.xiaoxianben.lazymystical.jei.advancedGuiHandler;

import com.xiaoxianben.lazymystical.gui.guiScreen.GuiContainerBase;
import mezz.jei.api.gui.IAdvancedGuiHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class GuiContainerAdvancedGuiHandler implements IAdvancedGuiHandler<GuiContainerBase> {

    @Nonnull
    @Override
    public Class<GuiContainerBase> getGuiContainerClass() {
        return GuiContainerBase.class;
    }

    @Nullable
    @Override
    public List<Rectangle> getGuiExtraAreas(GuiContainerBase guiContainer) {
        return guiContainer.getGuiExtraAreas();
    }

    @Nullable
    @Override
    public Object getIngredientUnderMouse(@Nonnull GuiContainerBase guiContainer, int mouseX, int mouseY) {
        return guiContainer.getIngredientUnderMouse(mouseX, mouseY);
    }
}
