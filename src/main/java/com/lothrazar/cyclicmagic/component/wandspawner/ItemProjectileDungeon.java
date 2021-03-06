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
package com.lothrazar.cyclicmagic.component.wandspawner;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.item.base.BaseItemProjectile;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilSound;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class ItemProjectileDungeon extends BaseItemProjectile implements IHasRecipe, IHasConfig {
  private static final int COOLDOWN = 10;
  private static int DUNGEONRADIUS = 64;
  public ItemProjectileDungeon() {
    super();
    this.setMaxDamage(1000);
    this.setMaxStackSize(1);
  }
  @Override
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntityDungeonEye(world, x, y, z);
  }
  @Override
  public void syncConfig(Configuration config) {
    DUNGEONRADIUS = config.getInt("Ender Dungeon Radius", Const.ConfigCategory.items, 64, 8, 128, "Search radius of dungeonfinder");
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        " sp",
        " ws",
        "w  ",
        'w', "cropNetherWart",
        's', new ItemStack(Blocks.MOSSY_COBBLESTONE),
        'p', "enderpearl");
  }
  @Override
  public void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    BlockPos blockpos = UtilWorld.findClosestBlock(player, Blocks.MOB_SPAWNER, DUNGEONRADIUS);
    if (blockpos != null) {
      EntityDungeonEye entityendereye = new EntityDungeonEye(world, player);
      doThrow(world, player, hand, entityendereye, 0.5F);
      entityendereye.moveTowards(blockpos);
    }
    else {
      // not found, so play different sound
      UtilSound.playSound(player, player.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH);
      if (world.isRemote) {
        UtilChat.addChatMessage(player, UtilChat.lang("item.ender_dungeon.notfound") + " " + DUNGEONRADIUS);
      }
    }
    player.getCooldownTracker().setCooldown(held.getItem(), COOLDOWN);
    UtilItemStack.damageItem(player, held);
  }
  @Override
  public SoundEvent getSound() {
    return SoundRegistry.dungeonfinder;
  }
}
