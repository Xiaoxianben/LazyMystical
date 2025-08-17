package com.xiaoxianben.lazymystical.item.compomemt;

import com.xiaoxianben.lazymystical.item.ItemBase;

public class ItemComponent extends ItemBase {

    public final EnumTypeComponent typeComponent;
    public final int level;


    public ItemComponent(EnumTypeComponent typeComponent, int level) {
        super("component_" + typeComponent.type);

        this.typeComponent = typeComponent;
        this.level = level;
    }

    public ItemComponent(EnumTypeComponent typeComponent) {
        this(typeComponent, 1);
    }

}
