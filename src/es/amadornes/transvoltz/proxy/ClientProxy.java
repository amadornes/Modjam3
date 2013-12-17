package es.amadornes.transvoltz.proxy;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import es.amadornes.transvoltz.lib.Ids;
import es.amadornes.transvoltz.render.RenderAntenna;
import es.amadornes.transvoltz.render.RenderCore;
import es.amadornes.transvoltz.tileentity.TileEntityAntenna;
import es.amadornes.transvoltz.tileentity.TileEntityCore;

public class ClientProxy extends CommonProxy {

	public static int renderIDCore = -1;
	public static int renderIDAntenna = -1;
	
	@Override
	public void registerRenders() {
		registerBlockRenders();
		registerItemRenders();
	}
	
	public void registerBlockRenders(){
		renderIDCore = RenderingRegistry.getNextAvailableRenderId();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCore.class, new RenderCore());
		
		renderIDCore = RenderingRegistry.getNextAvailableRenderId();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAntenna.class, new RenderAntenna());
	}
	
	public void registerItemRenders(){
		MinecraftForgeClient.registerItemRenderer(Ids.core, new RenderCore());
		MinecraftForgeClient.registerItemRenderer(Ids.antenna, new RenderAntenna());
	}
	
}
