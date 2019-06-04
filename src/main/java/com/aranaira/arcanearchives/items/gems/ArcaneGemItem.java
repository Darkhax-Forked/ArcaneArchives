package com.aranaira.arcanearchives.items.gems;

import baubles.api.cap.BaublesCapabilities;
import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.items.templates.ItemTemplate;
import com.aranaira.arcanearchives.util.NBTUtils;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;

import java.awt.*;
import java.util.ArrayList;

public abstract class ArcaneGemItem extends ItemTemplate {
    public GemCut cut;
    public GemColor color;
    public int
        maxChargeNormal, maxChargeUpgraded;

    private static final byte
        UPGRADE_MATTER = 1,
        UPGRADE_POWER = 2,
        UPGRADE_SPACE = 4,
        UPGRADE_TIME = 8;

    public ArcaneGemItem(String name, GemCut cut, GemColor color, int maxChargeNormal, int maxChargeUpgraded) {
        super(name);
        this.cut = cut;
        this.color = color;
        this.maxChargeNormal = maxChargeNormal;
        this.maxChargeUpgraded = maxChargeUpgraded;
        setMaxStackSize(1);
    }

    public GemCut getGemCut(){
        return cut;
    }

    public GemColor getGemColor(){
        return color;
    }

    public int getMaxChargeNormal() { return maxChargeNormal; }

    public int getMaxChargeUpgraded() { return maxChargeUpgraded; }

    protected String getTooltipData(ItemStack stack) {
        String str;
        if(GemUtil.hasUnlimitedCharge(stack))
            str = "[Unlimited]";
        else
            str = "["+GemUtil.getCharge(stack)+" / "+GemUtil.getMaxCharge(stack)+"]";

        byte upgrades = GemUtil.getUpgrades(stack);
        if((upgrades & UPGRADE_MATTER) == UPGRADE_MATTER)
            str += "   " + TextFormatting.GREEN + I18n.format("arcanearchives.tooltip.gemupgrade.matter");
        if((upgrades & UPGRADE_POWER) == UPGRADE_POWER)
            str += "   " + TextFormatting.RED + I18n.format("arcanearchives.tooltip.gemupgrade.power");
        if((upgrades & UPGRADE_SPACE) == UPGRADE_SPACE)
            str += "   " + TextFormatting.BLUE + I18n.format("arcanearchives.tooltip.gemupgrade.space");
        if((upgrades & UPGRADE_TIME) == UPGRADE_TIME)
            str += "   " + TextFormatting.RED + I18n.format("arcanearchives.tooltip.gemupgrade.time");
        return str;
    }

    /**
     * Used by the HUD element to determine whether to use the bar or the bar with toggle indicator
     * @return true if toggle indicator should be present
     */
    public boolean hasToggleMode() {
        return false;
    }

    /**
     * Retrieves the resource location for the gem's dun texture
     * @param cut The gem's cut
     * @return
     */
    protected ModelResourceLocation getDunGemResourceLocation(GemCut cut) {
        String loc = "arcanearchives:gems/";
        loc += cut.toString().toLowerCase()+"/dun";
        return new ModelResourceLocation(loc, "inventory");
    }

    /**
     * Retrieves the resource location for the gem's conflicted static texture
     * @param cut The gem's cut
     * @return
     */
    protected ModelResourceLocation getConflictGemResourceLocation(GemCut cut) {
        String loc = "arcanearchives:gems/";
        loc += cut.toString().toLowerCase()+"/static";
        return new ModelResourceLocation(loc, "inventory");
    }

    /**
     * Retrieves the resource location for the gem's textures
     * @param cut The gem's cut
     * @param color The gem's color spectrum
     * @return
     */
    protected ModelResourceLocation getChargedGemResourceLocation(GemCut cut, GemColor color) {
        String loc = "arcanearchives:gems/";
        loc += cut.toString().toLowerCase()+"/";
        loc += color.toString().toLowerCase();
        return new ModelResourceLocation(loc, "inventory");
    }

    /**
     * Sets up the models for both charged and dun states
     */
    @Override
    public void registerModels () {
        ModelResourceLocation charged = getChargedGemResourceLocation(cut, color);
        ModelResourceLocation conflict = getConflictGemResourceLocation(cut);
        ModelResourceLocation dun = getDunGemResourceLocation(cut);

        ModelBakery.registerItemVariants(this, charged, dun);

        ModelLoader.setCustomMeshDefinition(this, stack -> {
            if (GemUtil.isChargeEmpty(stack)) {
                return dun;
            } else if(false) {//TODO: Check for dupes in inventory
                return conflict;
            } else {
                return charged;
            }
        });
    }

    /**
     * Helper class that handles all of the NBT lookups
     */
    public static class GemUtil {

        /**
         * Sets the upgrades on a gem by applying a bitmask.
         * Matter=1, Power=2, Space=4, Time=8
         * @param stack The ItemStack to upgrade
         * @param upgrades The new upgrade bitmask to apply
         */
        public static void setUpgrades (ItemStack stack, byte upgrades) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            nbt.setByte("upgrades", upgrades);
        }

