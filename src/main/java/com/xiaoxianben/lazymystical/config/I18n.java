package com.xiaoxianben.lazymystical.config;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonParseException;
import com.xiaoxianben.lazymystical.LazyMystical;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextProcessing;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.server.LanguageHook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public class I18n {

    public static final LanguageMap language = loadMap();

    private static LanguageMap loadMap() {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        BiConsumer<String, String> biconsumer = builder::put;

        try {
            InputStream inputstream = LanguageMap.class.getResourceAsStream("/assets/" + LazyMystical.MODID + "/lang/en_us.json");
            Throwable var3 = null;

            try {
                LanguageMap.loadFromJson(inputstream, biconsumer);
            } catch (Throwable var13) {
                var3 = var13;
                throw var13;
            } finally {
                if (inputstream != null) {
                    if (var3 != null) {
                        try {
                            inputstream.close();
                        } catch (Throwable var12) {
                            var3.addSuppressed(var12);
                        }
                    } else {
                        inputstream.close();
                    }
                }

            }
        } catch (IOException | JsonParseException ignored) {
        }

        final Map<String, String> map = new HashMap(builder.build());
        LanguageHook.captureLanguageMap(map);
        return new LanguageMap() {
            public Map<String, String> getLanguageData() {
                return map;
            }

            public String getOrDefault(String p_230503_1_) {
                return (String) map.getOrDefault(p_230503_1_, p_230503_1_);
            }

            public boolean has(String p_230506_1_) {
                return map.containsKey(p_230506_1_);
            }

            @OnlyIn(Dist.CLIENT)
            public boolean isDefaultRightToLeft() {
                return false;
            }

            @OnlyIn(Dist.CLIENT)
            public IReorderingProcessor getVisualOrder(ITextProperties p_241870_1_) {
                return (p_244262_1_) -> {
                    return p_241870_1_.visit((p_244261_1_, p_244261_2_) -> {
                        return TextProcessing.iterateFormatted(p_244261_2_, p_244261_1_, p_244262_1_) ? Optional.empty() : ITextProperties.STOP_ITERATION;
                    }, Style.EMPTY).isPresent();
                };
            }
        };
    }

    public static String translateToLocal(String key, Object... objects) {
        String s = language.getOrDefault(key);

        try {
            return String.format(s, objects);
        } catch (IllegalFormatException var4) {
            return "Format error: " + s;
        }
    }

}
