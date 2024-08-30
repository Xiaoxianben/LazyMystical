package com.xiaoxianben.lazymystical.init;

import net.minecraftforge.fml.common.Loader;

import java.util.Objects;

public enum EnumBlockLevel {
    v1(1, null),
    v2(2, null),
    v3(3, null),
    v4(4, null),
    v5(5, null),
    v6(6, "mysticalagradditions");


    private static int[] leveSpiltNumber = null;
    private static int enableNumber = 0;
    private final int level;
    private final String modId;
    private boolean enable = true;

    EnumBlockLevel(int level, String modId) {
        this.level = level;
        this.modId = modId;
    }

    public static int[] getLeveSpiltNumber() {
        if (leveSpiltNumber == null) {
            leveSpiltNumber = new int[2];

            int i = 0;
            String NowModId = null;
            for (EnumBlockLevel value : values()) {
                if (!Objects.equals(NowModId, value.getModId())) {
                    NowModId = value.getModId();
                    i++;
                }
                if (value.isEnable()) {
                    leveSpiltNumber[i] += 1;
                }
            }
        }
        return leveSpiltNumber;
    }

    public static int enableNumber() {
        if (enableNumber == 0) {
            for (EnumBlockLevel value : values()) {
                if (value.isEnable()) {
                    enableNumber += 1;
                }
            }
        }
        return enableNumber;
    }

    public String getModId() {
        return modId;
    }

    public void setEnable() {
        if (this.modId != null) {
            enable = Loader.isModLoaded(this.modId);
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public int getLevel() {
        return level;
    }

}
