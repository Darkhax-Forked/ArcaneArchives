package com.aranaira.arcanearchives.tileentities.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public interface IManifestTileEntity {
	default boolean isSingleStackInventory () {
		return false;
	}

	default ItemStack getSingleStack () {
		return ItemStack.EMPTY;
	}

	String getDescriptor ();

	String getChestName ();

	IItemHandler getInventory ();
}
