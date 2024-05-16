package com.xiaoxianben.lazymystical.event;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class ConfigLoader {

    public static String cultivatorCategory = "cultivator";


    public static int acceleratorSpeed;
    public static int seedProbability;
    public static int seedSpeed;
    public static float seedNumberMultiplier;
    public static int[] seedLevelMultiplier;
    public static double[] acceleratorLevelMultiplier;


    private static Configuration config;
    private static Logger logger;

    public static void preInitConfigLoader(@Nonnull FMLPreInitializationEvent event) {
        logger = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());

        //实例化了一个Configuration类,括号中填的是Forge推荐的配置文件位置,这个位置在游戏根目录的config文件夹下，
        //名为<modid>.cfg，这里就是bm.cfg。

        config.load();//读取配置
        load();
    }

    public static int addInt(String name, int defaultValue) {
        return addInt(Configuration.CATEGORY_GENERAL, name, defaultValue);
    }

    public static int addInt(String category, String name, int defaultValue) {
        int tempInt = config.getInt(name, category, defaultValue, 1, Integer.MAX_VALUE, I18n.format("config." + name + ".comment"));
        config.save();
        return tempInt;
    }

    public static float addFloat(String name, String category, float defaultValue) {
        return config.getFloat(name, category, defaultValue, 0.0f, Float.MAX_VALUE, I18n.format("config." + name + ".comment"));
    }

    public static int[] addIntLIst(String category, String name, int[] defaultValue) {
        String common = I18n.format("config." + name + ".comment");
        Property tempProperty = config.get(category, name, defaultValue, common, 0, Integer.MAX_VALUE, true, defaultValue.length);

        int[] returnIntLIst = tempProperty.getIntList();
        config.save();
        return returnIntLIst;
    }

    public static double[] addDoubleLIst(String category, String name, float[] defaultValue) {
        String common = I18n.format("config." + name + ".comment");
        double[] defaultValueDouble = new double[defaultValue.length];
        for (int i = 0; i < defaultValue.length; i++) {
            defaultValueDouble[i] = defaultValue[i];
        }
        Property tempProperty = config.get(category, name, defaultValueDouble, common, 0.0, Float.MAX_VALUE, true, defaultValue.length);

        double[] returnIntLIst = tempProperty.getDoubleList();
        config.save();
        return returnIntLIst;
    }

    public static void load() {
        logger.info("Started loading config.");

        acceleratorSpeed = addInt("acceleratorSpeed", 10);

        config.setCategoryComment(cultivatorCategory, I18n.format("category." + cultivatorCategory + ".comment"));
        seedProbability = addInt(cultivatorCategory, "seedProbability", 100);
        seedSpeed = addInt(cultivatorCategory, "seedSpeed", 2000);
        seedNumberMultiplier = addFloat(cultivatorCategory, "seedNumberMultiplier", 1.0f);
        seedLevelMultiplier = addIntLIst(cultivatorCategory, "seedLevelMultiplier", new int[]{1, 2, 3, 4, 5, 6});
        acceleratorLevelMultiplier = addDoubleLIst(cultivatorCategory, "acceleratorLevelMultiplier", new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f});

        config.save(); //保存配置
        //至于为什么要保存配置呢？这是因为当配置缺失（最常见的原因就是配置文件没有创建，
        //这常常发生在你第一次使用Mod的时候）的时候，这一句会将默认的配置保存下来。
        logger.info("Finished loading config."); //输出完成加载配置文件
    }

}
