package es.amadornes.modjam3.block;

import java.util.List;

import es.amadornes.modjam3.lib.Blocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlockAntennaItem extends ItemBlock {

	public BlockAntennaItem(int id) {
		super(id);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List l) {
		l.add(new ItemStack(Blocks.core, 0, 1));
		l.add(new ItemStack(Blocks.core, 1, 1));
	}

}
