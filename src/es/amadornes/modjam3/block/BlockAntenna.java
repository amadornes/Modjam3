package es.amadornes.modjam3.block;

import java.util.List;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import es.amadornes.modjam3.lib.Blocks;
import es.amadornes.modjam3.lib.ModInfo;
import es.amadornes.modjam3.pathfind.Vector3;
import es.amadornes.modjam3.proxy.ClientProxy;
import es.amadornes.modjam3.tileentity.TileEntityAntenna;
import es.amadornes.modjam3.tileentity.TileEntityCore;

public class BlockAntenna extends BlockContainer {
	
	public BlockAntenna(int id) {
		super(id, Material.iron);
		setUnlocalizedName(ModInfo.MOD_ID + ".antenna");
		setHardness(10F);
		setResistance(10F);
	}
	
	@Override
	public int damageDropped(int metadata) {
		return 0;
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
	public int getRenderType() {
		return ClientProxy.renderIDAntenna;
	}
	
	@Override
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase entity, ItemStack item) {
		TileEntityAntenna antenna = (TileEntityAntenna) w.getBlockTileEntity(x, y, z);
		antenna.setType(item.getItemDamage());
	}
	
	@Override
	public boolean canBlockStay(World w, int x, int y, int z) {
		Vector3 rel = new Vector3(x, y, z, w).getRelative(ForgeDirection.getOrientation(w.getBlockMetadata(x, y, z)).getOpposite());
		TileEntity te = rel.getTileEntity();
		return te instanceof TileEntityCore &&
				ForgeDirection.getOrientation(w.getBlockMetadata(rel.getBlockX(), rel.getBlockY(), rel.getBlockZ())) ==
				ForgeDirection.getOrientation(w.getBlockMetadata(x, y, z));
	}
	
	@Override
	public void onNeighborBlockChange(World w, int x, int y, int z, int bid) {
		Vector3 rel = new Vector3(x, y, z, w).getRelative(ForgeDirection.getOrientation(w.getBlockMetadata(x, y, z)).getOpposite());
		TileEntity te = rel.getTileEntity();
		if(!(te instanceof TileEntityCore &&
				ForgeDirection.getOrientation(w.getBlockMetadata(rel.getBlockX(), rel.getBlockY(), rel.getBlockZ())) ==
				ForgeDirection.getOrientation(w.getBlockMetadata(x, y, z)))){
			dropBlockAsItem(w, x, y, z, w.getBlockMetadata(x, y, z), 1);
			w.setBlockToAir(x, y, z);
		}
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World w, int x, int y, int z, int side, ItemStack par6ItemStack) {
		Vector3 rel = new Vector3(x, y, z, w).getRelative(ForgeDirection.getOrientation(side).getOpposite());
		TileEntity te = rel.getTileEntity();
		return te instanceof TileEntityCore &&
				ForgeDirection.getOrientation(w.getBlockMetadata(rel.getBlockX(), rel.getBlockY(), rel.getBlockZ())) ==
				ForgeDirection.getOrientation(side);
	}
	
	@Override
	public int onBlockPlaced(World w, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta) {
		return side;
	}

	public TileEntity createNewTileEntity(World world) {
		return new TileEntityAntenna();
	}
	
	@Override
	public int getDamageValue(World par1World, int par2, int par3, int par4) {
		
		return 1;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List l) {
		l.add(new ItemStack(Blocks.antenna, 1, 0));
		l.add(new ItemStack(Blocks.antenna, 1, 1));
		l.add(new ItemStack(Blocks.antenna, 1, 2));
		l.add(new ItemStack(Blocks.antenna, 1, 3));
	}
	
}
