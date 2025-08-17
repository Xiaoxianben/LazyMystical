package com.xiaoxianben.lazymystical.tileEntity.itemHandler;

import com.xiaoxianben.lazymystical.item.compomemt.EnumTypeComponent;
import com.xiaoxianben.lazymystical.item.compomemt.ItemComponent;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 组件物品处理器，不建议将他作为{@link net.minecraftforge.items.CapabilityItemHandler#ITEM_HANDLER_CAPABILITY} 的返回，ta应该只有玩家交互和gui界面修改.
 * slot0 是 tempSlot, 只在Gui中作存储component的slot，并在Gui界面关闭时弹出
 */
public class ItemHandlerComponent extends ItemHandlerBase {

    private final List<EnumTypeComponent> types;


    public ItemHandlerComponent(EnumTypeComponent... types) {
        super(types.length + 1);
        this.types = Collections.unmodifiableList(Arrays.asList(types));
    }


    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        if (slot > 0 && slot <= types.size()) {
            return types.get(slot - 1).insertNumber;
        }
        return super.getStackLimit(slot, stack);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if (slot == 0) {
            return stack.getItem() instanceof ItemComponent && types.contains(((ItemComponent) stack.getItem()).typeComponent);
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slot) {
        return slot == 0;
    }


    /**
     * 插入一个物品组件, 要求 stack.item instanceof ItemComponent
     * @return 插入stack后，剩余的stack
     */
    @Nonnull
    public ItemStack insertComponent(ItemStack stack, boolean simulate) {
        if (stack.getItem() instanceof ItemComponent) {
            EnumTypeComponent typeComponent = ((ItemComponent) stack.getItem()).typeComponent;
            int indexOf = types.indexOf(typeComponent);

            if (indexOf == -1) {
                return stack;
            }

            return insertItemInternal(indexOf + 1, stack, simulate);
        }
        return stack;
    }

    public void insertTempSlotComponent() {
        ItemStack stackInSlot = getStackInSlot(0);
        setStackInSlot(0, insertComponent(stackInSlot, false));
    }

    /**
     * 提取一个物品组件, 要求 stack.item instanceof ItemComponent
     * @return 提取stack
     */
    @Nonnull
    public ItemStack extractComponent(ItemStack stack, boolean simulate) {
        if (stack.getItem() instanceof ItemComponent) {
            EnumTypeComponent typeComponent = ((ItemComponent) stack.getItem()).typeComponent;
            int indexOf = types.indexOf(typeComponent);

            if (indexOf == -1) {
                return ItemStack.EMPTY;
            }

            return extractItemInternal(indexOf + 1, stack.getCount(), simulate);
        }
        return ItemStack.EMPTY;
    }

    public int getComponentCount(EnumTypeComponent typeComponent) {
        int indexOf = types.indexOf(typeComponent);

        return getComponent(indexOf).getCount();
    }

    public int getComponentCountAndLevel(EnumTypeComponent typeComponent) {
        int indexOf = types.indexOf(typeComponent);
        ItemStack component = getComponent(indexOf);
        if (component.isEmpty()) {
            return 0;
        }

        return component.getCount() * ((ItemComponent) component.getItem()).level;
    }

    protected ItemStack getComponent(int index) {
        if (index < 0 || index >= types.size() - 1) {
            return ItemStack.EMPTY;
        }
        return getStackInSlot(index + 1);
    }
}
