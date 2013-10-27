//------------------------------------------------------
//
//   Greg's Lighting - Floodlight Beam Block
//
//------------------------------------------------------

package gcewing.lighting;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.util.*;

public class BlockFloodlightBeam extends BaseContainerBlock<TEFloodlightBeam> {

	Icon icons[];

	public BlockFloodlightBeam(int id) {
		super(id, Material.vine, TEFloodlightBeam.class);
		setLightValue(1.0F);
		setLightOpacity(0);
		setHardness(-1);
		setResistance(6000000);
		if (Floodlight.debugBeamBlocks)
			setBlockBounds(3/8.0F, 3/8.0F, 3/8.0F, 5/8.0F, 5/8.0F, 5/8.0F);
		else
			setBlockBounds(0F, 0F, 0F, 0F, 0F, 0F);
		//System.out.printf("BlockFloodlightBeam: Bounds = (%s, %s, %s, %s, %s, %s)\n", 
		//	minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		icons = new Icon[2];
		icons[0] = getIcon(reg, "debugbeam");
		icons[1] = getIcon(reg, "debugbeam-dot");
		super.registerIcons(reg);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TEFloodlightBeam();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
    
	@Override
	public boolean isAirBlock(World world, int x, int y, int z)  {
		return true;
	}
	
	@Override
	public boolean canBeReplacedByLeaves(World world, int x, int y, int z) {
		return true;
	}
	
	@Override
	public boolean isLeaves(World world, int x, int y, int z) {
		// This is a bit screwy, but it's needed so that trees are not prevented from growing
		// near a floodlight beam.
		return true;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return Floodlight.debugBeamBlocks;
	}
	
	@Override
	public int quantityDropped(Random par1Random) {
		return 0;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
		return null;
	}

	@Override
	public int getRenderType() {
		if (Floodlight.debugBeamBlocks)
			return super.getRenderType();
		else
			return -1;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int neighborBlockID) {
		//System.out.printf(
		//	"BlockFloodlightBeam.onNeighborBlockChange: updating beams from (%d, %d, %d)\n",
		//	x, y, z);
		Floodlight.updateBeams(world, x, y, z);
		//System.out.printf(
		//	"BlockFloodlightBeam.onNeighborBlockChange: updated beams from (%d, %d, %d)\n",
		//	x, y, z);
	}
	
	@Override
	public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side) {
		//System.out.printf("BlockFloodlightBeam.getBlockTexture for side %d at (%d,%d,%d)\n",
		//	side, x, y, z);
		TEFloodlightBeam te = (TEFloodlightBeam)world.getBlockTileEntity(x, y, z);
		//System.out.printf("BlockFloodlightBeam.getBlockTexture: intensity = %d\n", te.intensity[side]);
		if (te.intensity[side] > 0)
			return icons[1];
		else
			return icons[0];
	}

}
