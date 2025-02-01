package com.xiaoxianben.lazymystical.date;

import com.blakebr0.mysticalagradditions.MysticalAgradditions;
import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.registry.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class AcceleratorRecipeProvider extends ForgeRecipeProvider {
    public AcceleratorRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(BlockRegistry.GROWTH_ACCELERATOR_6.get())
                .pattern("ESE")
                .pattern("SDS")
                .pattern("ESE")
                .define('E', Registry.ITEM.get(new ResourceLocation(MysticalAgradditions.MOD_ID, "insanium_essence")))
                .define('S', Tags.Items.STONE)
                .define('D', Registry.ITEM.get(new ResourceLocation(MysticalAgradditions.MOD_ID, "insanium_gemstone")))
                .unlockedBy("", inventoryTrigger())
                .save(consumer);
    }

    @Nonnull
    @Override
    public String getName() {
        return LazyMystical.NAME + " recipe generator 2";
    }
}
