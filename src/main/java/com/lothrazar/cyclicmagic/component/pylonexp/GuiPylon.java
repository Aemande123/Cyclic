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
package com.lothrazar.cyclicmagic.component.pylonexp;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.pylonexp.TileEntityXpPylon.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPylon extends GuiBaseContainer {
  public static final ResourceLocation PROGEXP = new ResourceLocation(Const.MODID, "textures/gui/progress_exp.png");
  public static final ResourceLocation SLOT_BOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_bottle.png");
  public static final ResourceLocation SLOT_EBOTTLE = new ResourceLocation(Const.MODID, "textures/gui/inventory_slot_ebottle.png");
  private TileEntityXpPylon tile;
  boolean debugLabels = false;
  private ButtonExpPylon btnCollect;
  private ButtonExpPylon btnDepositAll;
  public GuiPylon(InventoryPlayer inventoryPlayer, TileEntityXpPylon tileEntity) {
    super(new ContainerPylon(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = Fields.REDSTONE.ordinal();
  }
  @Override
  public void initGui() {
    super.initGui();
    int btnId = 0;
    int w = 58, h = 20;
    int x = this.guiLeft + Const.PAD;
    int y = this.guiTop + Const.PAD * 3 + Const.PAD / 2;
    int hSpacing = w / 2 + Const.PAD / 4;
    btnCollect = new ButtonExpPylon(btnId++,
        x, y, w + 12, h, "");
    btnCollect.setTooltip("button.exp_pylon.collect.tooltip");
    this.buttonList.add(btnCollect);
    y += h + Const.PAD / 2;
    //collect and bottle are done, now the rest
    ButtonExpPylon btn = new ButtonExpPylon(btnId++,
        x, y, w / 2, h, "+" + 10);
    btn.setTooltip("button.exp_pylon.deposit.tooltip");
    btn.setValue(10);
    this.buttonList.add(btn);
    x += hSpacing;
    btn = new ButtonExpPylon(btnId++,
        x, y, w / 2, h, "+" + 50);
    btn.setTooltip("button.exp_pylon.deposit.tooltip");
    btn.setValue(50);
    this.buttonList.add(btn);
    x += hSpacing;
    btn = new ButtonExpPylon(btnId++,
        x, y, w / 2 + 5, h, "+" + 500);
    btn.setTooltip("button.exp_pylon.deposit.tooltip");
    btn.setValue(500);
    this.buttonList.add(btn);
    x = this.guiLeft + Const.PAD;
    y += h + Const.PAD / 2;
    //START OF - ROW
    btn = new ButtonExpPylon(btnId++,
        x, y, w / 2, h, "-" + 10);
    btn.setTooltip("button.exp_pylon.drain.tooltip");
    btn.setValue(-10);
    this.buttonList.add(btn);
    x += hSpacing;
    btn = new ButtonExpPylon(btnId++,
        x, y, w / 2, h, "-" + 50);
    btn.setTooltip("button.exp_pylon.drain.tooltip");
    btn.setValue(-50);
    this.buttonList.add(btn);
    x += hSpacing;
    btn = new ButtonExpPylon(btnId++,
        x, y, w / 2 + 5, h, "-" + 500);
    btn.setTooltip("button.exp_pylon.drain.tooltip");
    btn.setValue(-500);
    this.buttonList.add(btn);
    x = this.guiLeft + Const.PAD;
    //FINALLY THE all button
    y += h + Const.PAD / 2;
    btnDepositAll = new ButtonExpPylon(btnId++,
        x, y, w / 2, h, UtilChat.lang("button.exp_pylon.depositall"));
    btnDepositAll.setTooltip("button.exp_pylon.depositall.tooltip");
    this.buttonList.add(btnDepositAll);
  }
  @Override
  protected void actionPerformed(GuiButton button) {
    if (button.id == btnCollect.id) {
      ModCyclic.network.sendToServer(new PacketTilePylon(tile.getPos(), 1, TileEntityXpPylon.Fields.COLLECT));
    }
    else if (button.id == btnDepositAll.id) {
      //fake: exp really means deposit
      ModCyclic.network.sendToServer(new PacketTilePylon(tile.getPos(), 0, TileEntityXpPylon.Fields.EXP));
    }
    else if (button instanceof ButtonExpPylon && ((ButtonExpPylon) button).getValue() != 0) {
      ModCyclic.network.sendToServer(new PacketTilePylon(tile.getPos(), ((ButtonExpPylon) button).getValue(), TileEntityXpPylon.Fields.EXP));
    }
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(SLOT_BOTTLE);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
      if (k == 0)
        this.mc.getTextureManager().bindTexture(SLOT_BOTTLE);
      else
        this.mc.getTextureManager().bindTexture(SLOT_EBOTTLE);
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerPylon.SLOTX - 1, this.guiTop + ContainerPylon.SLOTY - 1 + k * (8 + Const.SQ), u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    this.drawFluidBar();
  }
  private void drawFluidBar() {
    //??EH MAYBE https://github.com/BuildCraft/BuildCraft/blob/6.1.x/common/buildcraft/core/gui/GuiBuildCraft.java#L121-L162
    int u = 0, v = 0;
    int currentFluid = tile.getField(TileEntityXpPylon.Fields.EXP.ordinal()); // ( fluid == null ) ? 0 : fluid.amount;//tile.getCurrentFluid();
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID);
    int pngWidth = 36, pngHeight = 124, f = 2, h = pngHeight / f;//f is scale factor. original is too big
    int x = this.guiLeft + 120, y = this.guiTop + 40;
    Gui.drawModalRectWithCustomSizedTexture(
        x, y, u, v,
        pngWidth / f, h,
        pngWidth / f, h);
    h -= 2;// inner texture is 2 smaller, one for each border
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID_EXP);
    float percent = ((float) currentFluid / ((float) TileEntityXpPylon.TANK_FULL));
    int hpct = (int) (h * percent);
    Gui.drawModalRectWithCustomSizedTexture(
        x + 1, y + 1 + h - hpct,
        u, v,
        16, hpct,
        16, h);
  }
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    btnCollect.displayString = UtilChat.lang("button.exp_pylon.collect" + tile.getField(TileEntityXpPylon.Fields.COLLECT.ordinal()));
    int fluidHas = this.tile.getField(TileEntityXpPylon.Fields.EXP.ordinal());
    this.drawString(fluidHas + " / " + TileEntityXpPylon.TANK_FULL, this.xSize / 2 - 8, 108);
    int expHas = fluidHas / TileEntityXpPylon.FLUID_PER_EXP;
    int expFull = TileEntityXpPylon.TANK_FULL / TileEntityXpPylon.FLUID_PER_EXP;
    this.drawString("EXP: " + expHas + " / " + expFull, this.xSize / 2 - 20, 118);
  }
}
