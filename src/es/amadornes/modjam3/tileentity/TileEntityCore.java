package es.amadornes.modjam3.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import es.amadornes.modjam3.lib.Blocks;
import es.amadornes.modjam3.lib.Items;
import es.amadornes.modjam3.packet.PacketHandler;
import es.amadornes.modjam3.pathfind.DefaultPathFinder;
import es.amadornes.modjam3.pathfind.Path;
import es.amadornes.modjam3.pathfind.Vector3;

public class TileEntityCore extends TileEntity implements ISidedInventory, IFluidHandler {
	
	private FluidTank tank = new FluidTank(8000);
	private ItemStack item;
	
	private boolean isReceiver = true;//Can receive from other cores
	private boolean isRepeater = false;

	public int getType(){
		if(item != null)
			return 1;
		if(tank.getFluidAmount() > 0)
			return 2;
		return 0;
	}
	
	public int getTransferrableItems(){
		if(item != null){
			return Math.min(item.stackSize, getMaxTransferableItems());
		}
		return 0;
	}
	
	public int getMaxTransferableItems(){
		if(item != null){
			double proportion = Math.min(defaultItems + itemsPerOC*getUpgradeAmount(UpgradeType.OVERCLOCK), 64);
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
			return defaultFluid + fluidPerOC*getUpgradeAmount(UpgradeType.OVERCLOCK);
		}
		return 0;
	}
	
	public boolean canAccept(ItemStack is){
		return (isReceiver || isRepeater) && (item == null || (item.isItemEqual(is) && item.stackSize < item.getMaxStackSize())) && tank.getFluidAmount() == 0;
	}
	
	public boolean canAccept(FluidStack fs){
		return (isReceiver || isRepeater) && (tank.getFluidAmount() == 0 || (tank.getFluid().isFluidEqual(fs) && tank.getFluidAmount() < tank.getCapacity())) && item == null;
	}
	
	private int tick = 0;
	private int ticksLived = 0;
	private int needed = 0;
	
	private TileEntity getAttachedTileEntity(){
		return new Vector3(this).getRelative(ForgeDirection.getOrientation(blockMetadata).getOpposite()).getTileEntity();
	}
	
