package com.xiaoxianben.lazymystical.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ModConfigs {

    protected static String cultivatorCategory = "cultivator";

    public static ForgeConfigSpec config;


    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();


        builder.comment(I18n.translateToLocal("category." + cultivatorCategory + ".comment")).push(cultivatorCategory);
        ConfigValue.seedProbabilityValue = addInt(builder, "seedProbability", 100);
        ConfigValue.seedSpeedValue = addInt(builder, "seedSpeed", 2000);
        ConfigValue.seedNumberMultiplierValue = addFloat(builder, "seedNumberMultiplier", 1.0f);
        ConfigValue.seedLevelMultiplierValue = addLIst(builder, "seedLevelMultiplier", Arrays.asList(1, 2, 3, 4, 5, 6));
        ConfigValue.acceleratorLevelMultiplierValue = addLIst(builder, "acceleratorLevelMultiplier", Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0));
        builder.pop();


        config = builder.build();
    }


    public static ForgeConfigSpec.IntValue addInt(ForgeConfigSpec.Builder builder, String name, int defaultValue) {
        String key = "config." + name + ".comment";
        return builder.comment(I18n.translateToLocal(key)).translation(key).defineInRange(name, defaultValue, 0, Integer.MAX_VALUE);
    }

    public static ForgeConfigSpec.DoubleValue addFloat(ForgeConfigSpec.Builder builder, String name, float defaultValue) {
        String key = "config." + name + ".comment";
        return builder.comment(I18n.translateToLocal(key)).translation(key).defineInRange(name, defaultValue, 0, Float.MAX_VALUE);
    }

    public static <list> ForgeConfigSpec.ConfigValue<List<list>> addLIst(ForgeConfigSpec.Builder builder, String name, List<list> defaultValue) {
        String key = "config." + name + ".comment";
        return builder.comment(I18n.translateToLocal(key)).translation(key).define(name, defaultValue);
    }

    public static File getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get().toFile();
    }
}
