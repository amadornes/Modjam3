package es.amadornes.modjam3.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import es.amadornes.modjam3.lib.ModInfo;
import es.amadornes.modjam3.proxy.ClientProxy;
import es.amadornes.modjam3.tileentity.TileEntityCore;

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
	
}
