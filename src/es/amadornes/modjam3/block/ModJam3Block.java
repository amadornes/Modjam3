package es.amadornes.modjam3.block;

import java.util.List;

import es.amadornes.modjam3.proxy.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ModJam3Block extends Block {
	
	public ModJam3Block(int id) {
		super(id, Material.iron);
	}
	
	@Override
	public boolean hasTileEntity(int metadata) {
		if(metadata >= 0 && metadata < 4){
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		switch(metadata){
		case 0:
			return null;
		case 1:
			return null;
		case 2:
			return null;
		case 3:
			return null;
		}
		
		return null;
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@SuppressWarnings({"rawtypes"})
	@Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3,int par4, AxisAlignedBB par5AxisAlignedBB, List par6List,Entity par7Entity) {
		super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB,par6List, par7Entity);
	}
	
	@Override
	public Icon getIcon(int meta, int side) {
		if(Minecraft.getMinecraft().gameSettings.fancyGraphics)
			return null;
		
		//FIXME return icon if needed
		return null;
	}
	
	@Override
	public int getRenderType() {
		return ClientProxy.renderID;
	}
	
}
