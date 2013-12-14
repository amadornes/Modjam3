package es.amadornes.modjam3.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import es.amadornes.modjam3.lib.ModInfo;

public class ItemUpgrade extends Item {
	
	private Icon[] icons = new Icon[2];

	public ItemUpgrade(int id) {
		super(id);
		setUnlocalizedName(ModInfo.MOD_ID + ".upgrade");
	}
	
	@Override
	public boolean getHasSubtypes() {
		return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List l) {
		l.add(new ItemStack(this, 1, 0));
		l.add(new ItemStack(this, 1, 1));
	}
	
	@Override
	public Icon getIconFromDamage(int meta) {
		try{
			return icons[meta];
		}catch(Exception e){}
		return icons[0];
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		icons[0] = reg.registerIcon(ModInfo.MOD_ID + ":upgrade_empty");
		icons[1] = reg.registerIcon(ModInfo.MOD_ID + ":upgrade_overclock");
	}

}
