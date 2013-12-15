package es.amadornes.modjam3.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileEntityAntenna extends TileEntity {
	
	private boolean hasDish = false;
	private boolean isDouble = false;
	public static int dishRangeMultiplier = 4;
	public static int defaultRange = 8;
	
	public void setType(int type){
		if(type == 0){
			hasDish = false;
			isDouble = false;
		}else if(type == 1){
			hasDish = false;
			isDouble = true;
		}else if(type == 2){
			hasDish = true;
			isDouble = false;
		}else if(type == 3){
			hasDish = true;
			isDouble = true;
		}
	}
	
	public boolean hasDish(){
		return hasDish;
	}
	
	public boolean isDouble(){
		return isDouble;
	}
	
}
