package com.aranaira.arcanearchives.containers.slots;

import com.aranaira.arcanearchives.inventories.ExtendedHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotExtended extends Slot {

  private static IInventory emptyInventory = new InventoryBasic("[Null]", true, 0);
  private final ExtendedHandler itemHandler;
  private final int index;

  public SlotExtended(ExtendedHandler itemHandler, int index, int xPosition, int yPosition) {
    super(emptyInventory, index, xPosition, yPosition);
    this.itemHandler = itemHandler;
    this.index = index;
  }

  @Override
  public boolean isItemValid(@Nonnull ItemStack stack) {
    if (stack.isEmpty()) {
      return false;
    }

    ItemStack currentStack = itemHandler.getStackInSlot(index);
    itemHandler.setStackInSlot(index, ItemStack.EMPTY);
    ItemStack remainder = itemHandler.insertItem(index, stack, true);
    itemHandler.setStackInSlot(index, currentStack);

    return remainder.isEmpty() || remainder.getCount() < stack.getCount();
  }

  @Override
  @Nonnull
  public ItemStack getStack() {
    return itemHandler.getStackInSlot(index);
  }

  @Override
  public void putStack(@Nonnull ItemStack stack) {
    itemHandler.setStackInSlot(index, stack);
    this.onSlotChanged();
  }

  @Override
  public void onSlotChange(@Nonnull ItemStack oldStack, @Nonnull ItemStack newStack) {
    itemHandler.onContentsChanged(index);
  }

  @Override
  public int getSlotStackLimit() {
    return itemHandler.getSlotLimit(index);
  }

  @Override
  public int getItemStackLimit(@Nonnull ItemStack stack) {
    return itemHandler.getStackLimit(index, stack);
  }

  @Override
  public boolean canTakeStack(EntityPlayer playerIn) {
    return !itemHandler.extractItem(index, 1, true).isEmpty();
  }

  @Override
  @Nonnull
  public ItemStack decrStackSize(int amount) {
    return itemHandler.extractItem(index, amount, false);
  }

  public ExtendedHandler getItemHandler() {
    return itemHandler;
  }

  @Override
  public boolean isSameInventory(Slot other) {
    return other instanceof SlotExtended && ((SlotExtended) other).getItemHandler() == this.itemHandler;
  }

}