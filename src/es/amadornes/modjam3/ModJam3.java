package es.amadornes.modjam3;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import es.amadornes.modjam3.lib.ModInfo;
import es.amadornes.modjam3.proxy.CommonProxy;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.NAME, version = ModInfo.VERSION, modLanguage = "java")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {ModInfo.NAME})
public class ModJam3 {
	
	@Instance(ModInfo.MOD_ID)
	public static ModJam3 inst;
	
	@SidedProxy(serverSide = ModInfo.COMMON_PROXY, clientSide = ModInfo.CLIENT_PROXY)
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent ev){
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent ev){
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent ev){
		
	}

}
