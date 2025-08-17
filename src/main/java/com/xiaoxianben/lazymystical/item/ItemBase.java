package com.xiaoxianben.lazymystical.item;
import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.api.IHasModel;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;

public class ItemBase extends Item implements IHasModel {

    public ItemBase(String name) {
        super();
        setRegistryName(LazyMystical.MOD_ID, name);
        setUnlocalizedName(LazyMystical.MOD_ID+"-"+name);

        setCreativeTab(LazyMystical.tab);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (I18n.hasKey(this.getUnlocalizedName() + ".tooltip")) {
            tooltip.addAll(Arrays.asList(I18n.format(this.getUnlocalizedName() + ".tooltip").split("\n")));
        }
    }

    @Override
    public void registerModels() {
        LazyMystical.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
