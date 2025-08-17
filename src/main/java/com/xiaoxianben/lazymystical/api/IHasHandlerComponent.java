package com.xiaoxianben.lazymystical.api;

import com.xiaoxianben.lazymystical.tileEntity.itemHandler.ItemHandlerComponent;

import javax.annotation.Nonnull;

public interface IHasHandlerComponent {
    @Nonnull
    ItemHandlerComponent getHandlerComponent();
}
