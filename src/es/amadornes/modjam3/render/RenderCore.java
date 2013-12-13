package es.amadornes.modjam3.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import es.amadornes.modjam3.lib.ModInfo;
import es.amadornes.modjam3.tileentity.TileEntityCore;

public class RenderCore extends TileEntitySpecialRenderer implements IItemRenderer {
	
	private IModelCustom model = AdvancedModelLoader.loadModel("/assets/" + ModInfo.MOD_ID + "/model/core.obj");
	private ResourceLocation texture;
	private ResourceLocation texture_empty = new ResourceLocation(ModInfo.MOD_ID, "textures/model/core_empty.png");
	private ResourceLocation texture_items = new ResourceLocation(ModInfo.MOD_ID, "textures/model/core_items.png");
	private ResourceLocation texture_fluids = new ResourceLocation(ModInfo.MOD_ID, "textures/model/core_fluids.png");
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		TileEntityCore te = (TileEntityCore) tileentity;
		int metadata = te.blockMetadata;
		
		float rx = 0;
		float ry = 0;
		float rz = 0;
		
		switch(metadata){
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		}
		
		switch(te.getType()){
		case 0:
			texture = texture_empty;
			break;
		case 1:
			texture = texture_items;
			break;
		case 2:
			texture = texture_fluids;
			break;
		default:
			texture = texture_empty;
			break;
		}
		
		GL11.glPushMatrix();
			
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
			GL11.glTranslated(x, y, z);
	        GL11.glTranslated(0, 0, 1);
			model.renderAll();
			
		GL11.glPopMatrix();
	}

	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch(type){
		case ENTITY:
			return;
		case EQUIPPED:
			return;
		case EQUIPPED_FIRST_PERSON:
			return;
		case FIRST_PERSON_MAP:
			return;
		case INVENTORY:
			return;
		}
	}
	
}
