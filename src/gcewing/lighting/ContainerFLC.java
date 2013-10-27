//------------------------------------------------------------------------------
//
//   Greg's Lighting - ContainerFloodlightCarbide
//
//------------------------------------------------------------------------------

package gcewing.lighting;

import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;

public class ContainerFLC extends BaseContainer {

	public static Container create(EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof TEFloodlightCarbide)
			return new ContainerFLC(player, (TEFloodlightCarbide)te);
		else
			return null;
	}

	TEFloodlightCarbide te;
	
	public ContainerFLC(EntityPlayer player, TEFloodlightCarbide te) {
		super(176, 166);
		this.te = te;
		addSlotToContainer(new Slot(te, 0, 44, 8));
		addSlotToContainer(new Slot(te, 1, 116, 60));
		addPlayerSlots(player, 8, 84);
//		for (int i = 0; i < 3; ++i)
//			for (int j = 0; j < 9; ++j)
//				this.addSlotToContainer(new Slot(pi, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
//		for (int i = 0; i < 9; ++i)
//			this.addSlotToContainer(new Slot(pi, i, 8 + i * 18, 142));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.te.isUseableByPlayer(player);
	}
	
	@Override
	void sendStateTo(ICrafting crafter) {
		crafter.sendProgressBarUpdate(this, 0, te.waterLevel);
		crafter.sendProgressBarUpdate(this, 1, te.carbideLevel);
	}

	@Override
	public void updateProgressBar(int i, int value) {
		//System.out.printf("ContainerFLC.updateProgressBar: %d %d\n", i, value);
		switch (i) {
			case 0: te.waterLevel = value;
			case 1: te.carbideLevel = value;
		}
	}

}
