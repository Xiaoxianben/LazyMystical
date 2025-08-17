package com.xiaoxianben.lazymystical.init;

import com.xiaoxianben.lazymystical.config.ConfigLoader;
import com.xiaoxianben.lazymystical.init.register.EnumModRegister;
import com.xiaoxianben.lazymystical.item.ItemAccelerantGel;
import net.minecraft.item.Item;

import java.util.Collection;
import java.util.EnumMap;
import java.util.function.Function;

public enum EnumItemType {
    AccelerantGel(int.class, ItemAccelerantGel::new),
    Component();


    /**
     * 存储输入类型（如 Integer.class, String.class）
     */
    public final Class<?> inputType;
    private final EnumMap<EnumModRegister, Item[]> mapItems = new EnumMap<>(EnumModRegister.class);
    /**
     * 使用 Object 作为输入，运行时转换
     */
    private final Function<Object, ? extends Item> fun;


    <input> EnumItemType(Class<input> inputType, Function<input, ? extends Item> fun) {
        this.inputType = inputType;
        this.fun = obj -> fun.apply((input) obj);
    }
    EnumItemType() {
        this(null, null);
    }


    public <input> Item create(input input) {
        if (inputType == null) {
            ConfigLoader.logger.error("misuse item create function, input: {}", input.toString(), new Throwable());
            return null;
        }
        try {
            return fun.apply(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setItems(EnumModRegister register, Item... items) {
        mapItems.put(register, items);
    }

    public Item[] getItems(EnumModRegister register) {
        return mapItems.get(register);
    }

    public Collection<Item[]> getAllItems() {
        return mapItems.values();
    }
}
