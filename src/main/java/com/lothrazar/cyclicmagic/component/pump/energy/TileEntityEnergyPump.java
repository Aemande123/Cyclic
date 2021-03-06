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
package com.lothrazar.cyclicmagic.component.pump.energy;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityEnergyPump extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {
  // Thermal does 1k, 4k, 9k, 16k, 25k per tick variants
  private static final int TRANSFER_ENERGY_PER_TICK = 8 * 1000;
  public static enum Fields {
    REDSTONE;
  }
  private EnergyStore pumpEnergyStore;
  private int needsRedstone = 0;
  public TileEntityEnergyPump() {
    super(0);
    pumpEnergyStore = new EnergyStore(TRANSFER_ENERGY_PER_TICK);
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    needsRedstone = compound.getInteger(NBT_REDST);
    CapabilityEnergy.ENERGY.readNBT(pumpEnergyStore, null, compound.getTag("powercable"));
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setInteger(NBT_REDST, needsRedstone);
    compound.setTag("powercable", CapabilityEnergy.ENERGY.writeNBT(pumpEnergyStore, null));
    return compound;
  }
  @Override
  public EnumFacing getCurrentFacing() {
    //TODO: same as item pump so pump base class!?!?
    EnumFacing facingTo = super.getCurrentFacing();
    if (facingTo.getAxis().isVertical()) {
      facingTo = facingTo.getOpposite();
    }
    return facingTo;
  }
  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    IEnergyStorage myEnergy = this.getCapability(CapabilityEnergy.ENERGY, null);
    EnumFacing importFromSide = this.getCurrentFacing();
    TileEntity importFromTile = world.getTileEntity(pos.offset(importFromSide));
    IEnergyStorage exportHandler = null;
    IEnergyStorage importHandlr = null;
    if (importFromTile != null) {
      importHandlr = importFromTile.getCapability(CapabilityEnergy.ENERGY, importFromSide.getOpposite());
      // ModCyclic.logger.error("importFromTile  "+importFromTile.getBlockType().getLocalizedName());
    }
    EnumFacing exportToSide = importFromSide.getOpposite();
    TileEntity exportToTile = world.getTileEntity(pos.offset(exportToSide));
    if (exportToTile != null) {
      exportHandler = exportToTile.getCapability(CapabilityEnergy.ENERGY, exportToSide.getOpposite());
      //   ModCyclic.logger.error("exportToTile   "+exportToTile.getBlockType().getLocalizedName());
    }
    //first pull in power
    if (importHandlr != null && importHandlr.canExtract()) {
      int drain = importHandlr.extractEnergy(TRANSFER_ENERGY_PER_TICK, true);
      if (drain > 0) {
        //now push it into output, but find out what was ACTUALLY taken
        int filled = myEnergy.receiveEnergy(drain, false);
        //now actually drain that much  
        importHandlr.extractEnergy(filled, false);
        //    ModCyclic.logger.error("pump take IN  " + filled + "i am holding" + this.pumpEnergyStore.getEnergyStored());
      }
    }
    if (exportHandler != null && exportHandler.canReceive()) {
      int drain = myEnergy.extractEnergy(TRANSFER_ENERGY_PER_TICK, true);
      if (drain > 0) {
        //now push it into output, but find out what was ACTUALLY taken
        int filled = exportHandler.receiveEnergy(drain, false);
        //now actually drain that much  
        myEnergy.extractEnergy(filled, false);
        if (importFromTile instanceof TileEntityCableBase) {
          //TODO: not so compatible with other fluid systems. itl do i guess
          TileEntityCableBase cable = (TileEntityCableBase) importFromTile;
          //  ModCyclic.logger.error("pump EXPORT  " + filled);
          if (cable.isEnergyPipe()) {
            // ModCyclic.logger.error("cable receive from   "+ side);
            cable.updateIncomingEnergyFace(importFromSide); // .getOpposite()
          }
        }
      }
    }
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
    }
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  @Override
  public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) {
      return CapabilityEnergy.ENERGY.cast(this.pumpEnergyStore);
    }
    return super.getCapability(capability, facing);
  }
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY &&
        (facing == this.getCurrentFacing() || facing == this.getCurrentFacing().getOpposite())) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
}
