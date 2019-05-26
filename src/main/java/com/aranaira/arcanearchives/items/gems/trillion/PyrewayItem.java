package com.aranaira.arcanearchives.items.gems.trillion;

import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class PyrewayItem extends ItemTemplate {
    public static final String NAME = "item_pyreway";

    public PyrewayItem() {
        super(NAME);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation (ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GOLD + I18n.format("arcanearchives.tooltip.item.pyreway"));
    }

    @Override
    @SuppressWarnings("deprecation")
    public EnumRarity getRarity (ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public boolean doesSneakBypassUse (ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }
}