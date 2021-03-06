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
package com.lothrazar.cyclicmagic.component.peat.generator;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityPeatGenerator extends TileEntityBaseMachineInvo implements ITickable {
  public final static int TIMER_FULL = 200;
  public final static int SLOT_INPUT = 0;
  //FOR REFERENCERF tools coal gen is 64 per tick
  //Thermal magmatic dynamo makes 40 per tick from lava
  //forestry engine 20/tick
  public static final int PER_TICK = 128;
  //total energy made per item is PER_TICK * TIMER_FULL
  private static final int CAPACITY = PER_TICK * 100;
  public static enum Fields {
    TIMER;
  }
  private EnergyStore energy;
  public TileEntityPeatGenerator() {
    super(1);
    this.setSlotsForInsert(SLOT_INPUT);
    energy = new EnergyStore(CAPACITY);
    timer = 0;
  }
  @Override
  public void update() {
    if (isRunning() == false) {
      return;
    }
    // only if burning peat 
    if (timer > 0) {
      int actuallyGained = this.energy.receiveEnergy(PER_TICK, true);
      if (actuallyGained == PER_TICK) {
        this.spawnParticlesAbove();
        // either we have room to eat everything that generated, or we didnt.
        //if we did, burn some fuel. if not, wait for more room in battery
        this.energy.receiveEnergy(PER_TICK, false);
        timer--;
      }
    }
    if (timer == 0) {
      //timer is zero. ok so it STAYS zero unless we can eat another peat brick
      // update fuel stuffs  
      if (this.isValidFuel(this.getStackInSlot(SLOT_INPUT))) {
        this.decrStackSize(SLOT_INPUT);
        timer = TIMER_FULL;
      }
    }
  }
  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return isValidFuel(stack);
  }
  private boolean isValidFuel(ItemStack peat) {
    return peat.getItem().equals(Item.getByNameOrId(Const.MODRES + "peat_fuel"));
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public int getFieldCount() {
    return getFieldOrdinals().length;
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        timer = value;
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if (this.energy != null && compound.hasKey("powercable")) {
      CapabilityEnergy.ENERGY.readNBT(energy, null, compound.getTag("powercable"));
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (energy != null) {
      compound.setTag("powercable", CapabilityEnergy.ENERGY.writeNBT(energy, null));
    }
    return super.writeToNBT(compound);
  }
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
  @Override
  public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY) {
      return CapabilityEnergy.ENERGY.cast(this.energy);
    }
    return super.getCapability(capability, facing);
  }
}
