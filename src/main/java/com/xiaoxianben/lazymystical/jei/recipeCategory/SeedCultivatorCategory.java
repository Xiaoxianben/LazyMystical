package com.xiaoxianben.lazymystical.jei.recipeCategory;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.config.ConfigValue;
import com.xiaoxianben.lazymystical.jei.recipeWrapper.SeedCultivatorWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class SeedCultivatorCategory implements IRecipeCategory<SeedCultivatorWrapper> {

    public static final String ID = LazyMystical.MOD_ID + ":seed_cultivator_category";

    public final IDrawable background, icon, animation;


    public SeedCultivatorCategory(IGuiHelper guiHelper) {
        int x = 0;
        if (Loader.isModLoaded("mysticalagradditions")) x = 14;
        this.background = guiHelper.createDrawable(new ResourceLocation(LazyMystical.MOD_ID, "textures/gui/1.png"), 38, 28, 100, 26 + x);

        this.icon = guiHelper.drawableBuilder(new ResourceLocation(LazyMystical.MOD_ID, "textures/blocks/machine/1.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
        this.animation = guiHelper.createAnimatedDrawable(
                guiHelper.createDrawable(new ResourceLocation(LazyMystical.MOD_ID, "textures/gui/1.png"), 176, 0, 22, 16),
                20 * ConfigValue.seedSpeed,
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
        return I18n.format("category.seed_cultivator.title");
    }

    @Nonnull
    @Override
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
        this.animation.draw(minecraft, 25, 5);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SeedCultivatorWrapper recipeWrapper, IIngredients ingredients) {

        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

        stacks.init(0, true, 0, 4);
        stacks.init(1, false, 60, 4);
        stacks.init(2, false, 82, 4);

        if (ingredients.getInputs(VanillaTypes.ITEM).size() >= 2) {
            stacks.init(3, true, 0, 22);
            stacks.set(3, ingredients.getInputs(VanillaTypes.ITEM).get(1));
        }

        stacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

        stacks.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        stacks.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(1));

    }
}
