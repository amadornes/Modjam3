package es.amadornes.modjam3.tileentity;

import java.util.Random;

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
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

public class TileEntityCore extends TileEntity implements ISidedInventory, IFluidHandler {
	
	private FluidTank tank = new FluidTank(1000);
	private ItemStack item;
	
	private int OCmultiplier = 4;//Tick multiplier
	private int OCmodules = 0;//Amount of OC modules installed
	
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
		if(item != null)
			return 1;
		if(tank.getFluidAmount() > 0)
			return 2;
		return 0;
	}
	
	public int getTransferrableItems(){
		if(item != null){
			return Math.min(item.stackSize, getMaxTransferrableItems());
		}
		return 0;
	}
	
	public int getMaxTransferrableItems(){
		if(item != null){
			double proportion = Math.min(defaultItems + itemsPerOC*OCmodules, 64);
			proportion /= 64;
			return (int) Math.floor(Math.min(proportion * item.getMaxStackSize(), item.getMaxStackSize()));
		}
		return 0;
	}
	
	public int getTransferrableMilibuckets(){
		if(tank.getFluidAmount() > 0){
			return Math.min(tank.getFluidAmount(), getMaxTransferrableMilibuckets());
		}
		return 0;
	}
	
	public int getMaxTransferrableMilibuckets(){
		if(tank.getFluidAmount() > 0){
			return defaultFluid + fluidPerOC*OCmodules;
		}
		return 0;
	}
	
	private int tick = 0;
	private int needed = 0;
	
	@Override
	public void updateEntity() {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
			if(tick%5 == 0){
				PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);
			}
			if(tick == needed){
				double minticks = 15;
				minticks -= OCmodules * OCmultiplier;
				int mticks = (int) Math.max(0, Math.floor(minticks));
				needed = mticks + new Random().nextInt(mticks);
				tick = 0;
				
				send();
			}
			tick++;
		}
	}
	
	private void send(){
		switch(getType()){
		case 1://Items
			
			
			return;
		case 2://Fluids
			
			
			return;
		}
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound data = new NBTTagCompound();
		writeToNBT(data);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, data);
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
		ForgeDirection oppositeDirection = ForgeDirection.getOrientation(j).getOpposite();
		boolean can = (item == null || (itemstack.isItemEqual(item) && itemstack.stackSize < itemstack.getMaxStackSize())) && oppositeDirection == ForgeDirection.getOrientation(blockMetadata);
		System.out.println(can);
		return can;
	}

	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		ForgeDirection oppositeDirection = ForgeDirection.getOrientation(j).getOpposite();
		return item != null && oppositeDirection == ForgeDirection.getOrientation(blockMetadata);
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(canFill(from, resource.getFluid())){
			if(tank.getFluid() == null || resource.getFluid().equals(tank.getFluid().getFluid())){
				int filled = Math.min(tank.getCapacity() - tank.getFluidAmount(), resource.amount);
				if(doFill){
					FluidStack fs = new FluidStack(resource.getFluid(), tank.getFluidAmount() + filled);
					tank.setFluid(fs);
				}
				return filled;
			}
		}
		return 0;
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		//FIXME
		return null;
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		//FIXME
		return null;
	}

	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(from == ForgeDirection.getOrientation(blockMetadata).getOpposite()){
			if(tank.getFluid() == null || tank.getFluidAmount() == 0 || (tank.getFluid().getFluid().equals(fluid) && tank.getFluidAmount() < tank.getCapacity())){
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
