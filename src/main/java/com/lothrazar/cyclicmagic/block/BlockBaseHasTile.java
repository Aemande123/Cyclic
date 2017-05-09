package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for any block that has a tile entity
 * 
 * @author Sam
 *
 */
public abstract class BlockBaseHasTile extends BlockBase {
  protected int guiID = -1;
  public BlockBaseHasTile(Material materialIn) {
    super(materialIn);
  }
  public void setGuiId(int id) {
    this.guiID = id;
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem,EnumFacing side, float hitX, float hitY, float hitZ) {
    if (player.isSneaking()) { //|| world.isRemote
      return super.onBlockActivated(world, pos, state, player, hand,heldItem, side, hitX, hitY, hitZ);
    }
    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
    if (this.guiID > -1) {
      player.openGui(ModCyclic.instance, this.guiID, world, x, y, z);
      return true;
    }
    return false;
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    if (tileentity != null && tileentity instanceof IInventory) {
      InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileentity);
      worldIn.updateComparatorOutputLevel(pos, this);
    }
    super.breakBlock(worldIn, pos, state);
  }
}
