package com.xiaoxianben.lazymystical.otherModInit;

import com.xiaoxianben.lazymystical.api.IModInit;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

public class OtherInit implements IModInit {

    private final List<IOtherModInit> OtherModInit = new ArrayList<>();

    @Override
    public void preInit() {
        if (Loader.isModLoaded("mysticalagradditions")) {
            OtherModInit.add(new MysticalAgradditionsInit());
        }

        OtherModInit.forEach(IOtherModInit::initBlocks);
        OtherModInit.forEach(IOtherModInit::initItems);
    }

    @Override
    public void init() {
        OtherModInit.forEach(IOtherModInit::initOre);
        OtherModInit.forEach(IOtherModInit::initRecipes);
    }

    @Override
    public void postInit() {

    }
}
