package com.xiaoxianben.lazymystical.event;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class ConfigLoader {
    private static Configuration config;

    private static Logger logger;

    public static int acceleratorSpeed;


    public static void preInitConfigLoader(@Nonnull FMLPreInitializationEvent event) {
        logger = event.getModLog();
        config = new Configuration(event.getSuggestedConfigurationFile());

        //实例化了一个Configuration类,括号中填的是Forge推荐的配置文件位置,这个位置在游戏根目录的config文件夹下，
        //名为<modid>.cfg，这里就是bm.cfg。

        config.load();//读取配置
        load();
    }

    public static int addInt(String name, int defaultValue) {
        return addInt(name, defaultValue, "config." + name + ".comment");
    }

    public static int addInt(String name, int defaultValue, String common) {
        int tempInt = config.getInt(name, Configuration.CATEGORY_GENERAL, defaultValue, 1, Integer.MAX_VALUE, I18n.format(common));
        config.save();
        return tempInt;
    }

    public static long addLong(String name, long defaultValue) {
        return addLong(name, defaultValue, I18n.format("config." + name + ".comment"));
    }

    public static long addLong(String name, long defaultValue, String common) {
        Property tempProperty = config.get(Configuration.CATEGORY_GENERAL, name, Long.toString(defaultValue), common, Property.Type.INTEGER);
        tempProperty.setMinValue(0);
        tempProperty.setMaxValue(Long.MAX_VALUE);
        tempProperty.setComment(tempProperty.getComment() + " [range: 0 ~ " + Long.MAX_VALUE + ", default: " + defaultValue + "]");

        long returnLong = tempProperty.getLong();
        config.save();
        return returnLong;
    }

    public static void load() {
        logger.info("Started loading config.");

        acceleratorSpeed = addInt("acceleratorSpeed", 10);
        //forge配置文件中会有多个类别，forge提供了general(Configuration.CATEGORY_GENERAL)
        //get函数的第一个参数就是表示general类型
        //get的第二个参数就是配置文件中的键的名称(难以看懂)
        //get的第三个参数就是键的默认值(默认100),如果该键不存在，返回默认值
        //get的第四个参数是该键的注释，就是获取basicAmountOfFluidToProduceEnergy相对应的值,getInt函数的作用就是获取整数（配置文件里面键的值一定是字符串）
        //从这里阔以看出get就是为了获取diamondBurnTime的值

        config.save(); //保存配置
        //至于为什么要保存配置呢？这是因为当配置缺失（最常见的原因就是配置文件没有创建，
        //这常常发生在你第一次使用Mod的时候）的时候，这一句会将默认的配置保存下来。
        logger.info("Finished loading config."); //输出完成加载配置文件
    }

    public static Logger logger() {
        return logger;
    }

}
