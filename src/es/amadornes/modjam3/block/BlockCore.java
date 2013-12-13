package es.amadornes.modjam3.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import es.amadornes.modjam3.lib.ModInfo;
import es.amadornes.modjam3.proxy.ClientProxy;
import es.amadornes.modjam3.tileentity.TileEntityCore;

public class BlockCore extends Block {
	
	public BlockCore(int id) {
		super(id, Material.iron);
		setUnlocalizedName(ModInfo.MOD_ID + ".core");
		setHardness(10F);
		setResistance(10F);
	}
	
	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityCore();
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
	public Icon getIcon(int meta, int side) {
		if(Minecraft.getMinecraft().gameSettings.fancyGraphics)
			return null;
		
		//FIXME return icon if needed
		return null;
	}
	
	@Override
	public int getRenderType() {
		return ClientProxy.renderIDCore;
	}
	
	@Override
	public int getDamageValue(World w, int x, int y, int z) {
		return 0;
	}
	
	@Override
	public int onBlockPlaced(World w, int x, int y, int z, int meta, float par6, float par7, float par8, int par9) {
		return meta;
	}
	
}