        /**
         * A human readable way to set upgrades on a gem.
         * @param stack The ItemStack to upgrade
         * @param matterUpgrade Whether to apply a Matter upgrade
         * @param powerUpgrade Whether to apply a Power upgrade
         * @param spaceUpgrade Whether to apply a Space upgrade
         * @param timeUpgrade Whether to apply a Time upgrade
         */
        public static void setUpgrades (ItemStack stack, boolean matterUpgrade, boolean powerUpgrade, boolean spaceUpgrade, boolean timeUpgrade) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            byte upgrades = 0;
            if(matterUpgrade)
                upgrades = (byte)(upgrades | UPGRADE_MATTER);
            if(powerUpgrade)
                upgrades = (byte)(upgrades | UPGRADE_POWER);
            if(spaceUpgrade)
                upgrades = (byte)(upgrades | UPGRADE_SPACE);
            if(timeUpgrade)
                upgrades = (byte)(upgrades | UPGRADE_TIME);
            nbt.setByte("upgrades", upgrades);
        }

        /**
         * Get a byte representing the upgrades on this gem as a bit mask
         * @param stack The ItemStack to check
         * @return
         */
        public static byte getUpgrades (ItemStack stack) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            if(nbt.hasKey("upgrades")) return nbt.getByte("upgrades");
            return 0;
        }

        /**
         * Check whether a particular gem has a specific upgrade type
         * @param stack The ItemStack to check
         * @param query The upgrade type to check for
         * @return
         */
        public static boolean hasUpgrade (ItemStack stack, GemUpgrades query) {
            byte upgrades = getUpgrades(stack);
            switch(query){
                case MATTER:
                    return (upgrades & UPGRADE_MATTER) == UPGRADE_MATTER;
                case POWER:
                    return (upgrades & UPGRADE_POWER) == UPGRADE_POWER;
                case SPACE:
                    return (upgrades & UPGRADE_SPACE) == UPGRADE_SPACE;
                case TIME:
                    return (upgrades & UPGRADE_TIME) == UPGRADE_TIME;
                default:
                    return false;
            }
        }

        /**
         * Get the maximum charge for this gem type, taking into account Power upgrades
         * @param stack The ItemStack to check
         * @return The gem's maximum charge
         */
        public static int getMaxCharge(ItemStack stack) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            boolean hasPowerUpgrade = hasUpgrade(stack, GemUpgrades.POWER);
            ArcaneGemItem item = (ArcaneGemItem) stack.getItem();
            if(hasPowerUpgrade)
                return item.getMaxChargeUpgraded();
            else
                return item.getMaxChargeNormal();
        }

        /**
         * Gets the current charge amount of a gem.
         * @param stack The ItemStack to check.
         * @return The amount of charge.
         */
        public static int getCharge(ItemStack stack) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            if(nbt.hasKey("charge")) {
                return nbt.getInteger("charge");
            } else {
                int maximum = getMaxCharge(stack);
                nbt.setInteger("charge", maximum);
                return maximum;
            }
        }

        /**
         * Gets the current charge amount of a gem as a value between 0..1
         * @param stack The ItemStack to check
         * @return The amount of charge.
         */
        public static float getChargePercent(ItemStack stack) {
            int current = getCharge(stack);
            int maximum = getMaxCharge(stack);

            return (float)current / (float)maximum;
        }

        /**
         * Restore the charge on the gem by a set amount.
         * @param amount How much to increase charge by. -1 to fill the gem outright.
         * @return true if the gem is full
         */
        public static boolean restoreCharge(ItemStack stack, int amount) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            int maximum = getMaxCharge(stack);
            int currentCharge;
            if (nbt.hasKey("charge"))
                currentCharge = nbt.getInteger("charge");
            else {
                currentCharge = maximum;
                nbt.setInteger("charge", currentCharge);
            }

            if(amount == -1) {
                currentCharge = maximum;
            }
            else {
                currentCharge += amount;
                if(currentCharge > maximum) currentCharge = maximum;
            }
            nbt.setInteger("charge", maximum);

            return currentCharge >= maximum;
        }

        /**
         * Reduce the charge on the gem by a set amount.
         * @param amount How much to reduce charge by. -1 to empty the gem outright.
         * @return true if charge remains, false if gem is now empty
         */
        public static boolean consumeCharge(ItemStack stack, int amount){
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            int currentCharge;
            if (nbt.hasKey("charge"))
                currentCharge = nbt.getInteger("charge");
            else {
                currentCharge = getMaxCharge(stack);
                nbt.setInteger("charge", currentCharge);
            }
            if(amount < -1)
                return currentCharge < 0;
            else if(amount == -1)
                currentCharge = 0;
            else {
                currentCharge -= amount;
                if (currentCharge < 0) currentCharge = 0;
            }
            nbt.setInteger("charge", currentCharge);
            return currentCharge > 0;
        }

        /**
         * Checks whether this gem has unlimited use.
         * @param stack The ItemStack to check
         * @return
         */
        public static boolean hasUnlimitedCharge(ItemStack stack) {
            if(getMaxCharge(stack) == 0) return true;

            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            if(nbt.hasKey("infinite")) return nbt.getBoolean("infinity");
            else {
                nbt.setBoolean("infinite", false);
                return false;
            }
        }

        public static boolean isChargeEmpty(ItemStack stack) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            if(!hasUnlimitedCharge(stack)) {
                return getCharge(stack) == 0;
            }
            else return false;
        }

        public static boolean isToggledOn(ItemStack stack) {
            NBTTagCompound nbt = NBTUtils.getOrCreateTagCompound(stack);
            if(!nbt.hasKey("toggle")) {
                nbt.setBoolean("toggle", false);
            }
            return nbt.getBoolean("toggle");
        }

        /**
         * Get gems that are capable of operating passively. Checks for held, gems slotted in a Fabrial's active or passive slots, and a gem in the Gem Socket.
         * @param player
         * @return List of appropriate gems
         */
        public static ArrayList<ItemStack> getAvailableGems(EntityPlayer player) {
            ArrayList<ItemStack> gems = new ArrayList<ItemStack>();

            //Held gems
            if(player.getHeldItemMainhand().getItem() instanceof ArcaneGemItem)
                gems.add(player.getHeldItemMainhand());
            if(player.getHeldItemOffhand().getItem() instanceof ArcaneGemItem)
                gems.add(player.getHeldItemOffhand());

            NonNullList<ItemStack> inv = player.inventory.mainInventory;
            //TODO: check for fabrial

            //TODO: check for socket bauble

            return gems;
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public EnumRarity getRarity (ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    public enum GemCut {
        NOCUT, ASSCHER, OVAL, PAMPEL, PENDELOQUE, TRILLION;

        /**
         * Converts a gem cut to a specific value. Used in packets.
         * @param cut The gem's cut
         */
        public static byte ToByte(GemCut cut) {
            if(cut == ASSCHER) return 1;
            if(cut == OVAL) return 2;
            if(cut == PAMPEL) return 3;
            if(cut == PENDELOQUE) return 4;
            if(cut == TRILLION) return 5;
            return 0;
        }

        /**
         * Converts a byte value into a specific gem cut value. Used in packets.
         * @param query The byte value to check
         * @return The gem's cut
         */
        public static GemCut fromByte(byte query) {
            if(query == 1) return ASSCHER;
            if(query == 2) return OVAL;
            if(query == 3) return PAMPEL;
            if(query == 4) return PENDELOQUE;
            if(query == 5) return TRILLION;
            return NOCUT;
        }
    }

    public enum GemColor {
        NOCOLOR, RED, ORANGE, YELLOW, GREEN, CYAN, BLUE, PURPLE, PINK, BLACK, WHITE;

        /**
         * Converts a gem cut to a specific value. Used in packets.
         * @param color The gem's color
         */
        public static byte ToByte(GemColor color) {
            if(color == RED) return 1;
            else if(color == ORANGE) return 2;
            else if(color == YELLOW) return 3;
            else if(color == GREEN) return 4;
            else if(color == CYAN) return 5;
            else if(color == BLUE) return 6;
            else if(color == PURPLE) return 7;
            else if(color == PINK) return 8;
            else if(color == BLACK) return 9;
            else if(color == WHITE) return 10;
            return 0;
        }

        /**
         * Converts a byte value into a specific color. Used in packets.
         * @param query The byte value to check
         * @return The color value
         */
        public static GemColor fromByte(byte query) {
            if(query == 1) return RED;
            else if(query == 2) return ORANGE;
            else if(query == 3) return YELLOW;
            else if(query == 4) return GREEN;
            else if(query == 5) return CYAN;
            else if(query == 6) return BLUE;
            else if(query == 7) return PURPLE;
            else if(query == 8) return PINK;
            else if(query == 9) return BLACK;
            else if(query == 10) return WHITE;
            return NOCOLOR;
        }

        public static Color getColor(GemColor color) {
            if(color == GemColor.RED)
                return new Color(1.00f, 0.50f, 0.50f, 1.0f);
            else if(color == GemColor.ORANGE)
                return new Color(1.00f, 0.75f, 0.50f, 1.0f);
            else if(color == GemColor.YELLOW)
                return new Color(1.00f, 1.00f, 0.50f, 1.0f);
            else if(color == GemColor.GREEN)
                return new Color(0.50f, 1.00f, 0.60f, 1.0f);
            else if(color == GemColor.CYAN)
                return new Color(0.50f, 1.00f, 1.00f, 1.0f);
            else if(color == GemColor.BLUE)
                return new Color(0.50f, 0.65f, 1.00f, 1.0f);
            else if(color == GemColor.PURPLE)
                return new Color(0.80f, 0.50f, 1.00f, 1.0f);
            else if(color == GemColor.PINK)
                return new Color(1.00f, 0.55f, 1.00f, 1.0f);
            else if(color == GemColor.BLACK)
                return new Color(0.00f, 0.00f, 0.00f, 1.0f);
            return new Color(1,1,1,1);
        }
    }

    public enum GemUpgrades {
        MATTER, POWER, SPACE, TIME
    }
}
