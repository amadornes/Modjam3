package es.amadornes.modjam3.proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import es.amadornes.modjam3.block.BlockCore;
import es.amadornes.modjam3.item.ItemUpgrade;
import es.amadornes.modjam3.lib.Blocks;
import es.amadornes.modjam3.lib.Ids;
import es.amadornes.modjam3.lib.Items;
import es.amadornes.modjam3.tileentity.TileEntityCore;

public class CommonProxy {

	public void assignIds(){
		Ids.core = 610;
		Ids.antenna = 611;
		Ids.upgrade = 612;
	}
	
	public void registerBlocks(){
		Blocks.core = new BlockCore(Ids.core);
		GameRegistry.registerBlock(Blocks.core, Blocks.core.getUnlocalizedName());
	}
	
	public void registerTileEntities(){
		GameRegistry.registerTileEntity(TileEntityCore.class, Blocks.core.getUnlocalizedName());
	}
	
	public void registerItems(){
		Items.upgrade = new ItemUpgrade(Ids.upgrade);
		GameRegistry.registerItem(Items.upgrade, Items.upgrade.getUnlocalizedName());
	}
	
	public void registerLanguages(){
		
	}
	
	public void registerRenders(){}
	
}
