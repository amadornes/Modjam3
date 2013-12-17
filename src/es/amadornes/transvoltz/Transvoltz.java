package es.amadornes.transvoltz;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import es.amadornes.transvoltz.lib.Blocks;
import es.amadornes.transvoltz.lib.ModInfo;
import es.amadornes.transvoltz.packet.PacketHandler;
import es.amadornes.transvoltz.proxy.CommonProxy;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.NAME, version = ModInfo.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {ModInfo.CHANNEL}, packetHandler = PacketHandler.class)
public class Transvoltz {
	
	@Instance(ModInfo.MOD_ID)
	public static Transvoltz inst;
	
	@SidedProxy(serverSide = ModInfo.COMMON_PROXY, clientSide = ModInfo.CLIENT_PROXY)
	public static CommonProxy proxy;
	
	public static CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), ModInfo.MOD_ID){
		public ItemStack getIconItemStack() {
			return new ItemStack(Blocks.core, 1, 1);
		};
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent ev){
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent ev){
		proxy.assignIds();
		
		proxy.registerBlocks();
		proxy.registerTileEntities();
		proxy.registerItems();
		
		proxy.registerRecipes();
		
		proxy.registerLanguages();
		
		proxy.registerRenders();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent ev){
		
	}

}
