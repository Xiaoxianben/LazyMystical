package com.xiaoxianben.lazymystical.item.compomemt;

public enum EnumTypeComponent {
    Speed("speed", 10),
    NoLight("no_light", 1);


    public final String type;
    public final int insertNumber;

    EnumTypeComponent(String type, int insertNumber) {
        this.type = type;
        this.insertNumber = insertNumber;
    }
}
