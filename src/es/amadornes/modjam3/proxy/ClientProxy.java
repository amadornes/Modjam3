package es.amadornes.modjam3.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

	public static int renderID = -1;
	
	@Override
	public void registerRenders() {
		registerBlockRenders();
		registerItemRenders();
	}
	
	public void registerBlockRenders(){
		renderID = RenderingRegistry.getNextAvailableRenderId();
	}
	
	public void registerItemRenders(){
		//MinecraftForgeClient.registerItemRenderer(itemID, renderer);
	}
	
}
