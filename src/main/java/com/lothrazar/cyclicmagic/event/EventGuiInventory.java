package com.lothrazar.cyclicmagic.event;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.gui.GuiPlayerExtended;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonCrafting;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonInventory;
import com.lothrazar.cyclicmagic.net.PacketOpenExtendedInventory;
import com.lothrazar.cyclicmagic.net.PacketOpenNormalInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventGuiInventory {

	final int buttonId = 55;

	@SideOnly(value = Side.CLIENT)
	@SubscribeEvent
	public void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event) {

		
		if (event.getGui() instanceof GuiInventory || event.getGui() instanceof GuiPlayerExtended) {
			GuiContainer gui = (GuiContainer) event.getGui();

			// TODO: reflection helper?
			// gui left and top are private, so are the sizes
			// int guiLeft = ;//gui.guiLeft
			// int guiTop = ;//gui.guiTop

			int xSize = 176;
			int ySize = 166;
			int guiLeft = (gui.width - xSize) / 2;
			int guiTop = (gui.height - ySize) / 2;
			int x = 30 + guiLeft;
			int y = guiTop + 2;
			event.getButtonList().add(new GuiButtonInventory(buttonId, x, y));
		

			if (event.getGui() instanceof GuiInventory) {

				event.getButtonList().add(new GuiButtonCrafting( x - 12, y));

			}
		}
	}

	@SideOnly(value = Side.CLIENT)
	@SubscribeEvent
	public void guiPostAction(GuiScreenEvent.ActionPerformedEvent.Post event) {

		if (event.getGui() instanceof GuiInventory) {
			if (event.getButton().id == buttonId) {
				ModMain.network.sendToServer(new PacketOpenExtendedInventory(event.getGui().mc.thePlayer));
			}
		}

		if (event.getGui() instanceof GuiPlayerExtended) {
			if (event.getButton().id == buttonId) {
				event.getGui().mc.displayGuiScreen(new GuiInventory(event.getGui().mc.thePlayer));
				ModMain.network.sendToServer(new PacketOpenNormalInventory(event.getGui().mc.thePlayer));
			}
		}
	}
}