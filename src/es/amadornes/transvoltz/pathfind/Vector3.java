package es.amadornes.modjam3.pathfind;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class Vector3 {
	
	private double x, y, z;
	private World w;
	
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(double x, double y, double z, World w) {
		this(x, y, z);
		this.w = w;
	}
	
	public Vector3(TileEntity te){
		this(te.xCoord, te.yCoord, te.zCoord, te.worldObj);
	}
	
	public boolean hasWorld(){
		return w != null;
	}
	
	public Vector3 add(double x, double y, double z){
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public Vector3 add(ForgeDirection dir){
		this.x += dir.offsetX;
		this.y += dir.offsetY;
		this.z += dir.offsetZ;
		return this;
	}
	
	public Vector3 subtract(double x, double y, double z){
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	
	public Vector3 getRelative(double x, double y, double z){
		return clone().add(x, y, z);
	}
	
	public Vector3 getRelative(ForgeDirection dir){
		return getRelative(dir.offsetX, dir.offsetY, dir.offsetZ);
	}
	
	public Vector3 clone(){
		return new Vector3(x, y, z, w);
	}
	
	public boolean hasTileEntity(){
		if(hasWorld()){
			return w.getBlockTileEntity((int)x, (int)y, (int)z) != null;
		}
		return false;
	}
	
	public TileEntity getTileEntity(){
		if(hasTileEntity()){
			return w.getBlockTileEntity((int)x, (int)y, (int)z);
		}
		return null;
	}
	
	public boolean isBlock(Block b){
		if(hasWorld()){
			if(b == null && w.getBlockId((int)x, (int)y, (int)z) == 0)
				return true;
			return Block.blocksList[w.getBlockId((int)x, (int)y, (int)z)] == b;
		}
		return false;
	}
	
	public int getBlockId(){
		if(hasWorld()){
			return w.getBlockId((int)x, (int)y, (int)z);
		}
		return -1;
	}
	
	public Block getBlock(){
		if(hasWorld()){
			if(w.getBlockId((int)x, (int)y, (int)z) == 0)
				return null;
			return Block.blocksList[w.getBlockId((int)x, (int)y, (int)z)];
		}
		return null;
	}
	
	public World getWorld() {
		return w;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}
	
	public int getBlockX() {
		return (int)x;
	}
	
	public int getBlockY() {
		return (int)y;
	}
	
	public int getBlockZ() {
		return (int)z;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Vector3){
			Vector3 vec = (Vector3) obj;
			return vec.w == w && vec.x == x && vec.y == y && vec.z == z; 
		}
		return false;
	}
	
	public Vec3 toVec3(){
		return Vec3.createVectorHelper(x, y, z);
	}
	
	@Override
	public String toString() {
		String s = "Vector3{";
		if(hasWorld())
			s += "w=" + w.provider.dimensionId + ";";
		s += "x=" + x + ";y=" + y + ";z=" + z + "}";
		return s;
	}

}
