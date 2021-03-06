/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.component.pump.item;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiItemPump extends GuiBaseContainer {
  TileEntityItemPump te;
  private ButtonTileEntityField btn;
  public GuiItemPump(InventoryPlayer inventoryPlayer, TileEntityItemPump tileEntity) {
    super(new ContainerItemPump(inventoryPlayer, tileEntity), tileEntity);
    te = tileEntity;
    this.fieldRedstoneBtn = TileEntityItemPump.Fields.REDSTONE.ordinal();
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int j = 1; j < 10; j++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerItemPump.SLOTX_START + (j - 1) * Const.SQ - 1,
          this.guiTop + ContainerItemPump.SLOTY - 1,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    btn.displayString = UtilChat.lang("button.itemfilter.type" + tile.getField(TileEntityItemPump.Fields.FILTERTYPE.ordinal()));
    btn.setTooltip(UtilChat.lang("button.itemfilter.tooltip.type" + tile.getField(TileEntityItemPump.Fields.FILTERTYPE.ordinal())));
  }
  @Override
  public void initGui() {
    super.initGui();
    int id = 2;
    btn = new ButtonTileEntityField(
        id++,
        this.guiLeft + 150,
        this.guiTop + Const.PAD / 2,
        tile.getPos(), TileEntityItemPump.Fields.FILTERTYPE.ordinal(), 1,
        20, 20);
    this.addButton(btn);
  }
}
