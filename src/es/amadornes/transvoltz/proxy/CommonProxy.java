package es.amadornes.transvoltz.proxy;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import es.amadornes.transvoltz.block.BlockAntenna;
import es.amadornes.transvoltz.block.BlockCore;
import es.amadornes.transvoltz.item.ItemBlockAntenna;
import es.amadornes.transvoltz.item.ItemBlockCore;
import es.amadornes.transvoltz.item.ItemUpgrade;
import es.amadornes.transvoltz.lib.Blocks;
import es.amadornes.transvoltz.lib.Ids;
import es.amadornes.transvoltz.lib.Items;
import es.amadornes.transvoltz.lib.ModInfo;
import es.amadornes.transvoltz.tileentity.TileEntityAntenna;
import es.amadornes.transvoltz.tileentity.TileEntityCore;

public class CommonProxy {
	
	Configuration config;
	
	public void assignIds(){
		config = new Configuration(new File("config/" + ModInfo.MOD_ID + ".cfg"));
		config.load();
		Ids.core = config.get("blocks", "core", 610).getInt();
		Ids.antenna = config.get("blocks", "antenna", 611).getInt();
		Ids.upgrade = config.get("item", "upgrade", 612).getInt();
		config.save();
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
	
	public void registerRecipes(){
		GameRegistry.addRecipe(new ItemStack(Blocks.core, 1, 0), "III", "LLL", "III", 'I', new ItemStack(Item.ingotIron, 1), 'L', new ItemStack(Item.dyePowder, 1, 4));
		GameRegistry.addRecipe(new ItemStack(Blocks.core, 1, 1), "III", "RRR", "III", 'I', new ItemStack(Item.ingotIron, 1), 'R', new ItemStack(Item.dyePowder, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.core, 1, 2), new ItemStack(Blocks.core, 1, 0), new ItemStack(Blocks.core, 1, 1));//Recipe(new ItemStack(Blocks.core, 1, 0), "III", "CCC", "III", 'I', new ItemStack(Item.ingotIron, 1), 'C', new ItemStack(Item.dyePowder, 1, 2));
		GameRegistry.addRecipe(new ItemStack(Blocks.antenna, 1, 0), "I", "I", "I", 'I', new ItemStack(Item.ingotIron, 1));

		GameRegistry.addRecipe(new ItemStack(Items.upgrade, 1, 0), " I ", "IRI", " I ", 'I', new ItemStack(Item.ingotIron, 1), 'R', new ItemStack(Block.blockRedstone, 1));
		GameRegistry.addRecipe(new ItemStack(Items.upgrade, 1, 1), " R ", "RUR", " R ", 'R', new ItemStack(Block.blockRedstone, 1), 'U', new ItemStack(Items.upgrade, 1, 0));
		
		GameRegistry.addRecipe(new ItemStack(Items.upgrade, 1, 2), " G ", "RUR", " G ", 'G', new ItemStack(Item.glowstone, 1), 'R', new ItemStack(Block.blockRedstone, 1), 'U', new ItemStack(Items.upgrade, 1, 0));
		GameRegistry.addRecipe(new ItemStack(Items.upgrade, 1, 2), " R ", "GUG", " R ", 'G', new ItemStack(Item.glowstone, 1), 'R', new ItemStack(Block.blockRedstone, 1), 'U', new ItemStack(Items.upgrade, 1, 0));
		
		GameRegistry.addRecipe(new ItemStack(Items.upgrade, 1, 3), " G ", "LUL", " G ", 'G', new ItemStack(Item.glowstone, 1), 'L', new ItemStack(Block.blockLapis, 1), 'U', new ItemStack(Items.upgrade, 1, 0));
		GameRegistry.addRecipe(new ItemStack(Items.upgrade, 1, 3), " L ", "GUG", " L ", 'G', new ItemStack(Item.glowstone, 1), 'L', new ItemStack(Block.blockLapis, 1), 'U', new ItemStack(Items.upgrade, 1, 0));
	}
	
	public void registerLanguages(){
		LanguageRegistry.instance().addStringLocalization("itemGroup." + ModInfo.MOD_ID, "en_US", ModInfo.NAME);
	}
	
	public void registerRenders(){}
	
}
