package es.amadornes.modjam3.block;

import java.util.List;

import es.amadornes.modjam3.lib.Blocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCore extends ItemBlock {

	public ItemBlockCore(int id) {
		super(id);
		setUnlocalizedName(Blocks.core.getUnlocalizedName());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List l) {
		l.add(new ItemStack(Blocks.core, 1, 0));
		l.add(new ItemStack(Blocks.core, 1, 1));
		l.add(new ItemStack(Blocks.core, 1, 2));
	}
	
	@Override
	public boolean getHasSubtypes() {
		return true;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is) {
		return getUnlocalizedName() + "." + is.getItemDamage();
	}

}
