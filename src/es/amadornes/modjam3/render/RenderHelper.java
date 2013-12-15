package es.amadornes.modjam3.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import es.amadornes.modjam3.pathfind.Path;
import es.amadornes.modjam3.pathfind.Vector3;

public class RenderHelper {

	public static void renderLightning(Fluid f, Path path){
		Icon texture = f.getStillIcon();
		double uMin = texture.getInterpolatedU(0);
		double vMin = texture.getInterpolatedU(0);
		double uMax = texture.getInterpolatedU(texture.getIconWidth());
		double vMax = texture.getInterpolatedU(texture.getIconHeight());
		
		Minecraft.getMinecraft().renderEngine.bindTexture(getFluidSheet(f));
			
		Tessellator t = Tessellator.instance;
		GL11.glPushMatrix();
			double minx = 0;
			double miny = 0;
			double minz = 0;
			for(Vector3 vec : path.getSteps()){
				minx = Math.min(minx, vec.getX());
				miny = Math.min(miny, vec.getY());
				minz = Math.min(minz, vec.getZ());
			}
			Vector3 last = null;
			for(Vector3 vec : path.getSteps()){
				if(last != null){
					t.startDrawingQuads();
					t.addVertexWithUV(last.getX() - minx + 0.05, last.getX() - miny + 0.05, last.getX() - minz + 0.05, uMin, vMin);
					t.addVertexWithUV(vec.getX() - minx + 0.05, vec.getX() - miny + 0.05, vec.getX() - minz + 0.05, uMin, vMax);
					t.addVertexWithUV(vec.getX() - minx - 0.05, vec.getX() - miny - 0.05, vec.getX() - minz - 0.05, uMax, vMax);
					t.addVertexWithUV(last.getX() - minx - 0.05, last.getX() - miny - 0.05, last.getX() - minz - 0.05, uMax, vMin);
					t.draw();
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
