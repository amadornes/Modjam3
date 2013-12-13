package es.amadornes.modjam3.tileentity;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileEntityCore extends TileEntity implements ISidedInventory, IFluidHandler {
	
	private FluidTank tank = new FluidTank(1000);
	private ItemStack item;
	
	private int OCmultiplier = 4;
	private int OCmodules = 0;
	
	private int defaultItems = 1;
	private int defaultFluid = 10;
	private int itemsPerOC = 8;
	private int fluidPerOC = 10;
	
	public boolean canAcceptStuff(){
		if(tank.getFluidAmount() > 0 || item != null)
			return false;
		return true;
	}
	
	public boolean canAddOverclocker(){
		if(OCmodules < 4)
			return true;
		return false;
	}

	public int getType(){
		if(tank.getFluidAmount() > 0)
			return 2;
		if(item != null)
			return 1;
		return 0;
	}
	
	private int tick = 0;
	private int needed = 0;
	
	@Override
	public void updateEntity() {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
			if(tick%4 == 0){
				PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
			}
			if(tick == needed){
				double minticks = 15;
				minticks -= OCmodules * OCmultiplier;
				int mticks = (int) Math.max(0, Math.floor(minticks));
				needed = mticks + new Random().nextInt(mticks);
				
				
			}
			tick++;
		}
	}
	
	@Override
	public Packet getDescriptionPacket() {
		Packet132TileEntityData packet = new Packet132TileEntityData();
		packet.data = new NBTTagCompound();
		writeToNBT(packet.data);
		return packet;
	}
	
	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNBT(pkt.data);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);

		tag.setInteger("OCmodules", OCmodules);

		NBTTagCompound tank = new NBTTagCompound();
		this.tank.writeToNBT(tank);
		tag.setCompoundTag("tank", tank);
		
		NBTTagCompound item = new NBTTagCompound();
		if(this.item != null){
			this.item.writeToNBT(item);
			tag.setCompoundTag("item", item);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		OCmodules = tag.getInteger("OCmodules");
		
		NBTTagCompound tank = tag.getCompoundTag("tank");
		this.tank.readFromNBT(tank);
		
		if(tag.hasKey("item")){
			NBTTagCompound item = tag.getCompoundTag("item");
			this.item = ItemStack.loadItemStackFromNBT(item);
		}
	}
	
	
	
	
	
	
	

	public int getSizeInventory() {
		return 1;
	}

	public ItemStack getStackInSlot(int i) {
		if(i == 0)
			return item;
		return null;
	}

	public ItemStack decrStackSize(int i, int amt) {
		if(i == 0){
			if(item.stackSize - amt > 0){
				item.stackSize -= amt;
			}
		}
		item = null;
		onInventoryChanged();
		return item;
	}

	public ItemStack getStackInSlotOnClosing(int i) {
		return getStackInSlot(i);
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		item = itemstack;
		onInventoryChanged();
	}

	public String getInvName() {
		return blockType.getUnlocalizedName() + ".inv";
	}

	public boolean isInvNameLocalized() {
		return false;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	public void openChest() {
		onInventoryChanged();
	}
	public void closeChest() {
		onInventoryChanged();
	}

	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return canAcceptStuff();
	}

	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{side};
	}

	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		int oppositemeta = ForgeDirection.getOrientation(j).getOpposite().ordinal();
		return (canAcceptStuff() || itemstack.isItemEqual(item)) && oppositemeta == blockMetadata;
	}

	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return item != null;
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(from == ForgeDirection.getOrientation(blockMetadata).getOpposite()){
			if(tank.getFluid() == null || tank.getFluidAmount() > 0 || resource.getFluid().equals(tank.getFluid().getFluid())){
				
			}
		}
		return 0;
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return null;
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(from == ForgeDirection.getOrientation(blockMetadata).getOpposite()){
			if(tank.getFluid() == null || tank.getFluidAmount() == 0 || tank.getFluid().getFluid().equals(fluid)){
				return true;
			}
		}
		return false;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if(from == ForgeDirection.getOrientation(blockMetadata).getOpposite()){
			if(tank.getFluidAmount() > 0){
				if(tank.getFluid().getFluid().equals(fluid)){
					return true;
				}
			}
		}
		return false;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{ tank.getInfo() };
	}
	
}
