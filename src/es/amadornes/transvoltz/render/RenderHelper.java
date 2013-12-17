package es.amadornes.transvoltz.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import es.amadornes.transvoltz.pathfind.Path;
import es.amadornes.transvoltz.pathfind.Vector3;

public class RenderHelper {

	public static void renderLightning(Fluid f, Path path, int progress){
		int pathLength = path.getSteps().size();
		
		GL11.glPushMatrix();
			double minx = Double.MAX_VALUE;
			double miny = Double.MAX_VALUE;
			double minz = Double.MAX_VALUE;
			double maxx = Double.MIN_VALUE;
			double maxy = Double.MIN_VALUE;
			double maxz = Double.MIN_VALUE;
			for(Vector3 vec : path.getSteps()){
				minx = Math.min(minx, vec.getX());
				miny = Math.min(miny, vec.getY());
				minz = Math.min(minz, vec.getZ());
				maxx = Math.max(maxx, vec.getX());
				maxy = Math.max(maxy, vec.getY());
				maxz = Math.max(maxz, vec.getZ());
			}
			Vector3 firstOfPath = path.getSteps().get(0);
			GL11.glTranslated(-(firstOfPath.getX() - minx), -(firstOfPath.getY() - miny), -(firstOfPath.getZ() - minz));
			
			Vector3 last = null;
			int minq = Math.max(progress - pathLength, 0);
			int maxq = pathLength - Math.abs(Math.min(progress - pathLength, 0));
			for(int q = minq; q < maxq; q++){
				Vector3 vec = path.getSteps().get(q);
				if(last != null){
					Tessellator tessellator = Tessellator.instance;
			        GL11.glDisable(GL11.GL_TEXTURE_2D);
			        GL11.glDisable(GL11.GL_LIGHTING);
			        GL11.glEnable(GL11.GL_BLEND);
			        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                    GL11.glPushMatrix();
	                    tessellator.startDrawingQuads();
		                    tessellator.setColorRGBA_F(1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))));
		                    tessellator.addVertex(last.getX() - minx, last.getY() - miny - 0.0625, last.getZ() - minz);
		                    tessellator.addVertex(vec.getX() - minx, vec.getY() - miny - 0.0625, vec.getZ() - minz);
		                    tessellator.addVertex(vec.getX() - minx, vec.getY() - miny + 0.0625, vec.getZ() - minz);
		                    tessellator.addVertex(last.getX() - minx, last.getY() - miny + 0.0625, last.getZ() - minz);
		                tessellator.draw();
	        		GL11.glPopMatrix();
	                GL11.glPushMatrix();
		                tessellator.startDrawingQuads();
		                    tessellator.setColorRGBA_F(1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))));
		                    tessellator.addVertex(last.getX() - minx - 0.0625, last.getY() - miny, last.getZ() - minz);
		                    tessellator.addVertex(vec.getX() - minx - 0.0625, vec.getY() - miny, vec.getZ() - minz);
		                    tessellator.addVertex(vec.getX() - minx + 0.0625, vec.getY() - miny, vec.getZ() - minz);
		                    tessellator.addVertex(last.getX() - minx + 0.0625, last.getY() - miny, last.getZ() - minz);
		                tessellator.draw();
		    		GL11.glPopMatrix();
		            GL11.glPushMatrix();
			            tessellator.startDrawingQuads();
			                tessellator.setColorRGBA_F(1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))));
			                tessellator.addVertex(last.getX() - minx, last.getY() - miny, last.getZ() - minz - 0.0625);
			                tessellator.addVertex(vec.getX() - minx, vec.getY() - miny, vec.getZ() - minz - 0.0625);
			                tessellator.addVertex(vec.getX() - minx, vec.getY() - miny, vec.getZ() - minz + 0.0625);
			                tessellator.addVertex(last.getX() - minx, last.getY() - miny, last.getZ() - minz + 0.0625);
			            tessellator.draw();
					GL11.glPopMatrix();
	                GL11.glPushMatrix();
		                tessellator.startDrawingQuads();
		                    tessellator.setColorRGBA_F(1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))));
		                    tessellator.addVertex(last.getX() - minx, last.getY() - miny + 0.0625, last.getZ() - minz);
		                    tessellator.addVertex(vec.getX() - minx, vec.getY() - miny + 0.0625, vec.getZ() - minz);
		                    tessellator.addVertex(vec.getX() - minx, vec.getY() - miny - 0.0625, vec.getZ() - minz);
		                    tessellator.addVertex(last.getX() - minx, last.getY() - miny - 0.0625, last.getZ() - minz);
		                tessellator.draw();
		    		GL11.glPopMatrix();
		            GL11.glPushMatrix();
		                tessellator.startDrawingQuads();
		                    tessellator.setColorRGBA_F(1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))));
		                    tessellator.addVertex(last.getX() - minx + 0.0625, last.getY() - miny, last.getZ() - minz);
		                    tessellator.addVertex(vec.getX() - minx + 0.0625, vec.getY() - miny, vec.getZ() - minz);
		                    tessellator.addVertex(vec.getX() - minx - 0.0625, vec.getY() - miny, vec.getZ() - minz);
		                    tessellator.addVertex(last.getX() - minx - 0.0625, last.getY() - miny, last.getZ() - minz);
		                tessellator.draw();
		    		GL11.glPopMatrix();
		            GL11.glPushMatrix();
			            tessellator.startDrawingQuads();
			                tessellator.setColorRGBA_F(1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))), 1F - ((float)(0.8*(q/pathLength))));
			                tessellator.addVertex(last.getX() - minx, last.getY() - miny, last.getZ() - minz + 0.0625);
			                tessellator.addVertex(vec.getX() - minx, vec.getY() - miny, vec.getZ() - minz + 0.0625);
			                tessellator.addVertex(vec.getX() - minx, vec.getY() - miny, vec.getZ() - minz - 0.0625);
			                tessellator.addVertex(last.getX() - minx, last.getY() - miny, last.getZ() - minz - 0.0625);
			            tessellator.draw();
					GL11.glPopMatrix();
                    
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
