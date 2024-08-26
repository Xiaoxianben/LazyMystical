package com.xiaoxianben.lazymystical.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ConfigValue {
    public static int seedProbability;
    public static int seedSpeed;
    public static double seedNumberMultiplier;
    public static List<Integer> seedLevelMultiplier;
    public static List<Double> acceleratorLevelMultiplier;


    protected static ForgeConfigSpec.IntValue seedProbabilityValue;
    protected static ForgeConfigSpec.IntValue seedSpeedValue;
    protected static ForgeConfigSpec.DoubleValue seedNumberMultiplierValue;
    protected static ForgeConfigSpec.ConfigValue<List<Integer>> seedLevelMultiplierValue;
    protected static ForgeConfigSpec.ConfigValue<List<Double>> acceleratorLevelMultiplierValue;

    public static void init() {
        seedProbability = seedProbabilityValue.get();
        seedSpeed = seedSpeedValue.get();
        seedNumberMultiplier = seedNumberMultiplierValue.get();
        seedLevelMultiplier = seedLevelMultiplierValue.get();
        acceleratorLevelMultiplier = acceleratorLevelMultiplierValue.get();
    }
}
