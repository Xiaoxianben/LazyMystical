package com.xiaoxianben.lazymystical.config;

import com.xiaoxianben.lazymystical.LazyMystical;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;

public class ConfigLoader {

    public static String recipeDir;
    public static Logger logger;
    protected static final String cultivatorCategory = "cultivator";


    private final Configuration config;


    public ConfigLoader(@Nonnull FMLPreInitializationEvent event) {
        logger = event.getModLog();
        recipeDir = event.getModConfigurationDirectory() + "/" + LazyMystical.MOD_ID;
        config = new Configuration(new File(recipeDir + "/" + LazyMystical.MOD_ID + ".cfg"));

        //实例化了一个Configuration类,括号中填的是Forge推荐的配置文件位置,这个位置在游戏根目录的config文件夹下，
        //名为<modid>.cfg，这里就是bm.cfg。

        config.load();//读取配置
    }


    public int addInt(String category, String name, int defaultValue) {
        int tempInt = config.getInt(name, category, defaultValue, 1, Integer.MAX_VALUE, I18n.translateToLocal("config." + name + ".comment"));
        config.save();
        return tempInt;
    }

    public float addFloat(String category, String name, float defaultValue) {
        return config.getFloat(name, category, defaultValue, 0.0f, Float.MAX_VALUE, I18n.translateToLocal("config." + name + ".comment"));
    }

    public int[] addIntLIst(String category, String name, int[] defaultValue) {
        String common = I18n.translateToLocal("config." + name + ".comment");
        Property tempProperty = config.get(category, name, defaultValue, common, 0, Integer.MAX_VALUE, true, defaultValue.length);

        int[] returnIntLIst = tempProperty.getIntList();
        config.save();
        return returnIntLIst;
    }

    public double[] addDoubleLIst(String category, String name, double[] defaultValue) {
        String common = I18n.translateToLocal("config." + name + ".comment");

        Property tempProperty = config.get(category, name, defaultValue, common, 0.0, Float.MAX_VALUE, true, defaultValue.length);

        double[] returnIntLIst = tempProperty.getDoubleList();
        config.save();
        return returnIntLIst;
    }

    public void load() {
        logger.info("Started loading config.");

        config.setCategoryComment(cultivatorCategory, I18n.translateToLocal("category." + cultivatorCategory + ".comment"));

        ConfigValue.acceleratorSpeed = addInt(Configuration.CATEGORY_GENERAL, "acceleratorSpeed", 10);

        ConfigValue.seedProbability = addInt(cultivatorCategory, "seedProbability", 100);
        ConfigValue.seedSpeed = addInt(cultivatorCategory, "seedSpeed", 2000);
        ConfigValue.seedNumberMultiplier = addFloat(cultivatorCategory, "seedNumberMultiplier", 1.0f);
        ConfigValue.seedLevelMultiplier = addIntLIst(cultivatorCategory, "seedLevelMultiplier", new int[]{1, 2, 3, 4, 5, 6});
        ConfigValue.acceleratorLevelMultiplier = addDoubleLIst(cultivatorCategory, "acceleratorLevelMultiplier", new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0});

        config.save(); //保存配置
        //至于为什么要保存配置呢？这是因为当配置缺失（最常见的原因就是配置文件没有创建，
        //这常常发生在你第一次使用Mod的时候）的时候，这一句会将默认的配置保存下来。
        logger.info("Finished loading config."); //输出完成加载配置文件
    }

}
