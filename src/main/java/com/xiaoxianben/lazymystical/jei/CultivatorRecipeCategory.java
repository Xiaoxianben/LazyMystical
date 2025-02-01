package com.xiaoxianben.lazymystical.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.config.ConfigValue;
import com.xiaoxianben.lazymystical.manager.SeedManager;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ForgeI18n;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CultivatorRecipeCategory implements IRecipeCategory<CultivatorRecipeCategory.CultivatorRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(LazyMystical.MODID, "seedcultivator");


    public final IDrawable background, icon, animation;


    public CultivatorRecipeCategory(IGuiHelper guiHelper) {
        int x = 0;
        if (ModList.get().isLoaded("mysticalagradditions")) x = 14;
        this.background = guiHelper.createDrawable(new ResourceLocation(LazyMystical.MODID, "textures/gui/1.png"), 38, 28, 100, 26 + x);

        this.icon = guiHelper.drawableBuilder(new ResourceLocation(LazyMystical.MODID, "textures/blocks/seed_cultivator/1.png"), 0, 0, 16, 16).setTextureSize(16, 16).build();
        this.animation = guiHelper.createAnimatedDrawable(
                guiHelper.createDrawable(new ResourceLocation(LazyMystical.MODID, "textures/gui/1.png"), 176, 0, 22, 16),
                20 * ConfigValue.seedSpeed,
                IDrawableAnimated.StartDirection.LEFT, false
        );

    }


    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends CultivatorRecipeCategory.CultivatorRecipe> getRecipeClass() {
        return CultivatorRecipeCategory.CultivatorRecipe.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return ForgeI18n.getPattern("category.seed_cultivator.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void draw(@Nonnull CultivatorRecipe recipe, @Nonnull MatrixStack stack, double mouseX, double mouseY) {
        this.animation.draw(stack, 25, 5);
    }

    @Override
    public void setIngredients(CultivatorRecipe cultivatorRecipe, @Nonnull IIngredients ingredients) {
        List<ItemStack> inputStack = new ArrayList<>();
        inputStack.add(cultivatorRecipe.seed.getDefaultInstance());
        if (cultivatorRecipe.resultItem.getCrux() != null) {
            inputStack.add(cultivatorRecipe.resultItem.getCrux().asItem().getDefaultInstance());
        }
        ingredients.setInputs(VanillaTypes.ITEM, inputStack);

        List<List<ItemStack>> outputStack = new ArrayList<>();
        outputStack.add(Collections.singletonList(cultivatorRecipe.resultItem.getResultItem()));
        List<ItemStack> otherResults = cultivatorRecipe.resultItem.getOtherResults();
        otherResults.add(cultivatorRecipe.seed.getDefaultInstance());
        outputStack.add(otherResults);
        ingredients.setOutputLists(VanillaTypes.ITEM, outputStack);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @Nonnull CultivatorRecipe cultivatorRecipe, @Nonnull IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);

        stacks.init(0, true, 0, 4);
        stacks.init(1, false, 60, 4);
        stacks.init(2, false, 82, 4);

        if (cultivatorRecipe.resultItem.getCrux() != null) {
            stacks.init(3, true, 0, 22);
            stacks.set(3, inputs.get(1));
        }
        stacks.set(0, inputs.get(0));

        stacks.set(1, outputs.get(0));
        stacks.set(2, outputs.get(1));
    }

    public static class CultivatorRecipe {

        private final Item seed;
        private final SeedManager.seedResultItem resultItem;

        public CultivatorRecipe(Map.Entry<Item, SeedManager.seedResultItem> entry) {
            this.seed = entry.getKey();
            this.resultItem = entry.getValue();
        }
    }
}
