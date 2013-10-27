package gcewing.lighting.ic2;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import gcewing.lighting.*;

public class BlockFloodlightIC2 extends BlockFloodlight<TEFloodlightIC2> {

	public final boolean switchedOn;
	
	public static class Off extends BlockFloodlightIC2 {
		public Off(int id) {
			super(id, false);
		}
	}

	public static class On extends BlockFloodlightIC2 {
		public On(int id) {
			super(id, true);
		}
	}

	public BlockFloodlightIC2(int id, boolean on) {
		super(id, Material.rock, TEFloodlightIC2.class);
		setHardness(1.5F);
		switchedOn = on;
	}

	@Override
	public void registerIcons(IconRegister reg) {
		String onOff = switchedOn ? "on" : "off";
		icons = new Icon[6];
		icons[0] = getIcon(reg, "flic2-bottom");
		icons[1] = getIcon(reg, "flic2-side-" + onOff);
		icons[2] = getIcon(reg, "flic2-top");
		icons[3] = getIcon(reg, "flic2-bottom-lit");
		icons[4] = getIcon(reg, "flic2-side-lit-" + onOff);
		icons[5] = getIcon(reg, "flic2-top-lit");
	}

	@Override
	public Icon getIcon(int side, int data) {
		int base = 0;
		if ((data & 1) != 0)
			base += 3;
		switch (side) {
			case 0: return icons[base]; // bottom
			case 1: return icons[base + 2]; // top
			default: return icons[base + 1]; // sides
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
		int par6, float par7, float par8, float par9)
	{
		if (!player.isSneaking()) {
			if (!world.isRemote)
				flipSwitch(world, x, y, z);
			return true;
		}
		else
			return false;
	}

//	@Override
//	public TileEntity createNewTileEntity(World world) {
//		return new TEFloodlightIC2();
//	}
	
	public void flipSwitch(World world, int x, int y, int z) {
		//TileEntity te = world.getBlockTileEntity(x, y, z);
		int data = world.getBlockMetadata(x, y, z);
		int newID;
		if (switchedOn)
			newID = GregsLightingIC2.floodlightIC2.blockID;
		else
			newID = GregsLightingIC2.floodlightIC2on.blockID;
		world.setBlock(x, y, z, newID, data, 0x3);
		//world.setBlockTileEntity(x, y, z, te);
		update(world, x, y, z);
		updateBeam(world, x, y, z);
	}

	@Override
	public boolean isActive(World world, int x, int y, int z) {
		TEFloodlightIC2 te = (TEFloodlightIC2)world.getBlockTileEntity(x, y, z);
		return te != null && te.isActive();
	}
	
}
