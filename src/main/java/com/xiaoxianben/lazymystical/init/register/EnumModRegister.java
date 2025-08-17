package com.xiaoxianben.lazymystical.init.register;

import com.xiaoxianben.lazymystical.config.ConfigLoader;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nullable;

public enum EnumModRegister {
    MINECRAFT("minecraft", MinecraftRegister.class),
    AGRADDITIONS("mysticalagradditions", AgradditionsRegister.class);

    private final String modIds;
    private boolean isEnable = false;
    private Object register;

    EnumModRegister(String modId, Class<? extends IModRegister> register) {
        this.modIds = modId;
        this.register = register;
    }

    public void setEnable() {
        isEnable = Loader.isModLoaded(modIds);
        if (isEnable) {
            ConfigLoader.logger.info("the {} extension is loading.", modIds);
            if (this.register instanceof Class) {
                try {
                    this.register = ((Class<?>) this.register).newInstance();
                } catch (Exception e) {
                    ConfigLoader.logger.info("the {} extension isn't loaded.", modIds);
                }
            }
        }
    }

    @Nullable
    public IModRegister getRegister() {
        try {
            return isEnable ? (IModRegister) this.register : null;
        } catch (Exception e) {
            return null;
        }
    }

    public void preInit() {
        IModRegister register1 = getRegister();
        if (register1 != null) {
            register1.preInit();
        }
    }
    public void init() {
        IModRegister register1 = getRegister();
        if (register1 != null) {
            register1.init();
        }
    }
    public void posInit() {
        IModRegister register1 = getRegister();
        if (register1 != null) {
            register1.posInit();
            ConfigLoader.logger.info("the {} extension has been loaded.", modIds);
        }
        register = null;
    }

}
