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
package com.lothrazar.cyclicmagic.util;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class UtilFluid {
  //  public static ItemStack dispenseStack(World world, BlockPos pos, ItemStack stack, EnumFacing facing) {
  //    if (FluidUtil.getFluidContained(stack) != null) {
  //      return dumpContainer(world, pos, stack);
  //    }
  //    else {
  //      return fillContainer(world, pos, stack, facing);
  //    }
  //  }
  /**
   * Picks up fluid fills a container with it.
   */
  public static FluidActionResult fillContainer(World world, BlockPos pos, ItemStack stackIn, EnumFacing facing) {
    //    ItemStack result = stackIn.copy();
    return FluidUtil.tryPickUpFluid(stackIn, null, world, pos, facing);
    //  if (--stackIn.stackSize == 0) {
    //    stackIn.deserializeNBT(result.serializeNBT());
    //  }
    //    if (res == FluidActionResult.FAILURE) { return stackIn; }
    //    return res.getResult();
  }
  /**
   * Drains a filled container and places the fluid.
   * 
   * RETURN new item stack that has been drained after placing in world if it works null otherwise
   */
  public static ItemStack dumpContainer(World world, BlockPos pos, ItemStack stackIn) {
    //    BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, pos);
    ItemStack dispensedStack = stackIn.copy();
    dispensedStack.setCount(1);
    IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(dispensedStack);
    if (fluidHandler == null) {
      return null;
    }
    FluidStack fluidStack = fluidHandler.drain(Fluid.BUCKET_VOLUME, false);
    if (fluidStack != null && fluidStack.amount >= Fluid.BUCKET_VOLUME) {
      //      FluidActionResult placeResult = FluidUtil.tryPlaceFluid(null, world, pos, dispensedStack, fluidStack);
      if (FluidUtil.tryPlaceFluid(null, world, pos, dispensedStack, fluidStack).isSuccess()) {
        //http://www.minecraftforge.net/forum/topic/56265-1112-fluidhandler-capability-on-buckets/
        fluidHandler.drain(Fluid.BUCKET_VOLUME, true);
        ItemStack returnMe = fluidHandler.getContainer();
        //        stackIn.deserializeNBT(returnMe.serializeNBT());
        return returnMe;
      }
    }
    return null;
  }
  public static ItemStack drainOneBucket(ItemStack d) {
    IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(d);
    if (fluidHandler == null) {
      return d;
    } //its empty, ok no problem
    fluidHandler.drain(Fluid.BUCKET_VOLUME, true);
    return fluidHandler.getContainer();
  }
  public static boolean isEmptyOfFluid(ItemStack returnMe) {
    return FluidUtil.getFluidContained(returnMe) == null;
  }
  public static FluidStack getFluidContained(ItemStack returnMe) {
    return FluidUtil.getFluidContained(returnMe);
  }
  public static Fluid getFluidType(ItemStack returnMe) {
    FluidStack f = FluidUtil.getFluidContained(returnMe);
    return (f == null) ? null : f.getFluid();
  }
  public static boolean stackHasFluidHandler(ItemStack stackIn) {
    return FluidUtil.getFluidHandler(stackIn) != null;
  }
  public static boolean hasFluidHandler(TileEntity tile, EnumFacing side) {
    return tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
  }
  public static IFluidHandler getFluidHandler(TileEntity tile, EnumFacing side) {
    return (tile != null && tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) ? tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side) : null;
  }
  public static boolean interactWithFluidHandler(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nullable EnumFacing side) {
    return FluidUtil.interactWithFluidHandler(player, EnumHand.MAIN_HAND, world, pos, side);
  }
  /**
   * Look for a fluid handler with gien position and direction try to extract from that pos and fill the tank
   * 
   * 
   * @param world
   * @param posSide
   * @param sideOpp
   * @param tankTo
   * @param amount
   * @return
   */
  public static boolean tryFillTankFromPosition(World world, BlockPos posSide, EnumFacing sideOpp, FluidTank tankTo, int amount) {
    try {
      IFluidHandler fluidFrom = FluidUtil.getFluidHandler(world, posSide, sideOpp);
      if (fluidFrom != null) {
        //its not my facing dir
        // SO: pull fluid from that into myself
        FluidStack wasDrained = fluidFrom.drain(amount, false);
        if (wasDrained == null) {
          return false;
        }
        int filled = tankTo.fill(wasDrained, false);
        if (wasDrained != null && wasDrained.amount > 0
            && filled > 0) {
          //       ModCyclic.logger.log(" wasDrained  "+wasDrained.amount);
          //       ModCyclic.logger.log(" filled  "+  filled);
          int realAmt = Math.min(filled, wasDrained.amount);
          wasDrained = fluidFrom.drain(realAmt, true);
          if (wasDrained == null) {
            return false;
          }
          return tankTo.fill(wasDrained, true) > 0;
        }
      }
      return false;
    }
    catch (Exception e) {
      ModCyclic.logger.error("Somebody elses fluid tank had an issue when we tried to drain");
      ModCyclic.logger.error(e.getMessage());
      //charset crashes here i guess
      //https://github.com/PrinceOfAmber/Cyclic/issues/605
      // https://github.com/PrinceOfAmber/Cyclic/issues/605https://pastebin.com/YVtMYsF6
      return false;
    }
  }
  public static boolean tryFillPositionFromTank(World world, BlockPos posSide, EnumFacing sideOpp, FluidTank tankFrom, int amount) {
    try {
      IFluidHandler fluidTo = FluidUtil.getFluidHandler(world, posSide, sideOpp);
      if (fluidTo != null) {
        //its not my facing dir
        // SO: pull fluid from that into myself
        FluidStack wasDrained = tankFrom.drain(amount, false);
        if (wasDrained == null) {
          return false;
        }
        int filled = fluidTo.fill(wasDrained, false);
        if (wasDrained != null && wasDrained.amount > 0
            && filled > 0) {
          int realAmt = Math.min(filled, wasDrained.amount);
          wasDrained = tankFrom.drain(realAmt, true);
          if (wasDrained == null) {
            return false;
          }
          return fluidTo.fill(wasDrained, true) > 0;
        }
      }
      return false;
    }
    catch (Exception e) {
      ModCyclic.logger.error("A fluid tank had an issue when we tried to fill");
      ModCyclic.logger.error(e.getMessage());
      //charset crashes here i guess
      //https://github.com/PrinceOfAmber/Cyclic/issues/605
      // https://github.com/PrinceOfAmber/Cyclic/issues/605https://pastebin.com/YVtMYsF6
      return false;
    }
  }
}
