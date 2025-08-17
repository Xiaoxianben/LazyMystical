package com.xiaoxianben.lazymystical.gui.container;


import com.xiaoxianben.lazymystical.gui.slot.SlotSeedCultivatorItemHandler;
import com.xiaoxianben.lazymystical.tileEntity.TESeedCultivator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSeedCultivator extends ContainerBase<TESeedCultivator> {

    public ContainerSeedCultivator(EntityPlayer player, TESeedCultivator tileEntity) {
        super(player, tileEntity);
        inputSlotCount = 3;


        // 将容器格子添加进入容器
        this.addSlotToContainer(new SlotSeedCultivatorItemHandler(tileEntity.seedCultivatorItemHandler, 0, 39, 15));
        this.addSlotToContainer(new SlotSeedCultivatorItemHandler(tileEntity.seedCultivatorItemHandler, 1, 39, 15 + 18));
        this.addSlotToContainer(new SlotSeedCultivatorItemHandler(tileEntity.seedCultivatorItemHandler, 2, 39, 15 + 18 * 2));
        if (tileEntity.blockLevel >= 6) {
            this.addSlotToContainer(new SlotSeedCultivatorItemHandler(tileEntity.seedCultivatorItemHandler, 7, 67, 33));
            ++inputSlotCount;
        }

        this.addSlotToContainer(new SlotItemHandler(tileEntity.seedCultivatorItemHandler, 3, 95, 15));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.seedCultivatorItemHandler, 4, 95 + 16, 15));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.seedCultivatorItemHandler, 5, 95, 15 + 16));
        this.addSlotToContainer(new SlotItemHandler(tileEntity.seedCultivatorItemHandler, 6, 95 + 16, 15 + 16));

    }
}
