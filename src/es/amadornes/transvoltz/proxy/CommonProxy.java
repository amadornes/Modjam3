package es.amadornes.modjam3.proxy;

import cpw.mods.fml.common.registry.GameRegistry;
import es.amadornes.modjam3.block.BlockAntenna;
import es.amadornes.modjam3.block.BlockCore;
import es.amadornes.modjam3.block.ItemBlockAntenna;
import es.amadornes.modjam3.block.ItemBlockCore;
import es.amadornes.modjam3.item.ItemUpgrade;
import es.amadornes.modjam3.lib.Blocks;
import es.amadornes.modjam3.lib.Ids;
import es.amadornes.modjam3.lib.Items;
import es.amadornes.modjam3.tileentity.TileEntityAntenna;
import es.amadornes.modjam3.tileentity.TileEntityCore;

public class CommonProxy {

	public void assignIds(){
		Ids.core = 610;
		Ids.antenna = 611;
		Ids.upgrade = 612;
	}
	
	public void registerBlocks(){
		Blocks.core = new BlockCore(Ids.core);
		GameRegistry.registerBlock(Blocks.core, ItemBlockCore.class, Blocks.core.getUnlocalizedName());
		
		Blocks.antenna = new BlockAntenna(Ids.antenna);
		GameRegistry.registerBlock(Blocks.antenna, ItemBlockAntenna.class, Blocks.antenna.getUnlocalizedName());
	}
	
	public void registerTileEntities(){
		GameRegistry.registerTileEntity(TileEntityCore.class, Blocks.core.getUnlocalizedName());
		GameRegistry.registerTileEntity(TileEntityAntenna.class, Blocks.antenna.getUnlocalizedName());
	}
	
	public void registerItems(){
		Items.upgrade = new ItemUpgrade(Ids.upgrade);
		GameRegistry.registerItem(Items.upgrade, Items.upgrade.getUnlocalizedName());
	}
	
	public void registerLanguages(){
		
	}
	
	public void registerRenders(){}
	
}
