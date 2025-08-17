package com.xiaoxianben.lazymystical.jei.recipeCategory;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.jei.recipeWrapper.AccelerantFluidWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AccelerantFluidCategory implements IRecipeCategory<AccelerantFluidWrapper> {

    public final static String ID = LazyMystical.MOD_ID + ":accelerant_fluid_category";


    public final IDrawable background, icon, animation;


    public AccelerantFluidCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(LazyMystical.MOD_ID, "textures/gui/2.png"), 38, 13, 74, 60);

        this.icon = guiHelper.drawableBuilder(new ResourceLocation(LazyMystical.MOD_ID, "textures/blocks/machine/accelerant_fluid/1.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
        this.animation = guiHelper.createAnimatedDrawable(
                guiHelper.createDrawable(new ResourceLocation(LazyMystical.MOD_ID, "textures/gui/2.png"), 176, 0, 22, 16),
                20,
                IDrawableAnimated.StartDirection.LEFT, false
        );
    }


    @Nonnull
    @Override
    public String getUid() {
        return ID;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format("category.accelerant_fluid.title");
    }

    @Override
    @Nonnull
    public String getModName() {
        return LazyMystical.MOD_ID;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        this.animation.draw(minecraft, 25, 20);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AccelerantFluidWrapper recipeWrapper, IIngredients ingredients) {

        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();

        stacks.init(0, true, 0, 19);
        fluidStacks.init(0, false, 57, 1, 16, 58, recipeWrapper.outputFluidStack.amount, true, null);

        stacks.set(0, recipeWrapper.inputItemStack);
        fluidStacks.set(0, recipeWrapper.outputFluidStack);
    }
}
