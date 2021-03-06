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
package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnderPearlReuse extends BaseTool implements IHasRecipe {
  private static final int durability = 2000;
  private static final int cooldown = 10;
  public static enum OrbType {
    NORMAL, MOUNTED;
  }
  private OrbType orbType;
  public ItemEnderPearlReuse(OrbType o) {
    super(durability);
    orbType = o;
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack itemStackIn = playerIn.getHeldItem(hand);
    worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_ENDERPEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    playerIn.getCooldownTracker().setCooldown(this, cooldown);
    if (!worldIn.isRemote) {
      EntityEnderPearl entityenderpearl = new EntityEnderPearl(worldIn, playerIn); //func_184538_a
      entityenderpearl.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
      worldIn.spawnEntity(entityenderpearl);
      if (orbType == OrbType.MOUNTED) {
        playerIn.dismountRidingEntity();
        playerIn.startRiding(entityenderpearl);
      }
    }
    super.onUse(itemStackIn, playerIn, worldIn, hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
  }
  @Override
  public IRecipe addRecipe() {
    switch (orbType) {
      case MOUNTED:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            "ere",
            "rsr",
            "ere",
            'e', new ItemStack(Items.ENDER_EYE),
            'r', "dyeBlue",
            's', "blockIron");
      case NORMAL:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            "ere",
            "rsr",
            "ere",
            'e', new ItemStack(Items.ENDER_EYE),
            'r', "dustRedstone",
            's', "blockIron");
      default:
      break;
    }
    return null;
  }
  @SideOnly(Side.CLIENT)
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}
