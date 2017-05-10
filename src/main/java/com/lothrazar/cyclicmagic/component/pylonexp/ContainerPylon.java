package com.lothrazar.cyclicmagic.component.pylonexp;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.SlotItemRestricted;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerPylon extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX = 150;
  public static final int SLOTY = 18;
  protected TileEntityXpPylon tileEntity;
  private int bottle;
  private int tileTimer;
  private int tileXp;
  private int spray;
  private int collect;
  public ContainerPylon(InventoryPlayer inventoryPlayer, TileEntityXpPylon te) {
    tileEntity = te;
    for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
      Item filt = (i == 0) ? Items.GLASS_BOTTLE : Items.EXPERIENCE_BOTTLE;
      addSlotToContainer(new SlotItemRestricted(tileEntity, i, SLOTX, SLOTY + i * (8 + Const.SQ), filt));
    }
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
      if (stackInSlot.getCount() == 0) {
        slotObject.putStack(UtilItemStack.EMPTY);
      }
      else {
        slotObject.onSlotChanged();
      }
      if (stackInSlot.getCount() == stack.getCount()) { return UtilItemStack.EMPTY; }
      slotObject.onTake(player, stackInSlot);
    }
    return stack;
  }
  @Override
  public void detectAndSendChanges() {
    super.detectAndSendChanges();
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = (IContainerListener) this.listeners.get(i);
      int idx = TileEntityXpPylon.Fields.TIMER.ordinal();
      if (this.tileTimer != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityXpPylon.Fields.BOTTLE.ordinal();
      if (this.bottle != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityXpPylon.Fields.EXP.ordinal();
      if (this.tileXp != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityXpPylon.Fields.SPRAY.ordinal();
      if (this.spray != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
      idx = TileEntityXpPylon.Fields.COLLECT.ordinal();
      if (this.collect != this.tileEntity.getField(idx)) {
        icontainerlistener.sendProgressBarUpdate(this, idx, this.tileEntity.getField(idx));
      }
    }
    this.tileTimer = this.tileEntity.getField(TileEntityXpPylon.Fields.TIMER.ordinal());
    this.bottle = this.tileEntity.getField(TileEntityXpPylon.Fields.BOTTLE.ordinal());
    this.tileXp = this.tileEntity.getField(TileEntityXpPylon.Fields.EXP.ordinal());
    this.spray = this.tileEntity.getField(TileEntityXpPylon.Fields.SPRAY.ordinal());
    this.collect = this.tileEntity.getField(TileEntityXpPylon.Fields.COLLECT.ordinal());
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
