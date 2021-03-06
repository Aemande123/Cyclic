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
package com.lothrazar.cyclicmagic.component.peat.farm;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class GuiPeatFarm extends GuiBaseContainer {
  public GuiPeatFarm(InventoryPlayer inventoryPlayer, TileEntityPeatFarm te) {
    super(new ContainerPeatFarm(inventoryPlayer, te), te);
    this.fieldRedstoneBtn = TileEntityPeatFarm.Fields.REDSTONE.ordinal();
  }
  @Override
  public void initGui() {
    super.initGui();
    // BUTTONS! 
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int rowSize = 6;
    for (int i = 0; i < rowSize; i++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerPeatFarm.SLOTX_START + i * Const.SQ - 1,
          this.guiTop + ContainerPeatFarm.SLOTY - 1,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    for (int i = rowSize; i < 2 * rowSize; i++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerPeatFarm.SLOTX_START + (i - rowSize) * Const.SQ - 1,
          this.guiTop + ContainerPeatFarm.SLOTY - 1 + Const.SQ,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    //DRAW ENERGY BAR
    this.drawEnergyBar();
    this.drawFluidBar();
  }
  private void drawEnergyBar() {
    int u = 0, v = 0;
    IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
    float percent = ((float) energy.getEnergyStored()) / ((float) energy.getMaxEnergyStored());
    int outerLength = 62, outerWidth = 16;
    int innerLength = 60, innerWidth = 14;
    int fuelX = this.guiLeft + 152;
    this.mc.getTextureManager().bindTexture(Const.Res.ENERGY_CTR);
    Gui.drawModalRectWithCustomSizedTexture(
        fuelX - 1,
        this.guiTop + 16, u, v,
        outerWidth, outerLength,
        outerWidth, outerLength);
    this.mc.getTextureManager().bindTexture(Const.Res.ENERGY_INNER);
    Gui.drawModalRectWithCustomSizedTexture(
        fuelX,
        this.guiTop + 17, u, v,
        innerWidth, (int) (innerLength * percent),
        innerWidth, innerLength);
  }
  private void drawFluidBar() {
    //??EH MAYBE https://github.com/BuildCraft/BuildCraft/blob/6.1.x/common/buildcraft/core/gui/GuiBuildCraft.java#L121-L162
    int u = 0, v = 0;
    int currentFluid = tile.getField(TileEntityPeatFarm.Fields.FLUID.ordinal()); // ( fluid == null ) ? 0 : fluid.amount;//tile.getCurrentFluid();
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID);
    int pngWidth = 36, pngHeight = 124, f = 2, h = pngHeight / f;//f is scale factor. original is too big
    int x = this.guiLeft + 120, y = this.guiTop + 16;
    Gui.drawModalRectWithCustomSizedTexture(
        x, y, u, v,
        pngWidth / f, h,
        pngWidth / f, h);
    h -= 2;// inner texture is 2 smaller, one for each border
    this.mc.getTextureManager().bindTexture(Const.Res.FLUID_WATER);
    float percent = ((float) currentFluid / ((float) TileEntityPeatFarm.TANK_FULL));
    int hpct = (int) (h * percent);
    Gui.drawModalRectWithCustomSizedTexture(
        x + 1, y + 1 + h - hpct,
        u, v,
        16, hpct,
        16, h);
  }
}
