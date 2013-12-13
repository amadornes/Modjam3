package es.amadornes.modjam3.proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import es.amadornes.modjam3.block.BlockCore;
import es.amadornes.modjam3.block.BlockCoreItem;
import es.amadornes.modjam3.lib.Blocks;
import es.amadornes.modjam3.lib.Ids;
import es.amadornes.modjam3.tileentity.TileEntityCore;

public class CommonProxy {

	public void assignIds(){
		Ids.core = 612;
	}
	
	public void registerBlocks(){
		Blocks.core = new BlockCore(Ids.core);
		GameRegistry.registerBlock(Blocks.core, BlockCoreItem.class, Blocks.core.getUnlocalizedName());
	}
	
	public void registerTileEntities(){
		GameRegistry.registerTileEntity(TileEntityCore.class, Blocks.core.getUnlocalizedName());
	}
	
	public void registerItems(){
		
	}
	
	public void registerLanguages(){
		
	}
	
	public void registerRenders(){}
	
}
