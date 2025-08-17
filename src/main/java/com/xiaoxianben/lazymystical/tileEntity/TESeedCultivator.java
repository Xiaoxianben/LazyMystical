package com.xiaoxianben.lazymystical.tileEntity;

import com.xiaoxianben.lazymystical.api.IHasHandlerComponent;
import com.xiaoxianben.lazymystical.config.ConfigValue;
import com.xiaoxianben.lazymystical.item.compomemt.EnumTypeComponent;
import com.xiaoxianben.lazymystical.manager.seedManagr.SeedManager;
import com.xiaoxianben.lazymystical.tileEntity.itemHandler.ItemHandlerComponent;
import com.xiaoxianben.lazymystical.tileEntity.itemHandler.SeedCultivatorItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class TESeedCultivator extends TEBase implements IHasHandlerComponent {

    public final SeedCultivatorItemHandler seedCultivatorItemHandler;
    public final ItemHandlerComponent itemHandlerComponent;

    /**
     * 当前运行中的时间，-1则没有运行
     */
    public int timeRun = -1;
    /**
     * 运行中的总时间，0则没有运行
     */
    public int maxTimeRun = 0;


    public TESeedCultivator() {
        super();
        seedCultivatorItemHandler = new SeedCultivatorItemHandler(this);
        itemHandlerComponent = new ItemHandlerComponent(EnumTypeComponent.Speed, EnumTypeComponent.NoLight);
    }


    @Override
    protected void updateInSever() {

        for (int i = -1; i < getHandlerComponent().getComponentCountAndLevel(EnumTypeComponent.Speed); i++) {
            // 判断 是否可以运行
            if (!canRun()) {
                updateThis();
                break;
            }

            runMachine();
        }

    }

    @Override
    protected void updateInClient() {

    }


    @ParametersAreNonnullByDefault
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(seedCultivatorItemHandler);
        }
        return super.getCapability(capability, facing);
    }

    // NBT
    @ParametersAreNonnullByDefault
    public void readFromNBT(NBTTagCompound compound) {
        // 读取相关数据到 实体 中
        super.readFromNBT(compound);

        seedCultivatorItemHandler.deserializeNBT(compound.getCompoundTag("seedCultivatorItemHandler"));
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // 将相关数据保存到 compound 中
        compound = super.writeToNBT(compound);

        compound.setTag("seedCultivatorItemHandler", seedCultivatorItemHandler.serializeNBT());

        return compound;
    }

    @Override
    public void handleNetworkUpdateNbt(NBTTagCompound NBT) {
        this.timeRun = NBT.getInteger("timeRun");
        this.maxTimeRun = NBT.getInteger("maxTimeRun");
    }

    @Nonnull
    @Override
    public NBTTagCompound getNetworkUpdateNbt() {
        NBTTagCompound NBT = new NBTTagCompound();
        NBT.setInteger("timeRun", timeRun);
        NBT.setInteger("maxTimeRun", maxTimeRun);

        return NBT;
    }

    @Nonnull
    @Override
    public ItemHandlerComponent getHandlerComponent() {
        return itemHandlerComponent;
    }


    /**
     * 更新还在运行中的自身，如果改变了生长的总时间，则重新改变运行状态。
     */
    public void updateThis() {
        if (this.maxTimeRun > 0 && this.maxTimeRun != this.seedCultivatorItemHandler.getMaxTimeRun()) {
            updateThisTime();
        }
    }

    /**
     * 重置时间, 同时同步客户端
     */
    protected void updateThisTime() {
        if (canRun()) {
            this.maxTimeRun = this.seedCultivatorItemHandler.getMaxTimeRun();
            this.timeRun = this.maxTimeRun;
        } else {
            this.timeRun = -1;
            this.maxTimeRun = 0;
        }
        this.sendUpdatePacket();
    }

    protected int getLight() {
        int componentCountAndLevel = getHandlerComponent().getComponentCountAndLevel(EnumTypeComponent.NoLight);
        if (componentCountAndLevel > 0) {
            return componentCountAndLevel * 15;
        }

        int light = 0;
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            light = Math.max(light, this.getWorld().getLight(this.getPos().offset(facing)));
        }
        return light - 1;
    }

    public boolean canRun() {
//        int light = this.getLight();
//        if (light < 10 || getHandlerComponent().getComponentCount(EnumTypeComponent.NoLight) > 0) {
//            return false;
//        }

        ItemStack seedItemStack = this.seedCultivatorItemHandler.getSeedItemStack();
        SeedCultivatorItemHandler.CultivationRecipe nowRecipe = this.seedCultivatorItemHandler.getNowRecipe();
        if (nowRecipe == null || seedItemStack.isEmpty()) {
            return false;
        }
        if (this.seedCultivatorItemHandler.getEffectiveSeedCount() <= 0) return false;
        if (!this.seedCultivatorItemHandler.insertResultItem(nowRecipe.recipeOutput, true).isEmpty()) {
            return false;
        }
        if (SeedManager.isTier6Seed(nowRecipe.recipeInput)) {
            return !seedCultivatorItemHandler.getRootBlockItemStack().isEmpty() &&
                    SeedManager.getRootBlockMeta(nowRecipe.recipeInput) == seedCultivatorItemHandler.getRootBlockItemStack().getMetadata();
        }
        return true;
    }

    protected void runMachine() {
        // 判断是否正在运行
        if (this.timeRun > 0) {
            if (this.getLight() < 10) {
                return;
            }
            this.timeRun -= (1 + this.seedCultivatorItemHandler.getAllBlockLevel());
            if (this.timeRun <= 0) {
                this.timeRun = 0;
            } else {
                this.sendUpdatePacket();
            }
        }

        switch (this.timeRun) {
            case 0:
                // 判断是否运行完成
                SeedCultivatorItemHandler.CultivationRecipe recipe = seedCultivatorItemHandler.getNowRecipe();
                if (recipe != null) {
                    ItemStack itemEssenceStack = recipe.recipeOutput.copy();

                    int effectiveSeedCount = this.seedCultivatorItemHandler.getEffectiveSeedCount();
                    itemEssenceStack.setCount(effectiveSeedCount * itemEssenceStack.getCount());
                    this.seedCultivatorItemHandler.insertResultItem(itemEssenceStack, false);

                    if (recipe.recipeOutputOther.length > 0) {
                        for (int i = 0; i < (effectiveSeedCount); i++) {
                            if (ConfigValue.othersProbability <= 0) {
                                break;
                            }
                            if ((getWorld().rand.nextInt(ConfigValue.othersProbability) == 0)) {
                                this.seedCultivatorItemHandler.insertResultItem(recipe.recipeOutputOther[getWorld().rand.nextInt(recipe.recipeOutputOther.length)].copy(), false);
                            }
                        }
                    }
                    for (int i = 0; i < (effectiveSeedCount); i++) {
                        if (ConfigValue.seedProbability <= 0) {
                            break;
                        }
                        if ((getWorld().rand.nextInt(ConfigValue.seedProbability) == 0)) {
                            this.seedCultivatorItemHandler.insertResultItem(recipe.recipeInput, false);
                        }
                    }
                }
            case -1:
                // 判断是否初次运行
                this.updateThisTime();
        }
    }

}
