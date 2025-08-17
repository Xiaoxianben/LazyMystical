package com.xiaoxianben.lazymystical.gui.guiScreen;

import com.xiaoxianben.lazymystical.LazyMystical;
import com.xiaoxianben.lazymystical.api.ICanOpenGui;
import com.xiaoxianben.lazymystical.api.IHasHandlerComponent;
import com.xiaoxianben.lazymystical.gui.container.ContainerComponent;
import com.xiaoxianben.lazymystical.item.compomemt.ItemComponent;
import com.xiaoxianben.lazymystical.network.data.DataGuiButton;
import com.xiaoxianben.lazymystical.network.data.DataGuiChange;
import com.xiaoxianben.lazymystical.tileEntity.TEBase;
import com.xiaoxianben.lazymystical.tileEntity.itemHandler.ItemHandlerComponent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiComponent<TE extends TEBase & IHasHandlerComponent> extends GuiContainerBase<ContainerComponent<TE>> {

    private static final ResourceLocation TEXTURES = new ResourceLocation(LazyMystical.MOD_ID, "textures/gui/component.png");


    /**
     * 在实际ItemHandlerComponent中的slotIndex，即应该>0，若<=0, 则为非法index
     */
    public int indexComponent = 0;


    public GuiComponent(ContainerComponent<TE> inventorySlotsIn) {
        super(inventorySlotsIn);
    }


    @Override
    public void initGui() {
        super.initGui();

        addButton(new ButtonComponentIO(1, EnumTypeButtonComponentIO.Increase, this));
        addButton(new ButtonComponentIO(2, EnumTypeButtonComponentIO.Exclude, this));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            ICanOpenGui block = (ICanOpenGui) container.getTE().getBlockType();
            LazyMystical.getChannel().sendToServer(new DataGuiChange(block.getGuiId(), container.getTE().getPos()).toMessage());
        } else {
            LazyMystical.getChannel().sendToServer(new DataGuiButton(button.id, indexComponent).toMessage());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isPointInRegion(7, 5, 18, 18 * container.getTE().getHandlerComponent().getSlots(), mouseX, mouseY)) {
            indexComponent = (mouseY - guiTop - 5) / 18 + 1;
            if (!isTrueIndexComponent()) {
                indexComponent = 0;
            }
        }
    }

    @Override
    protected ResourceLocation getTextureResourceLocation() {
        return TEXTURES;
    }

    @Override
    protected void drawBackgroundTexture() {
        ItemHandlerComponent handlerComponent = container.getTE().getHandlerComponent();
        final int x = guiLeft + 7;

        for (int i = 1; i < handlerComponent.getSlots(); i++) {
            final int y = guiTop + 5 + (i - 1) * 18;

            if (isTrueIndexComponent() && i == indexComponent) {
                drawTexturedModalRect(x, y, 193, 0, 18, 18);
            } else {
                drawTexturedModalRect(x, y, 176, 0, 18, 18);
            }
        }
        for (int i = 1; i < handlerComponent.getSlots(); i++) {
            final int y = guiTop + 5 + (i - 1) * 18;

            this.itemRender.zLevel = 200.0F;
            this.itemRender.renderItemAndEffectIntoGUI(handlerComponent.getStackInSlot(i), x + 1, y + 1);
            this.itemRender.zLevel = 0;
        }
    }

    @Override
    protected void drawBackgroundString() {
        if (!isTrueIndexComponent()) {
            return;
        }

        final ItemStack inSlot = this.container.getTE().getHandlerComponent().getStackInSlot(indexComponent);
        if (inSlot.isEmpty()) {
            return;
        }

        final int fontHeight = this.fontRenderer.FONT_HEIGHT;
        final int x = guiLeft + 27;
        final int y = guiTop + 7;
        final ItemComponent item = (ItemComponent) inSlot.getItem();

        this.fontRenderer.drawString(inSlot.getDisplayName(), x, y, 0xff000000);

        List<String> list = new ArrayList<>(1);
        item.addInformation(inSlot, null, list, ITooltipFlag.TooltipFlags.NORMAL);
        this.fontRenderer.drawString(list.get(0), x, y + fontHeight, 0xff000000);

        String s = I18n.format("gui.count.string", inSlot.getCount(), item.typeComponent.insertNumber);
        this.fontRenderer.drawString(s, x, y + fontHeight * 2, 0xff000000);

        s = I18n.format("gui.level.string", item.level);
        this.fontRenderer.drawString(s, x, y + fontHeight * 3, 0xff000000);
    }

    @Override
    protected void drawForegroundTexture() {
    }

    @Override
    protected void drawForegroundString() {

    }

    @Override
    protected void drawHoveredTip(int mouseX, int mouseY) {

    }


    protected boolean isTrueIndexComponent() {
        return indexComponent > 0 && indexComponent < container.getTE().getHandlerComponent().getSlots();
    }


    static class ButtonComponentIO extends GuiButton {

        public ButtonComponentIO(int buttonId, EnumTypeButtonComponentIO type, GuiContainer container) {
            super(buttonId, container.getGuiLeft() + 154, container.getGuiTop() + 39 + type.id * 18, 18, 18, type.drawString);
        }
    }

    enum EnumTypeButtonComponentIO {
        Increase(0, "In"),
        Exclude(1, "Ex");


        public final int id;
        public final String drawString;

        EnumTypeButtonComponentIO(int id, String s) {
            this.id = id;
            this.drawString = s;
        }

    }

}
