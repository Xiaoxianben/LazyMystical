package com.xiaoxianben.lazymystical.jei.recipeCategory;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.config.ConfigValue;
import com.xiaoxianben.lazymystical.init.EnumBlockType;
import com.xiaoxianben.lazymystical.jei.recipeWrapper.SeedCultivatorWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SeedCultivatorCategory implements IRecipeCategory<SeedCultivatorWrapper> {

    public static final String ID = LazyMystical.MOD_ID + ":seed_cultivator_category";
    public static final List<ItemStack> allAccelerantDirt = new ArrayList<>();
    public static final List<ItemStack> allAccelerator = new ArrayList<>();


    public final IDrawable background, icon, animation, slot;


    public SeedCultivatorCategory(IGuiHelper guiHelper) {
        ResourceLocation resourceLocation = new ResourceLocation(LazyMystical.MOD_ID, "textures/gui/1.png");

        this.background = guiHelper.createDrawable(resourceLocation, 38, 14, 92, 54);
        this.slot = guiHelper.createDrawable(resourceLocation, 7, 83, 18, 18);

        this.icon = guiHelper.drawableBuilder(new ResourceLocation(LazyMystical.MOD_ID, "textures/blocks/machine/seed_cultivator/1.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
        this.animation = guiHelper.createAnimatedDrawable(
                guiHelper.createDrawable(resourceLocation, 176, 0, 22, 16),
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
        this.animation.draw(minecraft, 25, 1);
        this.slot.draw(minecraft, 28, 18);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SeedCultivatorWrapper recipeWrapper, IIngredients ingredients) {

        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

        stacks.init(0, true, 0, 0); // 种子
        stacks.init(1, true, 0, 18); // drit
        stacks.init(6, true, 0, 36); // acceleratorBlock
        stacks.init(7, true, 28, 18); // root block
        stacks.init(2, false, 56, 0); // output ...
        stacks.init(3, false, 72, 0);
        stacks.init(4, false, 56, 17);
        stacks.init(5, false, 72, 17);

        stacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

        stacks.set(1, allAccelerantDirt);

        stacks.set(6, allAccelerator);
        stacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == 6) {
                tooltip.add("可选");
            }
        });

        if (ingredients.getInputs(VanillaTypes.ITEM).size() >= 2) {
            stacks.set(7, ingredients.getInputs(VanillaTypes.ITEM).get(1));
        }

        for (int i = 0; i < ingredients.getOutputs(VanillaTypes.ITEM).size(); i++) {
            stacks.set(i + 2, ingredients.getOutputs(VanillaTypes.ITEM).get(i));
        }

    }
}