	private void suckFluid(){
		TileEntity attached = getAttachedTileEntity();
		if(attached != null){
			if(attached instanceof IFluidHandler){
				IFluidHandler te = (IFluidHandler) attached;
				if(tank.getFluidAmount() > 0){
					if(te.canDrain(ForgeDirection.getOrientation(blockMetadata).getOpposite(), tank.getFluid().getFluid())){
						FluidStack drained = te.drain(ForgeDirection.getOrientation(blockMetadata), new FluidStack(tank.getFluid().getFluid(), Math.min(40, tank.getCapacity() - tank.getFluidAmount())), true);
						tank.fill(drained, true);

						updateTile(this);
						updateTile(attached);
					}
				}else{
					for(FluidTankInfo info : te.getTankInfo(ForgeDirection.getOrientation(blockMetadata))){
						if(info.fluid != null){
							if(info.fluid.amount > 0){
								if(te.canDrain(ForgeDirection.getOrientation(blockMetadata).getOpposite(), info.fluid.getFluid())){
									FluidStack drained = te.drain(ForgeDirection.getOrientation(blockMetadata), new FluidStack(info.fluid.getFluid(), Math.min(40, tank.getCapacity() - tank.getFluidAmount())), true);
									tank.fill(drained, true);
		
									updateTile(this);
									updateTile(attached);
									return;
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void ejectFluid(){
		if(tank.getFluidAmount() > 0){
			TileEntity attached = getAttachedTileEntity();
			if(attached != null){
				if(attached instanceof IFluidHandler){
					IFluidHandler te = (IFluidHandler) attached;
					if(te.canFill(ForgeDirection.getOrientation(blockMetadata).getOpposite(), tank.getFluid().getFluid())){
						int filled = te.fill(ForgeDirection.getOrientation(blockMetadata).getOpposite(), new FluidStack(tank.getFluid().getFluid(), Math.min(tank.getFluidAmount(), 40)), true);
						tank.drain(filled, true);

						updateTile(this);
						updateTile(attached);
					}
				}
			}
		}
	}
	
	private void suckItem(){
		TileEntity attached = getAttachedTileEntity();
		//TODO
	}
	
	private void suckItemISided(){
		
	}
	
	private void ejectItem(){
		TileEntity attached = getAttachedTileEntity();
		//TODO
	}
	
	private void ejectItemISided(){
		
	}
	
	@Override
	public void updateEntity() {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
			if(ticksLived%5 == 0){//Every 5 ticks (4 times every second)
				if(isRepeater){
					
				}else{
					if(!isReceiver){
						if(hasUpgrade(UpgradeType.AUTO_SUCK)){
							if(getType() == 0){
								suckFluid();
								if(new Random().nextBoolean()){
									suckFluid();
								}else{
									suckItem();
								}
							}else if(getType() == 1){
								suckItem();
							}else if(getType() == 2){
								suckFluid();
							}
						}
					}else{
						if(hasUpgrade(UpgradeType.AUTO_EJECT)){
							if(getType() == 1){
								ejectItem();
							}else if(getType() == 2){
								ejectFluid();
							}
						}
					}
				}

				updateTile(this);
			}
			if(tick == needed){
				double minticks = 16;
				minticks -= getUpgradeAmount(UpgradeType.OVERCLOCK) * OCmultiplier;
				int mticks = (int) Math.max(1, Math.floor(minticks));
				needed = mticks + new Random().nextInt(mticks);
				tick = 0;
				
				randomTick();
			}
			tick++;
			ticksLived++;
		}
	}
	
	private void randomTick(){
		if(hasUpgrade(UpgradeType.HV)){
			//TODO ADD UPGRADE CODE
		}else{
			if(!isReceiver){
				if(hasAntenna()){
					Map<TileEntityCore, Path> cores = getNearbyReceivingCores(getAntennaRange());
					if(cores.size() > 0){
						for(int timesTried = 0; timesTried < 10; timesTried++){
							TileEntityCore core = new ArrayList<TileEntityCore>(cores.keySet()).get(new Random().nextInt(cores.keySet().size()));
							Path path = cores.get(core);
							switch(getType()){
							case 1://Items
								if(item != null){
									if(core.canAccept(item)){
										int freeSlots = item.getMaxStackSize();
										if(core.item != null){
											freeSlots = core.item.getMaxStackSize() - core.item.stackSize;
										}
										int items = Math.min(getTransferrableItems(), freeSlots);
										
										if(core.item == null){
											core.item = item.copy();
											core.item.stackSize = items;
										}else{
											core.item.stackSize += items;
										}
										
										if(item.stackSize - items == 0){
											item = null;
										}else{
											item.stackSize -= items;
										}
										updateTile(this);
										updateTile(core);
										PacketDispatcher.sendPacketToAllInDimension(PacketHandler.createLightningPacket(this, path), worldObj.provider.dimensionId);
										return;
									}
								}
							case 2://Fluids
								if(core.canAccept(tank.getFluid())){
									int fluid = Math.min(getTransferrableMilibuckets(), core.tank.getCapacity() - core.tank.getFluidAmount());
									core.tank.fill(new FluidStack(tank.getFluid().getFluid(), fluid), true);
									tank.drain(fluid, true);
									updateTile(this);
									updateTile(core);
									PacketDispatcher.sendPacketToAllInDimension(PacketHandler.createLightningPacket(this, path), worldObj.provider.dimensionId);
									return;
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void updateTile(TileEntity te){
		PacketDispatcher.sendPacketToAllInDimension(te.getDescriptionPacket(), worldObj.provider.dimensionId);
	}
	
	private List<EntityLiving> getNearbyEntities(int radius){
		List<EntityLiving> entities = new ArrayList<EntityLiving>();
		Chunk c1 = worldObj.getChunkFromBlockCoords(xCoord - radius, zCoord - radius);
		Chunk c2 = worldObj.getChunkFromBlockCoords(xCoord - radius, zCoord + radius);
		Chunk c3 = worldObj.getChunkFromBlockCoords(xCoord + radius, zCoord + radius);
		Chunk c4 = worldObj.getChunkFromBlockCoords(xCoord + radius, zCoord - radius);
		List<Chunk> chunks = new ArrayList<Chunk>();
		chunks.add(c1);
		if(!chunks.contains(c2))
			chunks.add(c2);
		if(!chunks.contains(c3))
			chunks.add(c3);
		if(!chunks.contains(c4))
			chunks.add(c4);
		for(Chunk c : chunks){
			//FIXME
		}
		return entities;
	}
	
	private Map<TileEntityCore, Path> getNearbyReceivingCores(int radius){
		Map<TileEntityCore, Path> cores = getNearbyCores(radius);
		Map<TileEntityCore, Path> receivers = new HashMap<TileEntityCore, Path>();
		
		for(TileEntityCore core : cores.keySet()){
			if(!core.isRepeater && core.isReceiver){
				receivers.put(core, cores.get(core));
			}
		}
		
		return receivers;
	}
	
	private Map<TileEntityCore, Path> getNearbyRepeaters(int radius){
		Map<TileEntityCore, Path> cores = getNearbyCores(radius);
		Map<TileEntityCore, Path> repeaters = new HashMap<TileEntityCore, Path>();
		
		for(TileEntityCore core : cores.keySet()){
			if(core.isRepeater){
				repeaters.put(core, cores.get(core));
			}
		}
		
		return repeaters;
	}
	
	private Map<TileEntityCore, Path> getNearbyCores(int radius){
		Vec3 thisTE = new Vector3(this).toVec3();
		List<TileEntityCore> nearby = new ArrayList<TileEntityCore>();
		for(int x = (xCoord - radius); x < (xCoord + radius); x++){
			for(int y = (yCoord - radius); y < (yCoord + radius); y++){
				for(int z = (zCoord - radius); z < (zCoord + radius); z++){
					TileEntity te = worldObj.getBlockTileEntity(x, y, z);
					if(te != null){
						if(te instanceof TileEntityCore){
							if(((TileEntityCore)te).hasAntenna()){
								Vec3 tile = new Vector3(x, y, z).toVec3();
								if(thisTE.distanceTo(tile) < radius){
									nearby.add((TileEntityCore) worldObj.getBlockTileEntity(x, y, z));
								}
							}
						}
					}
				}
			}
		}
		if(nearby.contains(this))
			nearby.remove(this);

		Map<TileEntityCore, Path> cores = new HashMap<TileEntityCore, Path>();
		TileEntity from = hasExternalAntenna() ? getExternalAntenna() : this;
		for(TileEntityCore c : nearby){
			TileEntity to = c.hasExternalAntenna() ? c.getExternalAntenna() : c;
			Path path = new DefaultPathFinder(new Vector3(from), new Vector3(to), radius).pathfind().getShortestPath();
			if(path != null){
				cores.put(c, path);
			}
		}
		
		return cores;
	}
	
	private List<IInventory> getNearbyInventories(int radius){
		Vec3 thisTE = new Vector3(this).toVec3();
		List<IInventory> inventories = new ArrayList<IInventory>();
		for(int x = xCoord - radius; x < xCoord + radius; x++){
			for(int y = yCoord - radius; y < yCoord + radius; y++){
				for(int z = zCoord - radius; z < xCoord + radius; z++){
					TileEntity te = worldObj.getBlockTileEntity(x, y, z);
					if(te != null){
						if(te instanceof IInventory){
							if(!(te instanceof TileEntityCore)){
								Vec3 tile = new Vector3(x, y, z).toVec3();
								if(thisTE.distanceTo(tile) < radius){
									inventories.add((IInventory) worldObj.getBlockTileEntity(x, y, z));
								}
							}
						}
					}
				}
			}
		}
		return inventories;
	}
	
	private List<IFluidHandler> getNearbyTanks(int radius){
		Vec3 thisTE = new Vector3(this).toVec3();
		List<IFluidHandler> inventories = new ArrayList<IFluidHandler>();
		for(int x = xCoord - radius; x < xCoord + radius; x++){
			for(int y = yCoord - radius; y < yCoord + radius; y++){
				for(int z = zCoord - radius; z < xCoord + radius; z++){
					TileEntity te = worldObj.getBlockTileEntity(x, y, z);
					if(te != null){
						if(te instanceof IFluidHandler){
							if(!(te instanceof TileEntityCore)){
								Vec3 tile = new Vector3(x, y, z).toVec3();
								if(thisTE.distanceTo(tile) < radius){
									inventories.add((IFluidHandler) worldObj.getBlockTileEntity(x, y, z));
								}
							}
						}
					}
				}
			}
		}
		return inventories;
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

		if(tank.getFluidAmount() > 0){
			NBTTagCompound tank = new NBTTagCompound();
			this.tank.writeToNBT(tank);
			tag.setCompoundTag("tank", tank);
		}
		
		if(this.item != null){
			NBTTagCompound item = new NBTTagCompound();
			this.item.writeToNBT(item);
			tag.setCompoundTag("item", item);
		}

		tag.setBoolean("isInput", isReceiver);
		tag.setBoolean("isRepeater", isRepeater);
		tag.setInteger("metadata", blockMetadata);
		writeUpgradesToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		
		if(tag.hasKey("tank")){
			NBTTagCompound tank = tag.getCompoundTag("tank");
			this.tank.readFromNBT(tank);
		}else{
			this.tank.setFluid(null);
		}
		
		if(tag.hasKey("item")){
			NBTTagCompound item = tag.getCompoundTag("item");
			this.item = ItemStack.loadItemStackFromNBT(item);
		}else{
			this.item = null;
		}

		isReceiver = tag.getBoolean("isInput");
		isRepeater = tag.getBoolean("isRepeater");
		blockMetadata = tag.getInteger("metadata");
		readUpgradesFromNBT(tag);
	}
	
	
	
	
	
	
	

	public int getSizeInventory() {
		return 1;
	}

	public ItemStack getStackInSlot(int i) {
		onInventoryChanged();
		if(i == 0)
			return item;
		return null;
	}

	public ItemStack decrStackSize(int i, int amt) {
		if(isReceiver && !isRepeater){
			if(i == 0){
				if(item.stackSize - amt > 0){
					item.stackSize -= amt;
				}else{
					item = null;
				}
			}
		}
		onInventoryChanged();
		return null;
	}

	public ItemStack getStackInSlotOnClosing(int i) {
		return getStackInSlot(i);
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if(!isReceiver && !isRepeater){
			if(i == 0){
				item = itemstack;
			}
		}
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
		return canInsertItem(i, itemstack, ((blockMetadata/2 == Math.floor(blockMetadata)/2) ? (blockMetadata + 1)%6 : (blockMetadata - 1)%6));
	}

	public int[] getAccessibleSlotsFromSide(int side) {
		ForgeDirection oppositeDirection = ForgeDirection.getOrientation(side).getOpposite();
		if(oppositeDirection == ForgeDirection.getOrientation(blockMetadata)){
			return new int[]{0};
		}
		return new int[]{};
	}

	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		if(!isReceiver && !isRepeater){
			ForgeDirection oppositeDirection = ForgeDirection.getOrientation(j).getOpposite();
			if(oppositeDirection == ForgeDirection.getOrientation(blockMetadata)){
				if(tank.getFluidAmount() == 0){
					if(item == null){
						return true;
					}else{
						if(item.isItemEqual(itemstack)){
							if(item.stackSize + itemstack.stackSize <= item.getMaxStackSize()){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		if(isReceiver && !isRepeater){
			ForgeDirection oppositeDirection = ForgeDirection.getOrientation(j).getOpposite();
			return item != null && oppositeDirection == ForgeDirection.getOrientation(blockMetadata);
		}
		return false;
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(canFill(from, resource.getFluid())){
			if(tank.getFluid() == null || resource.getFluid().equals(tank.getFluid().getFluid())){
				int filled = Math.min(tank.getCapacity() - tank.getFluidAmount(), resource.amount);
				if(doFill){
					tank.fill(new FluidStack(resource.getFluid(), filled), true);
				}
				return filled;
			}
		}
		return 0;
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if(isReceiver && !isRepeater){
			if(from == ForgeDirection.getOrientation(blockMetadata).getOpposite()){
				if(tank.getFluidAmount() > 0){
					if(tank.getFluid().getFluid().equals(resource.getFluid())){
						FluidStack fs = new FluidStack(tank.getFluid().getFluid(), Math.min(50, Math.min(resource.amount, tank.getFluidAmount())));
						if(doDrain){
							tank.drain(resource.amount, true);
						}
						return fs;
					}
				}
			}
		}
		return null;
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if(isReceiver && !isRepeater){
			if(from == ForgeDirection.getOrientation(blockMetadata).getOpposite()){
				if(tank.getFluidAmount() > 0){
					FluidStack fs = new FluidStack(tank.getFluid().getFluid(), Math.min(50, Math.min(maxDrain, tank.getFluidAmount())));
					if(doDrain){
						tank.drain(maxDrain, true);
					}
					return fs;
				}
			}
		}
		return null;
	}

	public boolean canFill(ForgeDirection from, Fluid fluid) {
		if(!isReceiver && !isRepeater){
			if(from == ForgeDirection.getOrientation(blockMetadata).getOpposite()){
				if(tank.getFluid() == null || tank.getFluidAmount() == 0 || (tank.getFluid().getFluid().equals(fluid) && tank.getFluidAmount() < tank.getCapacity())){
					return true;
				}
			}
		}
		return false;
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		if(isReceiver && !isRepeater){
			if(from == ForgeDirection.getOrientation(blockMetadata).getOpposite()){
				if(tank.getFluidAmount() > 0){
					if(tank.getFluid().getFluid().equals(fluid)){
						return true;
					}
				}
			}
		}
		return false;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[]{ tank.getInfo() };
	}
	
	public boolean hasAntenna(){
		return hasUpgrade(UpgradeType.INTERNAL_ANTENNA) || new Vector3(this).getRelative(ForgeDirection.getOrientation(blockMetadata)).isBlock(Blocks.antenna);
	}
	
	public boolean hasExternalAntenna(){
		return new Vector3(this).getRelative(ForgeDirection.getOrientation(blockMetadata)).isBlock(Blocks.antenna);
	}
	
	public TileEntityAntenna getExternalAntenna(){
		return (TileEntityAntenna) new Vector3(this).getRelative(ForgeDirection.getOrientation(blockMetadata)).getTileEntity();
	}
	
	public int getAntennaRange(){
		return getUpgradeAmount(UpgradeType.INTERNAL_ANTENNA)*4 + (new Vector3(this).getRelative(ForgeDirection.getOrientation(blockMetadata)).isBlock(Blocks.antenna) ? 8 : 0);
	}
	
	public boolean isReceiver(){
		return isReceiver;
	}
	
	public boolean isRepeater(){
		return isRepeater;
	}
	
	public void setType(int type){
		if(type == 0){
			isRepeater = false;
			isReceiver = true;
		}else if(type == 1){
			isRepeater = false;
			isReceiver = false;
		}else if(type == 2){
			isRepeater = true;
			isReceiver = false;
		}
	}
	
	/*
	 * 
	 *   ___  ___                                 .___                ____              __          _____  _____
	 *  |   ||   \______   ________________     __| _/____   ______  /  _ \     _______/  |_ __ ___/ ____\/ ____\
	 *  |   ||   /\____ \ / ___\_  __ \__  \   / __ |/ __ \ /  ___/  >  _ </\  /  ___/\   __\  |  \   __\\   __\ 
	 *  |   ||  / |  |_> > /_/  >  | \// __ \_/ /_/ \  ___/ \___ \  /  <_\ \/  \___ \  |  | |  |  /|  |   |  |   
	 *  |______/  |   __/\___  /|__|  (____  /\____ |\___  >____  > \_____\ \ /____  > |__| |____/ |__|   |__|
	 *            |__|  /_____/            \/      \/    \/     \/         \/      \/                            
	 * 
	 */
	

	private UpgradeType[] upgrades = new UpgradeType[4];
	
	public ForgeDirection[] determineUpgradableFaces(){
		switch(blockMetadata){
		case 0:
			return new ForgeDirection[]{ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH};
		case 1:
			return new ForgeDirection[]{ForgeDirection.EAST, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.SOUTH};
		case 2:
			return new ForgeDirection[]{ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.EAST, ForgeDirection.WEST};
		case 3:
			return new ForgeDirection[]{ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.EAST, ForgeDirection.WEST};
		case 4:
			return new ForgeDirection[]{ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.SOUTH};
		case 5:
			return new ForgeDirection[]{ForgeDirection.UP, ForgeDirection.DOWN, ForgeDirection.NORTH, ForgeDirection.SOUTH};
		}
		
		return null;
	}
	
	private boolean isFaceUpgradable(ForgeDirection face){
		int f = 0;
		ForgeDirection[] upgradable = determineUpgradableFaces();
		if(upgradable != null){
			for(ForgeDirection dir : upgradable){
				if(dir == face)
					if(upgrades[f] == null)
						return true;
				f++;
			}
		}
		return false;
	}
	
	private int OCmultiplier = 4;//Tick multiplier
	
	private int defaultItems = 1;
	private int defaultFluid = 10;
	private int itemsPerOC = 8;
	private int fluidPerOC = 10;
	
	private void writeUpgradesToNBT(NBTTagCompound t){
		NBTTagCompound tag = new NBTTagCompound();
		
		for(int i = 0; i < upgrades.length; i++){
			if(upgrades[i] == null){
				tag.setInteger("upgrade" + i, -1);
			}else{
				tag.setInteger("upgrade" + i, upgrades[i].ordinal());
			}
		}
		
		t.setTag("upgrades", tag);
	}
	
	private void readUpgradesFromNBT(NBTTagCompound t){
		NBTTagCompound tag = t.getCompoundTag("upgrades");
		
		for(int i = 0; i < upgrades.length; i++){
			UpgradeType type = null;
			if(tag.getInteger("upgrade" + i) >= 0){
				type = UpgradeType.getFromDamage(tag.getInteger("upgrade" + i));
			}
			upgrades[i] = type;
		}
	}
	
	public boolean installUpgrade(UpgradeType type, ForgeDirection face){
		if(type != null){
			if(isFaceUpgradable(face)){
				if(isRepeater){
					if(type.repeater){
						if(type.maxRepeater > getUpgradeAmount(type)){
							return true;
						}
					}
				}else{
					if(!isReceiver){
						if(type.sender){
							if(type.maxSender > getUpgradeAmount(type)){
								return true;
							}
						}
					}else{
						if(type.receiver){
							if(type.maxReceiver > getUpgradeAmount(type)){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public boolean hasUpgrade(UpgradeType type){
		for(int i = 0; i < upgrades.length; i++){
			if(upgrades[i] == type)
				return true;
		}
		return false;
	}
	
	public int getUpgradeAmount(UpgradeType type){
		int amount = 0;
		for(int i = 0; i < upgrades.length; i++){
			if(upgrades[i] == type)
				amount++;
		}
		return amount;
	}
	
	public void setUpgrade(UpgradeType type, ForgeDirection face){
		int id = 0;
		ForgeDirection[] faces = determineUpgradableFaces();
		if(faces != null){
			for(ForgeDirection dir : faces){
				if(dir == face)
					break;
				id++;
			}
			if(id < 4){
				if(upgrades[id] == null){
					upgrades[id] = type;
					updateTile(this);
				}
			}
		}
	}
	
	public void removeUpgrade(int id){
		upgrades[id] = null;
		updateTile(this);
	}
	
	public ItemStack getUpgradeItemStack(int upgrade){
		if(upgrades[upgrade] == null)
			return null;
		return new ItemStack(Items.upgrade, 1, upgrades[upgrade].ordinal());
	}
	
	@SuppressWarnings("incomplete-switch")
	public ItemStack getUpgradeOnSide(ForgeDirection side){
		if(blockMetadata == 0){
			switch(side){
			case WEST:
				return getUpgradeItemStack(1);
			case EAST:
				return getUpgradeItemStack(0);
			case SOUTH:
				return getUpgradeItemStack(3);
			case NORTH:
				return getUpgradeItemStack(2);
			}
		}else if(blockMetadata == 1){
			switch(side){
			case EAST:
				return getUpgradeItemStack(0);
			case WEST:
				return getUpgradeItemStack(1);
			case NORTH:
				return getUpgradeItemStack(2);
			case SOUTH:
				return getUpgradeItemStack(3);
			}
		}else if(blockMetadata == 2){
			switch(side){
			case UP:
				return getUpgradeItemStack(0);
			case DOWN:
				return getUpgradeItemStack(1);
			case EAST:
				return getUpgradeItemStack(2);
			case WEST:
				return getUpgradeItemStack(3);
			}
		}else if(blockMetadata == 3){
			switch(side){
			case UP:
				return getUpgradeItemStack(0);
			case DOWN:
				return getUpgradeItemStack(1);
			case EAST:
				return getUpgradeItemStack(2);
			case WEST:
				return getUpgradeItemStack(3);
			}
		}else if(blockMetadata == 4){
			switch(side){
			case UP:
				return getUpgradeItemStack(0);
			case DOWN:
				return getUpgradeItemStack(1);
			case NORTH:
				return getUpgradeItemStack(2);
			case SOUTH:
				return getUpgradeItemStack(3);
			}
		}else if(blockMetadata == 5){
			switch(side){
			case UP:
				return getUpgradeItemStack(0);
			case DOWN:
				return getUpgradeItemStack(1);
			case NORTH:
				return getUpgradeItemStack(2);
			case SOUTH:
				return getUpgradeItemStack(3);
			}
		}
		return null;
	}
	
	@SuppressWarnings("incomplete-switch")
	public void removeUpgradeOnSide(ForgeDirection side){
		if(blockMetadata == 0){
			switch(side){
			case WEST:
				removeUpgrade(1);
				return;
			case EAST:
				removeUpgrade(0);
				return;
			case SOUTH:
				removeUpgrade(3);
				return;
			case NORTH:
				removeUpgrade(2);
				return;
			}
		}else if(blockMetadata == 1){
			switch(side){
			case EAST:
				removeUpgrade(0);
				return;
			case WEST:
				removeUpgrade(1);
				return;
			case NORTH:
				removeUpgrade(2);
				return;
			case SOUTH:
				removeUpgrade(3);
				return;
			}
		}else if(blockMetadata == 2){
			switch(side){
			case UP:
				removeUpgrade(0);
				return;
			case DOWN:
				removeUpgrade(1);
				return;
			case EAST:
				removeUpgrade(2);
				return;
			case WEST:
				removeUpgrade(3);
				return;
			}
		}else if(blockMetadata == 3){
			switch(side){
			case UP:
				removeUpgrade(0);
				return;
			case DOWN:
				removeUpgrade(1);
				return;
			case EAST:
				removeUpgrade(2);
				return;
			case WEST:
				removeUpgrade(3);
				return;
			}
		}else if(blockMetadata == 4){
			switch(side){
			case UP:
				removeUpgrade(0);
				return;
			case DOWN:
				removeUpgrade(1);
				return;
			case NORTH:
				removeUpgrade(2);
				return;
			case SOUTH:
				removeUpgrade(3);
				return;
			}
		}else if(blockMetadata == 5){
			switch(side){
			case UP:
				removeUpgrade(0);
				return;
			case DOWN:
				removeUpgrade(1);
				return;
			case NORTH:
				removeUpgrade(2);
				return;
			case SOUTH:
				removeUpgrade(3);
				return;
			}
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	public static enum UpgradeType{
		EMPTY("empty", 0, false, false, false, 0, 0, 0),
		OVERCLOCK("overclock", 1, false, true, true, 0, 4, 4),
		AUTO_EJECT("autoeject", 2, true, false, false, 1, 0, 0),
		AUTO_SUCK("autosuck", 3, false, true, false, 0, 1, 0),
		INTERNAL_ANTENNA("empty", 4, true, true, true, 1, 1, 2),//TODO
		HV("empty", 5, false, true, false, 0, 1, 0);//TODO
		
		private String icon;
		private int id;
		private boolean receiver, sender, repeater;
		private int maxReceiver, maxSender, maxRepeater;
		
		private UpgradeType(String icon, int id, boolean receiver, boolean sender, boolean repeater, int maxReceiver, int maxSender, int maxRepeater) {
			this.icon = icon;
			this.id = id;
			this.receiver = receiver;
			this.sender = sender;
			this.repeater = repeater;
			this.maxReceiver = maxReceiver;
			this.maxSender = maxSender;
			this.maxRepeater = maxRepeater;
		}
		
		public String getIconName(){
			return icon;
		}
		
		public int getId(){
			return id;
		}
		
		public boolean canGoOnReceiver(){
			return receiver;
		}
		
		public boolean canGoOnSender(){
			return sender;
		}
		
		public boolean canGoOnRepeater(){
			return repeater;
		}
		
		public int maxAmoutPerReceiver(){
			return maxReceiver;
		}
		
		public int maxAmoutPerSender(){
			return maxSender;
		}
		
		public int maxAmoutPerRepeater(){
			return maxRepeater;
		}
		
		public static UpgradeType getFromDamage(int damage){
			for(UpgradeType u : values()){
				if(u.id == damage)
					return u;
			}
			return UpgradeType.EMPTY;
		}
		
		public static int getHighestUpgradeID(){
			int highest = 0;
			for(UpgradeType u : values()){
				if(u.id > highest)
					highest = u.id;
			}
			return highest;
		}
	}
	
	private boolean renderLightning = false;
	
	public boolean shouldRenderLightning(){
		return renderLightning;
	}
	
	public void setShouldRenderLightning(boolean should){
		renderLightning = should;
	}

	private Path lightning_path = null;
	
	public Path getLightningPath() {
		return lightning_path;
	}
	
	public void setLightningPath(Path p){
		lightning_path = p;
	}
	
}
