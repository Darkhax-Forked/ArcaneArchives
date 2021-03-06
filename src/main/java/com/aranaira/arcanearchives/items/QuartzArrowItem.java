package com.aranaira.arcanearchives.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemSpectralArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class QuartzArrowItem extends ItemSpectralArrow {
	public static final String NAME = "quartz_arrow";

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.quartz_arrow"));
	}
}
