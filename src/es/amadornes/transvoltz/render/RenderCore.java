package es.amadornes.transvoltz.render;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import es.amadornes.transvoltz.lib.ModInfo;
import es.amadornes.transvoltz.pathfind.Path;
import es.amadornes.transvoltz.tileentity.TileEntityCore;

public class RenderCore extends TileEntitySpecialRenderer implements IItemRenderer {
	
	private IModelCustom model = AdvancedModelLoader.loadModel("/assets/" + ModInfo.MOD_ID + "/model/core.obj");
	private ResourceLocation texture_base = new ResourceLocation(ModInfo.MOD_ID, "textures/model/core_base.png");

	private ResourceLocation texture_input = new ResourceLocation(ModInfo.MOD_ID, "textures/model/core_input.png");
	private ResourceLocation texture_output = new ResourceLocation(ModInfo.MOD_ID, "textures/model/core_output.png");
	private ResourceLocation texture_repeater = new ResourceLocation(ModInfo.MOD_ID, "textures/model/core_repeater.png");
	
	private ResourceLocation texture_empty = new ResourceLocation(ModInfo.MOD_ID, "textures/model/core_empty.png");
	private ResourceLocation texture_items = new ResourceLocation(ModInfo.MOD_ID, "textures/model/core_items.png");
	private ResourceLocation texture_fluids = new ResourceLocation(ModInfo.MOD_ID, "textures/model/core_fluids.png");
	
	private int lastAnimationTick = 0;
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
		TileEntityCore te = (TileEntityCore) tileentity;
		int metadata = te.blockMetadata;

		float rx = 0;
		float ry = 0;
		float rz = 0;
		float tx = 0;
		float ty = 0;
		float tz = 0;
		
		switch(metadata){
		case 0:
			break;
		case 1:
			rx = 180;
			tz = -1;
			ty = 1;
			break;
		case 2:
			rx = 90;
			tz = -1;
			break;
		case 3:
			rx = -90;
			ty = 1;
			break;
		case 4:
			rz = -90;
			ty = 1;
			break;
		case 5:
			rz = 90;
			tx = 1;
			break;
		}
		
		int type = te.getType();
		
		GL11.glPushMatrix();
			
			GL11.glTranslated(x, y, z);
	        GL11.glTranslated(0, 0, 1);
	        GL11.glTranslated(tx, ty, tz);
	        GL11.glRotated(rx, 1, 0, 0);
	        GL11.glRotated(ry, 0, 1, 0);
	        GL11.glRotated(rz, 0, 0, 1);

			FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_base);
			model.renderAll();
			
			if(te.isRepeater()){
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_repeater);
			}else{
				if(te.isReceiver()){
					FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_input);
				}else{
					FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_output);
				}
			}
			model.renderAll();
			
			switch(type){
			case 0:
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_empty);
				break;
			case 1:
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_items);
				break;
			case 2:
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_fluids);
				break;
			default:
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_empty);
				break;
			}
			model.renderAll();
			
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
	        GL11.glTranslated(0, 0, 1);
	        GL11.glTranslated(tx, ty, tz);
			switch(te.blockMetadata){
			case 1:
				GL11.glTranslated(0, -1, 1);
				break;
			case 2:
				GL11.glTranslated(0, 0, 1);
				break;
			case 3:
				GL11.glTranslated(0, -1, 0);
				break;
			case 4:
				GL11.glTranslated(0, -1, 0);
				break;
			case 5:
				GL11.glTranslated(-1, 0, 0);
				break;
			}
			renderUpgrades(te);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
			
			renderLightning(te, x, y, z);
		
		GL11.glPopMatrix();
	}
	
	private void renderLightning(TileEntityCore te, double x, double y, double z){
		Map<Path, Integer> list = te.getLightningEffects();
		for(Path p : list.keySet()){
			GL11.glPushMatrix();
				GL11.glTranslated(x, y, z);
		        GL11.glTranslated(0.5, 0.5, 0.5);
				GL11.glTranslated(0, 1, 0);
				RenderHelper.renderLightning(null, p, list.get(p));
			GL11.glPopMatrix();
		}
	}
	
	private void renderUpgrades(TileEntityCore te){
		GL11.glPushMatrix();
			GL11.glRotated(90, 1, 0, 0);
			GL11.glScaled(0.5, 0.5, 0.5);
			GL11.glTranslated(0.5, -1.5, -1.75);
			renderItem(te.getUpgradeOnSide(ForgeDirection.UP));
		GL11.glPopMatrix();
		GL11.glPushMatrix();
			GL11.glRotated(-90, 1, 0, 0);
			GL11.glScaled(0.5, 0.5, 0.5);
			GL11.glTranslated(0.5, 0.5, 0.25);
			renderItem(te.getUpgradeOnSide(ForgeDirection.DOWN));
		GL11.glPopMatrix();
		GL11.glPushMatrix();
			GL11.glScaled(0.5, 0.5, 0.5);
			GL11.glTranslated(0.5, 0.5, -1.75);
			renderItem(te.getUpgradeOnSide(ForgeDirection.NORTH));
		GL11.glPopMatrix();
		GL11.glPushMatrix();
			GL11.glRotated(180, 0, 1, 0);
			GL11.glScaled(0.5, 0.5, 0.5);
			GL11.glTranslated(-1.5, 0.5, 0.25);
			renderItem(te.getUpgradeOnSide(ForgeDirection.SOUTH));
		GL11.glPopMatrix();
		GL11.glPushMatrix();
			GL11.glRotated(-90, 0, 1, 0);
			GL11.glScaled(0.5, 0.5, 0.5);
			GL11.glTranslated(-1.5, 0.5, -1.75);
			renderItem(te.getUpgradeOnSide(ForgeDirection.EAST));
		GL11.glPopMatrix();
		GL11.glPushMatrix();
			GL11.glRotated(90, 0, 1, 0);
			GL11.glScaled(0.5, 0.5, 0.5);
			GL11.glTranslated(0.5, 0.5, 0.25);
			renderItem(te.getUpgradeOnSide(ForgeDirection.WEST));
		GL11.glPopMatrix();
	}
	
	private void renderItem(ItemStack item){
		if(item != null){
			float divider = 6F;
			
			TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
			
			texturemanager.bindTexture(texturemanager.getResourceLocation(item.getItemSpriteNumber()));
            Tessellator tessellator = Tessellator.instance;
            float f = item.getIconIndex().getMinU();
            float f1 = item.getIconIndex().getMaxU();
            float f2 = item.getIconIndex().getMinV();
            float f3 = item.getIconIndex().getMaxV();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, item.getIconIndex().getIconWidth(), item.getIconIndex().getIconHeight(), 0.0625F);
	        
	        lastAnimationTick++;
	        if(lastAnimationTick == 16 * divider)
	        	lastAnimationTick = 0;
		}
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
			render(-0.5, 1, -0.5, 180, 0, 0, 1, item.getItemDamage());
			return;
		case EQUIPPED:
			render(0, 1, 0, 180, 0, 0, 1, item.getItemDamage());
			return;
		case EQUIPPED_FIRST_PERSON:
			render(0, 1, 0, 180, 0, 0, 1, item.getItemDamage());
			return;
		case FIRST_PERSON_MAP:
			return;
		case INVENTORY:
			render(0, 0.9, 0, 180, 0, 0, 1, item.getItemDamage());
			return;
		}
	}
	
	public void render(double x, double y, double z, double rx, double ry, double rz, double scale, int metadata){
		boolean isInput = metadata == 0;
		boolean isRepeater = metadata == 3;
		
		GL11.glPushMatrix();
			
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_empty);
			GL11.glScaled(scale, scale, scale);
			
			GL11.glTranslated(x, y, z);
			GL11.glRotated(rx, 1, 0, 0);
			GL11.glRotated(ry, 0, 1, 0);
			GL11.glRotated(rz, 0, 0, 1);
			
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_base);
			model.renderAll();
			
			if(isRepeater){
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_repeater);
			}else{
				if(isInput){
					FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_input);
				}else{
					FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_output);
				}
			}
			model.renderAll();
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture_empty);
			model.renderAll();
			
		GL11.glPopMatrix();
	}
	
}
