package gcewing.lighting;

import java.util.ArrayList;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraftforge.common.*;

public class BlockFloodlight<TE extends TileEntity> extends BaseContainerBlock<TE> {

	static int renderType;
	protected Icon[] icons;

	public BlockFloodlight(int id) {
		this(id, Material.rock, null);
	}
	
	public BlockFloodlight(int id, Class<TE> teClass) {
		this(id, Material.rock, teClass);
	}
	
	public BlockFloodlight(int id, Material mat, Class<TE> teClass) {
		super(id, mat, teClass);
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		icons = new Icon[6];
		icons[0] = getIcon(reg, "floodlight-bottom");
		icons[1] = getIcon(reg, "floodlight-side");
		icons[2] = getIcon(reg, "floodlight-top");
		icons[3] = getIcon(reg, "floodlight-bottom-lit");
		icons[4] = getIcon(reg, "floodlight-side-lit");
		icons[5] = getIcon(reg, "floodlight-top-lit");
		super.registerIcons(reg);
	}
	
	@Override
	public int getRenderType() {
		return renderType;
	}
	
	@Override
	public void setRenderType(int id) {
		renderType = id;
	}
	
//	@Override
//	public String getTextureFile() {
//		return "/gcewing/lighting/resources/textures.png";
//	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hx, float hy, float hz, int initData) {
		return side << 1;
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
	public void onBlockAdded(World world, int x, int y, int z) {
		update(world, x, y, z);
		updateBeam(world, x, y, z);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int metadata) {
		ForgeDirection dir = direction(metadata);
		Floodlight.propagateBeam(world, x, y, z, dir, 0, 1);
		super.breakBlock(world, x, y, z, id, metadata);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		//System.out.printf("BlockFloodlight.onNeighborBlockChange: (%d, %d, %d)\n", x, y, z);
		update(world, x, y, z);
		updateBeam(world, x, y, z);
	}

//	@Override
//	public TileEntity createNewTileEntity(World world) {
//		return null;
//	}
	
	public void update(World world, int x, int y, int z) {
		if (!world.isRemote) {
			boolean active = isActive(world, x, y, z);
			setIlluminated(world, x, y, z, active);
		}
	}

	public void updateBeam(World world, int x, int y, int z) {
		if (!world.isRemote) {
			ForgeDirection dir = getDirection(world, x, y, z);
			//System.out.printf("BlockFloodlight.updateBeam: in direction %s from (%d,%d,%d)\n",
			//	dir, x, y, z);
			Floodlight.updateBeamInDirection(world,
				x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir);
			//System.out.printf("BlockFloodlight.updateBeam: finished %s from (%d,%d,%d)\n",
			//	dir, x, y, z);
		}
	}
	
	public void setIlluminated(World world, int x, int y, int z, boolean state) {
		int oldData = world.getBlockMetadata(x, y, z);
		int newData = (oldData & 0xe) | (state ? 1 : 0);
		if (oldData != newData) {
			//System.out.printf("BlockFloodlight.setIlluminated: changing metadata to %s\n", newData);
			world.setBlockMetadataWithNotify(x, y, z, newData, 0x3);
			world.markBlockForUpdate(x, y, z);
		}
	}

	public boolean isActive(World world, int x, int y, int z) {
		return world.isBlockIndirectlyGettingPowered(x, y, z);
	}
	
	public boolean isActiveInDirection(World world, int x, int y, int z, ForgeDirection dir) {
		return getDirection(world, x, y, z) == dir && isActive(world, x, y, z);
	}
	
	public ForgeDirection getDirection(World world, int x, int y, int z) {
		int data = world.getBlockMetadata(x, y, z);
		return direction(data);
	}
	
	public ForgeDirection direction(int metadata) {
		return ForgeDirection.getOrientation(metadata >> 1);
	}
	
}
