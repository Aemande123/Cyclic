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
package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.fluid.BlockFluidExp;
import com.lothrazar.cyclicmagic.fluid.BlockFluidMilk;
import com.lothrazar.cyclicmagic.fluid.BlockFluidPoison;
import com.lothrazar.cyclicmagic.fluid.FluidExp;
import com.lothrazar.cyclicmagic.fluid.FluidMilk;
import com.lothrazar.cyclicmagic.fluid.FluidPoison;
import net.minecraft.init.Items;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class FluidsRegistry {
  public static FluidMilk fluid_milk;
  public static BlockFluidMilk block_milk;
  public static FluidExp fluid_exp;
  public static BlockFluidExp block_exp;
  public static FluidPoison fluid_poison;
  public static BlockFluidPoison block_poison;
  public static void registerPoison() {
    fluid_poison = new FluidPoison();
    FluidRegistry.registerFluid(fluid_poison);
    block_poison = new BlockFluidPoison();
    fluid_poison.setBlock(block_poison);
    BlockRegistry.registerBlock(block_poison, "poison", null);
    FluidRegistry.addBucketForFluid(fluid_poison);
  }
  public static void registerMilk() {
    fluid_milk = new FluidMilk();
    FluidRegistry.registerFluid(fluid_milk);
    block_milk = new BlockFluidMilk();
    fluid_milk.setBlock(block_milk);
    BlockRegistry.registerBlock(block_milk, "milk", null);
    FluidRegistry.addBucketForFluid(fluid_milk);
  }
  public static void registerExp() {
    if (fluid_exp != null) {
      return;
    }
    fluid_exp = new FluidExp();
    FluidRegistry.registerFluid(fluid_exp);
    block_exp = new BlockFluidExp();
    fluid_exp.setBlock(block_exp);
    BlockRegistry.registerBlock(block_exp, "xpjuice", null);
    FluidRegistry.addBucketForFluid(fluid_exp);
  }
  public static void addPoisonRecipe() {
    try {
      //dont do this, FluidsRegistry.fluid_poison , some other mod might have added poison first, then we get rejected --> null
      Fluid poison = FluidRegistry.getFluid(FluidsRegistry.fluid_poison.getName());
      if (poison == null) {
        return;
      }
      RecipeRegistry.addShapelessRecipe(FluidUtil.getFilledBucket(new FluidStack(poison, Fluid.BUCKET_VOLUME)),
          FluidUtil.getFilledBucket(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME)),
          Items.SPIDER_EYE, Items.POISONOUS_POTATO, Items.SUGAR);
    }
    catch (Exception e) {
      //if another mod adds poison fluid (such as biomes o plenty), then our fluid gets rejected
      ModCyclic.logger.error("Error: Cyclic failed to add recipe for Poison Bucket");
      e.printStackTrace();
    }
  }
}
