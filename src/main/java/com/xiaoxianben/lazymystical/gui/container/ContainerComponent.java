package com.xiaoxianben.lazymystical.gui.container;

import com.xiaoxianben.lazymystical.api.IHasHandlerComponent;
import com.xiaoxianben.lazymystical.tileEntity.TEBase;
import com.xiaoxianben.lazymystical.tileEntity.itemHandler.ItemHandlerComponent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerComponent<TE extends TEBase & IHasHandlerComponent> extends ContainerBase<TE> {

    int indexComponent = -1;

    public ContainerComponent(EntityPlayer player, TE tileEntity) {
        super(player, tileEntity);

        this.addSlotToContainer(new SlotItemHandler(te.getHandlerComponent(), 0, 154, 6));
        ++inputSlotCount;
    }

    @Override
    public void handlerButtonClick(int buttonId, int... ints) {
        super.handlerButtonClick(buttonId, ints);

        switch (buttonId) {
            case 1: {
                ItemHandlerComponent handlerComponent = getTE().getHandlerComponent();
                handlerComponent.insertTempSlotComponent();
            }
            break;
            case 2: {
                indexComponent = ints[0];
                if (indexComponent <= -1) {
                    break;
                }
                ItemHandlerComponent handlerComponent = getTE().getHandlerComponent();
                ItemStack itemStack = handlerComponent.extractComponent(handlerComponent.getStackInSlot(indexComponent).copy(), false);
                World world = getTE().getWorld();
                BlockPos pos = getTE().getPos();

                mergeItemStack(itemStack, 0, 36, false);
                if (!itemStack.isEmpty()) {
                    world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemStack));
                }
            }
            break;
        }

    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (playerIn.world.isRemote){
            return;
        }

        ItemStack stackInSlot = getTE().getHandlerComponent().getStackInSlot(0).copy();
        getTE().getHandlerComponent().extractItemInternal(0, stackInSlot.getCount(), false);
        mergeItemStack(stackInSlot, 0, 36, false);

        if (!stackInSlot.isEmpty()) {
            World worldIn = playerIn.world;
            BlockPos pos = playerIn.getPosition();
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), stackInSlot.copy()));
        }
    }
}
