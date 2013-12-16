package es.amadornes.modjam3.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLightningBolt;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import es.amadornes.modjam3.pathfind.Path;
import es.amadornes.modjam3.pathfind.Vector3;

public class RenderHelper {

	public static void renderLightning(Fluid f, Path path, int progress){
		int pathLength = path.getSteps().size();
		
		GL11.glPushMatrix();
			double maxx = Double.MIN_VALUE;
			double maxy = Double.MIN_VALUE;
			double maxz = Double.MIN_VALUE;
			for(Vector3 vec : path.getSteps()){
				maxx = Math.max(maxx, vec.getX());
				maxy = Math.max(maxy, vec.getY());
				maxz = Math.max(maxz, vec.getZ());
			}
			Vector3 last = null;//TODO CAMBIAR MAX POR MIN SI FALLA
			int minq = Math.max(progress - pathLength, 0);
			int maxq = pathLength - Math.abs(Math.min(progress - pathLength, 0));
			System.out.println(minq + " - " + maxq);
			for(int q = minq; q < maxq; q++){
				Vector3 vec = path.getSteps().get(q);
				if(last != null){
					Tessellator tessellator = Tessellator.instance;
			        GL11.glDisable(GL11.GL_TEXTURE_2D);
			        GL11.glDisable(GL11.GL_LIGHTING);
			        GL11.glEnable(GL11.GL_BLEND);
			        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			        
                    tessellator.startDrawing(9);
	                    tessellator.setColorRGBA_F(1F, 1F, 1F, 1F);
	                    //tessellator.addVertex(last.getX() - maxx, last.getY() - maxy, last.getZ() - maxz);
	                    //tessellator.addVertex(vec.getX() - maxx, vec.getY() - maxy, vec.getZ() - maxz);
	                    tessellator.addVertex(0.5, 1, 0.5);
	                    tessellator.addVertex(0.5, 2, 0.5);
	                    tessellator.addVertex(0.5, 2, 0.5);
	                    tessellator.addVertex(0.5, 1, 0.5);
	                    System.out.println("Render");
                    tessellator.draw();
                    
			        GL11.glDisable(GL11.GL_BLEND);
			        GL11.glEnable(GL11.GL_LIGHTING);
			        GL11.glEnable(GL11.GL_TEXTURE_2D);
				}
				last = vec;
			}
			
		GL11.glPopMatrix();
	}
	
	public static ResourceLocation getFluidSheet(FluidStack liquid) {
		if (liquid == null)
			return TextureMap.locationBlocksTexture;
		return getFluidSheet(liquid.getFluid());
	}

	public static ResourceLocation getFluidSheet(Fluid liquid) {
		return TextureMap.locationBlocksTexture;
	}
	
}
