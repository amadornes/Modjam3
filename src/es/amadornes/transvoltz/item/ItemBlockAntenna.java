package es.amadornes.transvoltz.item;

import java.util.List;

import es.amadornes.transvoltz.Transvoltz;
import es.amadornes.transvoltz.lib.Blocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockAntenna extends ItemBlock {

	public ItemBlockAntenna(int id) {
		super(id);
		setUnlocalizedName(Blocks.antenna.getUnlocalizedName());
	}
	
	@Override
	public CreativeTabs getCreativeTab() {
		return Transvoltz.tab;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List l) {
		l.add(new ItemStack(Blocks.antenna, 1, 0));
		/*l.add(new ItemStack(Blocks.antenna, 1, 1));
		l.add(new ItemStack(Blocks.antenna, 1, 2));
		l.add(new ItemStack(Blocks.antenna, 1, 3));*/
	}
	
	@Override
	public boolean getHasSubtypes() {
		return true;
	}
	
	@Override
	public String getItemDisplayName(ItemStack is) {
		return "Antenna";
	}

}
