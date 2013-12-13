package es.amadornes.modjam3.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import es.amadornes.modjam3.lib.ModInfo;
import es.amadornes.modjam3.proxy.ClientProxy;

public class BlockAntenna extends Block {
	
	public BlockAntenna(int id) {
		super(id, Material.iron);
		setUnlocalizedName(ModInfo.MOD_ID + ".antenna");
		setHardness(10F);
		setResistance(10F);
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return null;
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
		return ClientProxy.renderIDCore;
	}
	
}
