package es.amadornes.modjam3.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
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
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase entity, ItemStack item) {
		TileEntityCore core = (TileEntityCore) w.getBlockTileEntity(x, y, z);
		core.setType(item.getItemDamage());
		super.onBlockPlacedBy(w, x, y, z, entity, item);
	}
	
	@Override
	public int onBlockPlaced(World w, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta) {
		return side;
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
			ItemStack item = player.inventory.getCurrentItem();
			if(item != null){
				if(item.getItem() == Items.upgrade){
					if(item.getItemDamage() > 0){
						TileEntityCore core = (TileEntityCore) w.getBlockTileEntity(x, y, z);
						boolean can = core.installUpgrade(UpgradeType.values()[item.getItemDamage()], ForgeDirection.getOrientation(side));
						core.setUpgrade(UpgradeType.values()[item.getItemDamage()], ForgeDirection.getOrientation(side));
						if(can && !player.capabilities.isCreativeMode){
							if(item.stackSize - 1 == 0){
								player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
							}else{
								item.stackSize--;
							}
						}
						return can;
					}
				}
			}else{
				if(player.isSneaking()){
					TileEntityCore core = (TileEntityCore) w.getBlockTileEntity(x, y, z);
					ForgeDirection fd = ForgeDirection.getOrientation(side);
					ItemStack upgrade = core.getUpgradeOnSide(fd);
					if(upgrade != null){
						EntityItem entityitem = new EntityItem(w, (double)((float)x + 0.5 + fd.offsetX), (double)((float)y + 0.5 + fd.offsetY), (double)((float)z + 0.5 + fd.offsetZ), upgrade);
						
						core.removeUpgradeOnSide(fd);
						
		                float f3 = 0.05F;
		                entityitem.motionY = (double)((float)new Random().nextGaussian() * f3 + 0.2F);
		                w.spawnEntityInWorld(entityitem);
					}
				}
			}
		}
		
		return super.onBlockActivated(w, x, y, z, player, side, hitX, hitY, hitZ);
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		TileEntityCore core = (TileEntityCore) world.getBlockTileEntity(x, y, z);
		if(core.isRepeater()){
			return new ItemStack(this, 1, 2);
		}else{
			if(!core.isInput()){
				return new ItemStack(this, 1, 1);
			}else{
				return new ItemStack(this, 1, 0);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List l) {
		l.add(new ItemStack(this, 1, 0));
		l.add(new ItemStack(this, 1, 1));
		l.add(new ItemStack(this, 1, 2));
	}
	
}
