package com.aranaira.arcanearchives.inventory;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.inventory.handlers.SharedGCTData;
import com.aranaira.arcanearchives.inventory.slots.SlotRecipeHandler;
import com.aranaira.arcanearchives.network.NetworkHandler;
import com.aranaira.arcanearchives.network.PacketGemCutters;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipe;
import com.aranaira.arcanearchives.recipe.gct.GCTRecipeList;
import it.unimi.dsi.fastutil.ints.Int2IntMap.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class ContainerGemCuttersTable extends Container
{
	private static final int SLOT_OUTPUT = 0;
	private final SlotItemHandler slotOutput;
	private final IInventory playerInventory;
	private final IItemHandlerModifiable tileInventory;
	private final IItemHandlerModifiable outputInv = new ItemStackHandler(1);
	private final IItemHandler combinedInventory;
	private final SharedGCTData sharedData;
	private final EntityPlayer player;
	private final World world;
	private Runnable updateRecipeGUI;

	public ContainerGemCuttersTable(IItemHandlerModifiable tileInventory, SharedGCTData sharedData, EntityPlayer player)
	{
		this.tileInventory = tileInventory;
		this.sharedData = sharedData;
		this.playerInventory = player.inventory;
		this.player = player;
		this.world = player.world;

		IItemHandler mainPlayerInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		if(!(mainPlayerInv instanceof IItemHandlerModifiable))
			throw new IllegalStateException("Expected main player inventory to be modifiable");
		this.combinedInventory = new CombinedInvWrapper(tileInventory, (IItemHandlerModifiable) mainPlayerInv);

		//Output Slot
		this.slotOutput = new SlotItemHandler(outputInv, SLOT_OUTPUT, 95, 18)
		{
			@Override
			public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack)
			{
				for(Entry entry : sharedData.getCurrentRecipe().getMatchingSlots(combinedInventory).int2IntEntrySet())
				{
					combinedInventory.extractItem(entry.getIntKey(), entry.getIntValue(), false);
				}
				updateRecipe();
				return super.onTake(player, stack);
			}

			@Override
			public boolean canTakeStack(EntityPlayer player)
			{
				if (!player.world.isRemote) {
					ArcaneArchives.logger.info("");
				}
				return sharedData.getCurrentRecipe().matches(combinedInventory);
			}

			@Override
			public boolean isItemValid(@Nonnull ItemStack stack)
			{
				return false;
			}
		};

		this.addSlotToContainer(slotOutput);

		//Player Inventory
		for(int i = 0; i < 3; ++i)
		{
			for(int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 23 + j * 18, 166 + i * 18)
				{
					@Override
					public void onSlotChanged()
					{
						super.onSlotChanged();
						updateRecipe();
					}
				});
			}
		}

		//Player Hotbar
		for(int k = 0; k < 9; ++k)
		{
			this.addSlotToContainer(new Slot(playerInventory, k, 23 + k * 18, 224)
			{
				@Override
				public void onSlotChanged()
				{
					super.onSlotChanged();
					updateRecipe();
				}
			});
		}

		//GCT Inventory
		for(int i = 0; i < 2; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new SlotItemHandler(tileInventory, i * 9 + j, 23 + j * 18, 105 + i * 18)
				{
					@Override
					public void onSlotChanged()
					{
						super.onSlotChanged();
						updateRecipe();
					}
				});
			}
		}

		//Recipe Selection Slots
		for(int x = 6; x > -1; x--)
		{
			this.addSlotToContainer(new SlotRecipeHandler(x, x * 18 + 41, 70, sharedData));
		}

		updateRecipe();
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}

	public void setUpdateRecipeGUI(Runnable updateRecipeGUI)
	{
		this.updateRecipeGUI = updateRecipeGUI;
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		Slot slot = this.inventorySlots.get(index);

		if(slot == null || !slot.getHasStack())
		{
			return ItemStack.EMPTY;
		}

		ItemStack original = slot.getStack().copy();
		ItemStack itemstack = slot.getStack().copy();

		// Is it a slot in the main inventory? (aka not player inventory)
		if(index < 36 || slot == slotOutput)
		{
			// try to put it into the player inventory (if we have a player inventory)
			if(!this.mergeItemStack(itemstack, 1, 36, true))
			{
				return ItemStack.EMPTY;
			}
		}
		// Slot is in the player inventory (if it exists), transfer to main inventory
		else if(!this.mergeItemStack(itemstack, 36, 54, false))
		{
			return ItemStack.EMPTY;
		}

		slot.onSlotChanged();

		if(itemstack.getCount() == original.getCount())
		{
			return ItemStack.EMPTY;
		}

		// update slot we pulled from
		slot.putStack(itemstack);
		slot.onTake(player, itemstack);

		if(slot.getHasStack() && slot.getStack().isEmpty())
		{
			slot.putStack(ItemStack.EMPTY);
		}

		return original;
	}

	@Override
	@Nonnull
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if(slotId >= 54 && slotId <= 68)
		{
			Slot baseSlot = getSlot(slotId);
			if(!(baseSlot instanceof SlotRecipeHandler))
			{
				return super.slotClick(slotId, dragType, clickTypeIn, player);
			}

			SlotRecipeHandler slot = (SlotRecipeHandler) baseSlot;

			sharedData.setCurrentRecipe(slot.getRecipe());
			updateRecipe();

			if(player.world.isRemote)
			{
				updateRecipeGUI.run();
			}

			return ItemStack.EMPTY;
		}

		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}

	private void updateRecipe()
	{
		if(updateRecipeGUI != null) updateRecipeGUI.run();

		ItemStack itemstack = ItemStack.EMPTY;

		GCTRecipe curRecipe = sharedData.getCurrentRecipe();
		if(curRecipe != null)
		{
			itemstack = curRecipe.getRecipeOutput().copy();
			if(curRecipe.matches(combinedInventory))
			{
				outputInv.setStackInSlot(SLOT_OUTPUT, itemstack);
			} else
			{
				outputInv.setStackInSlot(SLOT_OUTPUT, ItemStack.EMPTY);
				return;
			}
		} else
		{
			outputInv.setStackInSlot(SLOT_OUTPUT, ItemStack.EMPTY);
			return;
		}

		if(sharedData.getPenultimateRecipe() != sharedData.getLastRecipe())
		{
			playerInventory.setInventorySlotContents(0, itemstack);
		} else if(sharedData.getPenultimateRecipe() != null && sharedData.getPenultimateRecipe() == sharedData.getLastRecipe() && !ItemStack.areItemStacksEqual(sharedData.getPenultimateRecipe().getRecipeOutput(), sharedData.getLastRecipe().getRecipeOutput()))
		{
			playerInventory.setInventorySlotContents(0, itemstack);
		}
		if(sharedData.getLastRecipe() != null && !world.isRemote)
			NetworkHandler.CHANNEL.sendTo(new PacketGemCutters.LastRecipe(sharedData.getLastRecipe()), (EntityPlayerMP) player);

		sharedData.updatePenultimateRecipe();
	}

	public Map<GCTRecipe, Boolean> updateRecipeStatus()
	{
		Map<GCTRecipe, Boolean> map = new HashMap<>();

		for(GCTRecipe recipe : GCTRecipeList.getRecipeList())
		{
			map.put(recipe, recipe.matches(combinedInventory));
		}

		return map;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
	}

	@Override
	public boolean canMergeSlot(ItemStack p_94530_1_, Slot p_94530_2_)
	{
		return super.canMergeSlot(p_94530_1_, p_94530_2_);
	}

	public void updateLastRecipeFromServer(GCTRecipe recipe)
	{
		sharedData.setLastRecipe(recipe);
		if(recipe != null)
		{
			tileInventory.setStackInSlot(0, recipe.getRecipeOutput());
		}
	}

	public SharedGCTData getSharedData()
	{
		return sharedData;
	}
}
