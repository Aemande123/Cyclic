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
package com.lothrazar.cyclicmagic.entity.projectile;
import com.lothrazar.cyclicmagic.explosion.ExplosionMining;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDynamiteMining extends EntityThrowableDispensable {
  public static Item renderSnowball;
  private float explosionLevel;
  public EntityDynamiteMining(World worldIn) {
    super(worldIn);
    this.explosionLevel = EntityDynamite.EX_ENDCRYSTAL;
  }
  public EntityDynamiteMining(World worldIn, int explos) {
    super(worldIn);
    this.explosionLevel = explos;
  }
  public EntityDynamiteMining(World worldIn, EntityLivingBase ent, int strength) {
    super(worldIn, ent);
    this.explosionLevel = strength;
  }
  public EntityDynamiteMining(World worldIn, int strength, double x, double y, double z) {
    super(worldIn, x, y, z);
    this.explosionLevel = strength;
  }
  @Override
  protected void processImpact(RayTraceResult mop) {
    if (this.inWater == false) {
      ExplosionMining explosion = new ExplosionMining(this.getEntityWorld(), this.getThrower(), posX, posY, posZ, explosionLevel, false, true);
      explosion.doExplosionA();
      explosion.doExplosionB(false);
    }
    this.setDead();
  }
}
