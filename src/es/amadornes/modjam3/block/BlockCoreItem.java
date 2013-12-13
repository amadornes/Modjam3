package es.amadornes.modjam3.block;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import es.amadornes.modjam3.lib.Blocks;

public class BlockCoreItem extends ItemBlock {

	public BlockCoreItem(int id) {
		super(id);
		setUnlocalizedName(Blocks.core.getUnlocalizedName());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List l) {
		l.add(new ItemStack(Blocks.core, 0, 1));
		l.add(new ItemStack(Blocks.core, 1, 1));
		l.add(new ItemStack(Blocks.core, 2, 1));
	}

}
