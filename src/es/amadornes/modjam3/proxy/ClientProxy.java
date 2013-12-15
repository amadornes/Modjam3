package es.amadornes.modjam3.proxy;

import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import es.amadornes.modjam3.lib.Ids;
import es.amadornes.modjam3.render.RenderAntenna;
import es.amadornes.modjam3.render.RenderCore;
import es.amadornes.modjam3.tileentity.TileEntityAntenna;
import es.amadornes.modjam3.tileentity.TileEntityCore;

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
