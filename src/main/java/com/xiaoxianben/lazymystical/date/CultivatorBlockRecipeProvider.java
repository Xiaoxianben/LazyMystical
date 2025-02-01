package com.xiaoxianben.lazymystical.date;

import com.blakebr0.mysticalagradditions.MysticalAgradditions;
import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.registry.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class CultivatorBlockRecipeProvider extends ForgeRecipeProvider {
    public CultivatorBlockRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        final String[] ingotOres = new String[]{MysticalAgriculture.MOD_ID + ":inferium_ingot_block",
                MysticalAgriculture.MOD_ID + ":prudentium_ingot_block",
                MysticalAgriculture.MOD_ID + ":tertium_ingot_block",
                MysticalAgriculture.MOD_ID + ":imperium_ingot_block",
                MysticalAgriculture.MOD_ID + ":supremium_ingot_block",
                MysticalAgradditions.MOD_ID + ":insanium_ingot_block"};
        for (int i = 0; i < BlockRegistry.SEED_CULTIVATOR.size(); i++) {
            ShapedRecipeBuilder.shaped(BlockRegistry.SEED_CULTIVATOR.get(i).get())
                    .pattern("EBE")
                    .pattern("BXB")
                    .pattern("EBE")
                    .define('X', i == 0 ? ModBlocks.MACHINE_FRAME.get() : BlockRegistry.SEED_CULTIVATOR.get(i - 1).get())
                    .define('B', Registry.ITEM.get(new ResourceLocation(ingotOres[i])))
                    .define('E', Registry.ITEM.get(new ResourceLocation(ingotOres[i].replace("_ingot", ""))))
                    .unlockedBy("", inventoryTrigger())
                    .save(consumer);
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return LazyMystical.NAME + " recipe generator";
    }
}
