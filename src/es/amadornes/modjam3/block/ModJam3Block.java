package es.amadornes.modjam3.block;

import java.util.List;

import es.amadornes.modjam3.lib.ModInfo;
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
		setUnlocalizedName(ModInfo.MOD_ID + ".modjam3block");
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
		if(metadata >= 0 && metadata < 6){
			return null;
		}
		switch(metadata){
		case 6:
			return null;
		case 7:
			return null;
		case 8:
			return null;
		}
		
		return null;
	}
	
	@Override
	public int damageDropped(int metadata) {
		if(metadata >= 0 && metadata < 6){
			return 0;
		}
		return metadata;
	}
	
	@Override
	public float getBlockHardness(World w, int x, int y, int z) {
		int metadata = w.getBlockMetadata(x, y, z);
		if(metadata >= 0 && metadata < 6){
			return 10F;
		}
		switch(metadata){
		case 6:
			return 10F;
		case 7:
			return 10F;
		case 8:
			return 10F;
		}
		return 0;
	}
	
	@Override
	public float getExplosionResistance(Entity par1Entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
		int metadata = world.getBlockMetadata(x, y, z);
		if(metadata >= 0 && metadata < 6){
			return 10F;
		}
		switch(metadata){
		case 6:
			return 10F;
		case 7:
			return 10F;
		case 8:
			return 10F;
		}
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
