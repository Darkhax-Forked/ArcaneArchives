package com.aranaira.arcanearchives.proxy;

import com.aranaira.arcanearchives.ArcaneArchives;
import com.aranaira.arcanearchives.client.Keybinds;
import com.aranaira.arcanearchives.client.render.RadiantTankTEISR;
import com.aranaira.arcanearchives.client.render.RadiantTankTESR;
import com.aranaira.arcanearchives.data.NetworkHelper;
import com.aranaira.arcanearchives.init.BlockRegistry;
import com.aranaira.arcanearchives.items.itemblocks.RadiantTankItem;
import com.aranaira.arcanearchives.tileentities.RadiantTankTileEntity;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ArcaneArchives.MODID)
public class ClientProxy extends CommonProxy
{
	@SideOnly(Side.CLIENT)
	public static RadiantTankTESR tankTESR;
	@SideOnly(Side.CLIENT)
	public static RadiantTankTEISR itemTESR;

	@SubscribeEvent
	public static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		NetworkHelper.clearClientCache();
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void modelRegister (ModelRegistryEvent event) {
		RadiantTankTESR tesr = new RadiantTankTESR();
		ClientRegistry.bindTileEntitySpecialRenderer(RadiantTankTileEntity.class, tesr);
		RadiantTankItem item = (RadiantTankItem) BlockRegistry.RADIANT_TANK.getItemBlock();
		ModelResourceLocation mrl = new ModelResourceLocation(new ResourceLocation(ArcaneArchives.MODID, "dummy_builtin_blocktransforms"), "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
		item.setTileEntityItemStackRenderer(new RadiantTankTEISR(tesr));
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		OBJLoader.INSTANCE.addDomain(ArcaneArchives.MODID);

		Keybinds.initKeybinds();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void registerItemRenderer(@Nonnull Item item, int meta, String id) {
	}
}
