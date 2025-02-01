package com.xiaoxianben.lazymystical.jei;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.block.BlockSeedCultivator;
import com.xiaoxianben.lazymystical.gui.ScreenSeedCultivator;
import com.xiaoxianben.lazymystical.manager.SeedManager;
import com.xiaoxianben.lazymystical.registry.BlockRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(LazyMystical.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CultivatorRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        for (RegistryObject<BlockSeedCultivator> registryObject : BlockRegistry.SEED_CULTIVATOR) {
            registration.addRecipeCatalyst(registryObject.get().asItem().getDefaultInstance(), CultivatorRecipeCategory.UID);
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(SeedManager.recipes.entrySet().stream()
                        .map(CultivatorRecipeCategory.CultivatorRecipe::new)
                        .collect(Collectors.toList()),
                CultivatorRecipeCategory.UID);
    }

    public void registerGuiHandlers(@Nonnull IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(ScreenSeedCultivator.class, 63, 33, 22, 16, CultivatorRecipeCategory.UID);
    }
}
