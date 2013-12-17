package es.amadornes.modjam3.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import es.amadornes.modjam3.lib.ModInfo;
import es.amadornes.modjam3.tileentity.TileEntityCore.UpgradeType;

public class ItemUpgrade extends Item {
	
	private Icon[] icons = new Icon[]{};

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
		for(int i = 0; i < UpgradeType.values().length; i++){
			l.add(new ItemStack(this, 1, i));
		}
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
		icons = new Icon[UpgradeType.values().length];
		for(int i = 0; i < UpgradeType.values().length; i++){
			icons[i] = reg.registerIcon(ModInfo.MOD_ID + ":upgrade_" + UpgradeType.getFromDamage(i).getIconName());
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is) {
		return getUnlocalizedName() + "." + UpgradeType.getFromDamage(is.getItemDamage()).getIconName();
	}

}
