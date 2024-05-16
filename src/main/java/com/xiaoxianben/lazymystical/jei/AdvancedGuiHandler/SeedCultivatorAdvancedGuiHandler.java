package com.xiaoxianben.lazymystical.jei.AdvancedGuiHandler;

import com.xiaoxianben.lazymystical.GUI.BlockGUI;
import mezz.jei.api.gui.IAdvancedGuiHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class SeedCultivatorAdvancedGuiHandler implements IAdvancedGuiHandler<BlockGUI> {

    @Nonnull
    @Override
    public Class<BlockGUI> getGuiContainerClass() {
        return BlockGUI.class;
    }

    @Nullable
    @Override
    public List<Rectangle> getGuiExtraAreas(BlockGUI guiContainer) {
        return guiContainer.getGuiExtraAreas();
    }

    @Nullable
    @Override
    public Object getIngredientUnderMouse(@Nonnull BlockGUI guiContainer, int mouseX, int mouseY) {
        return null;
    }
}
