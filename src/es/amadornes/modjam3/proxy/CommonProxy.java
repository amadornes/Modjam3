package es.amadornes.modjam3.proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import es.amadornes.modjam3.block.ModJam3Block;
import es.amadornes.modjam3.block.ModJam3ItemBlock;
import es.amadornes.modjam3.lib.Blocks;
import es.amadornes.modjam3.lib.Ids;

public class CommonProxy {

	public void assignIds(){
		Ids.modJam3Block = 612;
	}
	
	public void registerBlocks(){
		Blocks.modJam3Block = new ModJam3Block(Ids.modJam3Block);
		GameRegistry.registerBlock(Blocks.modJam3Block, ModJam3ItemBlock.class, Blocks.modJam3Block.getUnlocalizedName());
	}
	
	public void registerTileEntities(){
		
	}
	
	public void registerItems(){
		
	}
	
	public void registerLanguages(){
		
	}
	
	public void registerRenders(){}
	
}
