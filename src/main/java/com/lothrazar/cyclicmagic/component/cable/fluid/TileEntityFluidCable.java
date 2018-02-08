package com.lothrazar.cyclicmagic.component.cable.fluid;
import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;

public class TileEntityFluidCable extends TileEntityBaseCable {
  public TileEntityFluidCable() {
    super(0, 100,0);// inventory, fluid
    this.setFluidTransport();
  }
}