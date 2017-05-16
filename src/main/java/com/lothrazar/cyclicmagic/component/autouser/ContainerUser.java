package com.lothrazar.cyclicmagic.component.autouser;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerUser extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 42;
  protected TileEntityUser tileEntity;
  private int tileSpeed;
  private int timer;
  private int redstone;
  private int leftright;
  private int tileSize;
  public ContainerUser(InventoryPlayer inventoryPlayer, TileEntityUser te) {
    tileEntity = te;
    for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
      addSlotToContainer(new Slot(tileEntity, i, SLOTX_START + i * Const.SQ, SLOTY));
    }
    // commonly used vanilla code that adds the player's inventory
    bindPlayerInventory(inventoryPlayer);
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    ItemStack stack = UtilItemStack.EMPTY;
    Slot slotObject = (Slot) inventorySlots.get(slot);
    // null checks and checks if the item can be stacked (maxStackSize > 1)
    if (slotObject != null && slotObject.getHasStack()) {
      ItemStack stackInSlot = slotObject.getStack();
      stack = stackInSlot.copy();
      // merges the item into player inventory since its in the tileEntity
      if (slot < tileEntity.getSizeInventory()) {
        if (!this.mergeItemStack(stackInSlot, tileEntity.getSizeInventory(), 36 + tileEntity.getSizeInventory(), true)) { return UtilItemStack.EMPTY; }
      }
      // places it into the tileEntity is possible since its in the player
      // inventory
      else if (!this.mergeItemStack(stackInSlot, 0, tileEntity.getSizeInventory(), false)) { return UtilItemStack.EMPTY; }
      if (stackInSlot.stackSize == 0) {
        slotObject.putStack(UtilItemStack.EMPTY);
      }
      else {
        slotObject.onSlotChanged();
      }
      if (stackInSlot.stackSize == stack.stackSize) { return UtilItemStack.EMPTY; }
      slotObject.onPickupFromSlot(player, stackInSlot);
    }
    return stack;
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
      int idx = TileEntityUser.Fields.SPEED.ordinal();
      if (this.tileSpeed != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityUser.Fields.TIMER.ordinal();
      if (this.timer != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityUser.Fields.REDSTONE.ordinal();
      if (this.redstone != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityUser.Fields.LEFTRIGHT.ordinal();
      if (this.leftright != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityUser.Fields.SIZE.ordinal();
      if (this.tileSize != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileSpeed = this.tileEntity.getField(TileEntityUser.Fields.SPEED.ordinal());
    this.timer = this.tileEntity.getField(TileEntityUser.Fields.TIMER.ordinal());
    this.redstone = this.tileEntity.getField(TileEntityUser.Fields.REDSTONE.ordinal());
    this.leftright = this.tileEntity.getField(TileEntityUser.Fields.LEFTRIGHT.ordinal());
    this.tileSize = this.tileEntity.getField(TileEntityUser.Fields.SIZE.ordinal());
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.tileEntity.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tileEntity);
  }
}
