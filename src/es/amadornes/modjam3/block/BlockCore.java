package es.amadornes.modjam3.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import es.amadornes.modjam3.lib.Items;
import es.amadornes.modjam3.lib.ModInfo;
import es.amadornes.modjam3.proxy.ClientProxy;
import es.amadornes.modjam3.tileentity.TileEntityCore;
import es.amadornes.modjam3.tileentity.TileEntityCore.UpgradeType;

public class BlockCore extends BlockContainer {
	
	public BlockCore(int id) {
		super(id, Material.iron);
		setUnlocalizedName(ModInfo.MOD_ID + ".core");
		setHardness(10F);
		setResistance(10F);
	}

	public TileEntity createNewTileEntity(World world) {
		return new TileEntityCore();
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public Icon getIcon(int meta, int side) {
		return null;
	}
	
	@Override
	public int getRenderType() {
		return ClientProxy.renderIDCore;
	}
	
	@Override
	public int onBlockPlaced(World w, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta) {
		return side;
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		ItemStack item = player.inventory.getCurrentItem();
		if(item != null){
			if(item.getItem() == Items.upgrade){
				if(item.getItemDamage() > 0){
					TileEntityCore core = (TileEntityCore) w.getBlockTileEntity(x, y, z);
					boolean can = core.installUpgrade(UpgradeType.values()[item.getItemDamage()], ForgeDirection.getOrientation(side));
					if(can){
						core.setUpgrade(UpgradeType.values()[item.getItemDamage()], ForgeDirection.getOrientation(side));
						if(item.stackSize - 1 == 0){
							player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
						}else{
							item.stackSize--;
						}
					}
					return can;
				}
			}
		}
		
		return super.onBlockActivated(w, x, y, z, player, side, hitX, hitY, hitZ);
	}
	
}
