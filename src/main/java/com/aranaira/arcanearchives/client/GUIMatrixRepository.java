package com.aranaira.arcanearchives.client;

import org.lwjgl.opengl.GL11;

import com.aranaira.arcanearchives.common.ContainerGemcuttersTable;
import com.aranaira.arcanearchives.common.ContainerManifest;
import com.aranaira.arcanearchives.common.ContainerMatrixRepository;
import com.aranaira.arcanearchives.common.ContainerMatrixStorage;
import com.aranaira.arcanearchives.common.ContainerRadiantCraftingTable;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class GUIMatrixRepository extends GuiContainer
{

	private static final ResourceLocation GUITextures = new ResourceLocation("arcanearchives:textures/gui/matrix_repository.png");

	
	public GUIMatrixRepository(EntityPlayer player, ContainerMatrixRepository container)
	{
		super(container);
		this.xSize = 223;
		this.ySize = 246;
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) 
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableColorMaterial();
		this.mc.getTextureManager().bindTexture(GUITextures);

		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, 256,256,256,256);
	}
	
	@Override
	public boolean doesGuiPauseGame() 
	{
		return false;
	}
}