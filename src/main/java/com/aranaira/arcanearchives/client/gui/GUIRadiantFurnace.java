package com.aranaira.arcanearchives.client.gui;

import com.aranaira.arcanearchives.config.ConfigHandler;
import com.aranaira.arcanearchives.inventory.ContainerRadiantFurnace;
import com.aranaira.arcanearchives.tileentities.RadiantFurnaceTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GUIRadiantFurnace extends GuiContainer {
	private static final ResourceLocation TEXTURE_RADIANTFURNACE = new ResourceLocation("arcanearchives:textures/gui/radiant_furnace.png");
	private static final ResourceLocation TEXTURE_RADIANTFURNACE_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/radiant_furnace.png");
	private static final ResourceLocation TEXTURE_PLAYERINV = new ResourceLocation("arcanearchives:textures/gui/player_inv.png");
	private static final ResourceLocation TEXTURE_PLAYERINV_SIMPLE = new ResourceLocation("arcanearchives:textures/gui/simple/player_inv.png");

	private static final int
		BG_X = 0,
		BG_Y = 0,
		BG_W = 102,
		BG_H = 70,
		BG_SHIFT = 38,
		INVENTORY_X = 0,
		INVENTORY_Y = 0,
		INVENTORY_W = 181,
		INVENTORY_H = 101,
		PADDING = 4;
	private static final int ImageScale = 256;

	private ContainerRadiantFurnace container;
	private InventoryPlayer playerinventory;
	private RadiantFurnaceTileEntity tile;

	public GUIRadiantFurnace(ContainerRadiantFurnace container, InventoryPlayer playerinventory) {
		super(container);

		this.container = container;

		this.playerinventory = playerinventory;
		this.tile = container.getTile();
		xSize = INVENTORY_W;
		ySize = BG_H + INVENTORY_H + PADDING;
	}

	@Override
	public void initGui () {
		super.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer (float partialTicks, int mouseX, int mouseY) {
		if (ConfigHandler.UsePrettyGUIs) {
			mc.getTextureManager().bindTexture(TEXTURE_RADIANTFURNACE);
		} else {
			mc.getTextureManager().bindTexture(TEXTURE_RADIANTFURNACE_SIMPLE);
		}
		drawModalRectWithCustomSizedTexture(guiLeft + BG_SHIFT, guiTop, BG_X, BG_Y, BG_W, BG_H, ImageScale, ImageScale);

		if (ConfigHandler.UsePrettyGUIs) {
			mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV);
		} else {
			mc.getTextureManager().bindTexture(TEXTURE_PLAYERINV_SIMPLE);
		}
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop + BG_H + PADDING, INVENTORY_X, INVENTORY_Y, INVENTORY_W, INVENTORY_H, ImageScale, ImageScale);
	}

	@Override
	public void drawScreen (int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		/*int i = this.guiLeft;
		int j = this.guiTop;
		this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);*/

		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void renderHoveredToolTip (int mouseX, int mouseY) {
		super.renderHoveredToolTip(mouseX, mouseY);
	}
}
