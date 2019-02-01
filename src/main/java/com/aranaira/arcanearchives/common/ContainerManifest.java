package com.aranaira.arcanearchives.common;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.data.ArcaneArchivesClientNetwork;
import com.aranaira.arcanearchives.data.ArcaneArchivesNetwork;
import com.aranaira.arcanearchives.packets.AAPacketHandler;
import com.aranaira.arcanearchives.tileentities.ImmanenceTileEntity;
import com.aranaira.arcanearchives.tileentities.RadiantChestTileEntity;
import com.aranaira.arcanearchives.util.ItemComparison;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.util.handlers.AATickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ContainerManifest extends Container
{

	private List<RadiantChestTileEntity> networkChests = new ArrayList<>();
	private List<ItemStack> ItemList = new ArrayList<>();
	EntityPlayer mPlayer;
	private boolean mServerSide;
	private ArcaneArchivesClientNetwork mClientNetwork;
	private ArcaneArchivesNetwork mNetwork;

	public ContainerManifest(EntityPlayer playerIn, boolean ServerSide)
	{
	/*	ArcaneArchives.logger.info(playerIn.getUniqueID());
		mServerSide = ServerSide;

		mPlayer = playerIn;
		if(ServerSide)
		{
			// This all needs to be done client side
			mNetwork = NetworkHelper.getArcaneArchivesNetwork(playerIn.getUniqueID());
			mNetwork.mManifestItemHandler.Clear();
			for(ImmanenceTileEntity ite : mNetwork.GetRadiantChests())
			{
				RadiantChestTileEntity networkChest = (RadiantChestTileEntity) ite;

				List<ItemStack> items = new ArrayList<>();
				IItemHandler handler = networkChest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
				if (handler == null) throw new RuntimeException(); // TODO: Handle this

				for(int j = 0; j < handler.getSlots(); j++)
				{
					if(!handler.getStackInSlot(j).isEmpty())
					{
						ItemStack s = handler.getStackInSlot(j);
						items.add(s);
						boolean added = false;

						for(ItemStack aItemList : ItemList)
						{
							if(ItemComparison.AreItemsEqual(aItemList, s))
							{
								aItemList.grow(s.getCount());
								added = true;
							}
						}

						if(!added) ItemList.add(s.copy());
					}
				}
				// This doesn't work
				mNetwork.mManifestItemHandler.mChests.add(new RadiantChestPlaceHolder(networkChest.getPos(), items));
			}

			PacketRadiantChestsListResponse message = new PacketRadiantChestsListResponse(playerIn.getUniqueID(), ItemList, mNetwork.mManifestItemHandler.mChests);

			AAPacketHandler.CHANNEL.sendTo(message, (EntityPlayerMP) playerIn);
		}

		mClientNetwork = NetworkHelper.getArcaneArchivesClientNetwork(mPlayer.getUniqueID());

		int i = 0;
		for(int y = 0; y < 9; y++)
		{
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new SlotItemHandler(mClientNetwork.getManifestHandler(), i, x * 18 + 12, y * 18 + 30));
				i++;
			}
		}*/
	}

	@Override
	public boolean canInteractWith(@Nonnull EntityPlayer playerIn)
	{
		return true;
	}

	@Override
	@Nonnull
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		/*if(mServerSide) return ItemStack.EMPTY;

		AATickHandler.GetInstance().clearChests();

		if(slotId < 0) return ItemStack.EMPTY;
		if(ManifestItemHandler.mInstance.getStackInSlot(slotId).isEmpty()) return ItemStack.EMPTY;

		// unused: RadiantChestPlaceHolder RCPH = null;
		// TODO: RCPH handler
		for(RadiantChestPlaceHolder rcph : mClientNetwork.getManifestHandler().mChests)
		{
			if(rcph.Contains(mClientNetwork.getManifestHandler().getStackInSlot(slotId)))
			{
				AATickHandler.GetInstance().mBlockPositions.add(rcph.GetPosition());
			}
		}*/

		return ItemStack.EMPTY;
	}

	public void SetSearchString(String SearchText)
	{
		// ManifestItemHandler.mInstance.setSearchText(SearchText);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
	}
}
