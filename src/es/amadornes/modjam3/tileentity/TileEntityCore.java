package es.amadornes.modjam3.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
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
	
	private int OCmultiplier = 0;
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
		return item;
	}

	public ItemStack getStackInSlotOnClosing(int i) {
		return getStackInSlot(i);
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if(i == 0)
			item = itemstack;
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

	public void openChest() {}
	public void closeChest() {}

	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return canAcceptStuff();
	}

	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[]{side};
	}

	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return canAcceptStuff() || itemstack.isItemEqual(item);
	}

	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return item != null;
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return 0;
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return null;
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return null;
	}
	
}
