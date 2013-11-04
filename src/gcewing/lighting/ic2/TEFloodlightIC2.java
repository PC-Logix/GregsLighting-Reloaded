package gcewing.lighting.ic2;

import net.minecraft.block.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import ic2.api.*;
import ic2.api.energy.*;
import ic2.api.energy.tile.*;
import ic2.api.energy.event.*;
import ic2.api.tile.IWrenchable;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import gcewing.lighting.*;

public class TEFloodlightIC2 extends TileEntity implements IEnergySink, IWrenchable {

	final int maxEnergy = 64;
	final int maxInput = 32;
	final int energyUsedPerTick = 1;
	
	int energy = 0;
	boolean addedToEnergyNet = false;
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		energy = nbt.getInteger("energy");
		//addedToEnergyNet = nbt.getBoolean("addedToEnergyNet");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("energy", energy);
		//nbt.setBoolean("addedToEnergyNet", addedToEnergyNet);
	}
	
    @Override
    public void onChunkUnload() {
    	invalidate();
    }
	

	@Override
	public int getMaxSafeInput() {
		return maxInput;
	}
	/*
	@Override
	public int demandsEnergy() {
		//System.out.printf("TEFloodlightIC2.demandsEnergy\n");
    if (energy < maxEnergy / 2)
    	return maxEnergy - energy;
    else
    	return 0;
  }

	@Override
	public int injectEnergy(Direction directionFrom, int amount) {
		//System.out.printf("TEFloodlightIC2.injectEnergy: %d\n", amount);
		if (amount > maxInput) {
			System.out.printf("TEFloodlightIC2.injectEnergy: Exploding\n");
			GregsLighting.explodeMachineAt(worldObj, xCoord, yCoord, zCoord);
			return 0;
		}
		boolean hadEnergy = energy > 0;
    int surplus = 0;
    energy += amount;
    if (energy > maxEnergy) {
      surplus = energy - maxEnergy;
      energy = maxEnergy;
    }
    if (hadEnergy != (energy > 0)) {
    	onInventoryChanged();
    	updateBlock();
    }
    return surplus;
	}
*/

	public void updateEntity() {

		if (!worldObj.isRemote) {
			//System.out.printf("TEFloodlightIC2.updateEntity: %s addedToEnergyNet = %s\n", this, addedToEnergyNet);
			if (!this.addedToEnergyNet) {
				System.out.printf("TEFloodlightIC2.updateEntity: adding to energy net\n");
				//EnergyNet.getForWorld(worldObj).addTileEntity(this);
				MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
				this.addedToEnergyNet = true;
				onInventoryChanged();
			}
			if (isActive()) {
				//System.out.printf("TEFloodlightIC2.updateEntity: using %d energy from %d\n", energyUsedPerTick, energy);
				energy -= energyUsedPerTick;
				if (energy < 0)
					energy = 0;
				onInventoryChanged();
				if (!isActive())
					updateBlock();
			}
		}
		super.updateEntity();
	}
	
	public void invalidate() {
		if (addedToEnergyNet) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
			addedToEnergyNet = false;
			onInventoryChanged();
		}
		super.invalidate();
	}
	
	BlockFloodlightIC2 getBlock() {
		return (BlockFloodlightIC2)getBlockType();
	}

	void updateBlock() {
		BlockFloodlightIC2 block = getBlock();
		block.update(worldObj, xCoord, yCoord, zCoord);
		block.updateBeam(worldObj, xCoord, yCoord, zCoord);
	}
	
	public boolean isActive() {
		boolean on = switchedOn();
		boolean redstone =  receivingRedstoneSignal();
		//System.out.printf("TEFloodlightIC2.isActive: %s at (%d, %d, %d) energy = %d, on = %s, redstone = %s\n",
		//	this, xCoord, yCoord, zCoord, energy, on, redstone);
		return energy > 0 && (on || redstone);
	}
	
	public boolean switchedOn() {
		BlockFloodlightIC2 block = getBlock();
		boolean result = block.switchedOn;
		//System.out.printf("TEFloodlightIC2.switchedOn: id = %s switchedOn = %s\n",
		//	block.blockID, result);
		return result;
	}
	
	boolean receivingRedstoneSignal() {
		return worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public double demandedEnergyUnits() {
		// TODO Auto-generated method stub
		return Math.max(0, this.maxEnergy - this.energy);
	}

	@Override
	public double injectEnergyUnits(ForgeDirection directionFrom, double amount) {
		// TODO Auto-generated method stub
		//return 0;
		//System.out.printf("TEFloodlightIC2.injectEnergy: %d\n", amount);
		if (amount > maxInput) {
			System.out.printf("TEFloodlightIC2.injectEnergy: Exploding\n");
			GregsLighting.explodeMachineAt(worldObj, xCoord, yCoord, zCoord);
			return 0;
		}
		boolean hadEnergy = energy > 0;
    int surplus = 0;
    energy += amount;
    if (energy > maxEnergy) {
      surplus = energy - maxEnergy;
      energy = maxEnergy;
    }
    if (hadEnergy != (energy > 0)) {
    	onInventoryChanged();
    	updateBlock();
    }
    return surplus;
	}

	public boolean isAddedToEnergyNet() {
		//System.out.printf("TEFloodlightIC2.isAddedToEnergyNet\n");
    return addedToEnergyNet;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public short getFacing() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setFacing(short facing) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getWrenchDropRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
