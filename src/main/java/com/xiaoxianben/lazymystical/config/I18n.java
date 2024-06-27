package com.xiaoxianben.lazymystical.config;

public class I18n {

    public static String translateToLocal(String key) {
        if (net.minecraft.util.text.translation.I18n.canTranslate(key)) {
            return net.minecraft.util.text.translation.I18n.translateToLocal(key);
        } else {
            return net.minecraft.util.text.translation.I18n.translateToFallback(key);
        }
    }

}
