package gcewing.lighting;

import net.minecraft.block.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;

public class Floodlight {

	public static int maxRange = 64;
	public static boolean debugBeamBlocks = false;
	public static boolean debugBeamBlockPlacement = false;
	
	public static void configure(BaseConfiguration config) {
		maxRange = config.getInteger("options", "maxFloodlightRange", maxRange);
		debugBeamBlocks = config.getBoolean("options", "debugBeamBlocks", false);
	}

	public static void updateBeams(World world, int x, int y, int z) {
		if (debugBeamBlocks)
			System.out.printf("Floodlight.updateBeams at (%d,%d,%d)\n", x, y, z);
		for (int i = 0; i < 6; i++)
			updateBeamInDirection(world, x, y, z, ForgeDirection.getOrientation(i));
	}

	static void updateBeamInDirection(World world, int x, int y, int z, ForgeDirection dir) {
		if (debugBeamBlocks)
			System.out.printf("Floodlight.updateBeamInDirection %s from (%d,%d,%d) in %s\n",
				dir, x, y, z, world);
		int intensity = findSourceIntensity(world, x, y, z, dir);
		propagateBeam(world, x, y, z, dir, intensity, 0);
	}
	
	static int findSourceIntensity(World world, int x, int y, int z, ForgeDirection dir) {
		if (debugBeamBlocks)
			System.out.printf("Floodlight.findSourceIntensity %s from (%d,%d,%d)\n", dir, x, y, z);
		int h = world.getHeight();
		for (int i = 1; i <= maxRange; i++) {
			int sx = x - i * dir.offsetX;
			int sy = y - i * dir.offsetY;
			int sz = z - i * dir.offsetZ;
			if (debugBeamBlocks)
				System.out.printf("Floodlight.findSourceIntensity: Checking (%d,%d,%d)\n", sx, sy, sz);
			if (sy < 0 || sy >= h) {
				if (debugBeamBlocks)
					System.out.printf("Floodlight.findSourceIntensity: Reached edge of map, returning 0\n");
				return 0;
			}
			Block block = getBlock(world, sx, sy, sz);
			if (block == null) {
				if (debugBeamBlocks)
					System.out.printf("Floodlight.findSourceIntensity: Found air, returning 0\n");
				return 0;
			}
			if (debugBeamBlocks)
				System.out.printf("Floodlight.findSourceIntensity: Found %s\n", block.getLocalizedName());
			if (block instanceof BlockFloodlight) {
				if (debugBeamBlocks)
					System.out.printf("Floodlight.findSourceIntensity: Found floodlight\n");
				if (((BlockFloodlight)block).isActiveInDirection(world, sx, sy, sz, dir)) {
					int result = maxRange + 1 - i;
					if (debugBeamBlocks)
						System.out.printf("Floodlight.findSourceIntensity: Is active, returning %d\n", result);
					return result;
				}
			}
			if (block instanceof BlockFloodlightBeam) {
				int intensity = beamIntensity(world, sx, sy, sz, dir);
				if (intensity > 0) /*to allow for old-style beam blocks*/ {
					int result = intensity - i;
					if (debugBeamBlocks)
						System.out.printf("Floodlight.findSourceIntensity: Found beam block, returning %d\n", result);
					return result;
				}
			}
			if (block.isOpaqueCube()) {
				if (debugBeamBlocks)
					System.out.printf("Floodlight.findSourceIntensity: Found opaque block, returning 0\n");
				return 0;
			}
		}
		return 0;
	}
	
	public static void propagateBeam(World world, int x, int y, int z, ForgeDirection dir,
		int intensity, int start)
	{
		if (debugBeamBlocks)
			System.out.printf("Floodlight.propagateBeam %s from (%d,%d,%d) + %d with intensity %d\n",
				dir, x, y, z, start, intensity);
		int h = world.getHeight();
		for (int i = start; i < maxRange; i++) {
			int bx = x + i * dir.offsetX;
			int by = y + i * dir.offsetY;
			int bz = z + i * dir.offsetZ;
			if (debugBeamBlocks)
				System.out.printf("Floodlight.propagateBeam: Checking (%d,%d,%d)\n", bx, by, bz);
			if (by < 0 || by >= h) {
				if (debugBeamBlocks)
					System.out.printf("Floodlight.propagateBeam: Reached edge of map\n");
				return;
			}
			Block block = getBlock(world, bx, by, bz);
			if (block != null && block.isOpaqueCube()) {
				if (debugBeamBlocks)
					System.out.printf("Floodlight.propagateBeam: Found opaque block\n");
				return;
			}
			if (block == null || block instanceof BlockFloodlightBeam)
				if (!setBeamIntensity(world, bx, by, bz, dir, intensity - i) && i != start) {
					if (debugBeamBlocks)
						System.out.printf("Floodlight.propagateBeam: Intensity already correct\n");
					return;
				}
		}
	}

	static Block getBlock(World world, int x, int y, int z) {
		return Block.blocksList[world.getBlockId(x, y, z)];
	}
	
	static int beamIntensity(World world, int x, int y, int z, ForgeDirection dir) {
		TEFloodlightBeam te = getBeamTileEntity(world, x, y, z);
		if (te != null)
			return te.getIntensity(dir);
		else
			return 0;
	}
	
	static boolean setBeamIntensity(World world, int x, int y, int z, ForgeDirection dir, int intensity) {
		// Returns true if intensity was changed
		boolean changed = false;
		if (intensity < 0)
			intensity = 0;
		if (debugBeamBlocks)
			System.out.printf("Floodlight.setBeamIntensity %s at (%d,%d,%d) to %d\n", dir, x, y, z, intensity);
		Block block = getBlock(world, x, y, z);
		if (block == null) {
			if (intensity == 0)
				return false;
			if (debugBeamBlocks || debugBeamBlockPlacement)
				System.out.printf("Floodlight.setBeamIntensity: Placing beam block at (%d,%d,%d)\n", x, y, z);
			block = GregsLighting.floodlightBeam;
			world.setBlock(x, y, z, block.blockID, 0, 0x0);
			world.markBlockForUpdate(x, y, z);
		}
		TEFloodlightBeam te = getBeamTileEntity(world, x, y, z);
		if (te != null) {
			if (debugBeamBlocks)
				System.out.printf("Floodlight.setBeamIntensity: Intensities before = %d, %d, %d, %d, %d, %d\n",
					te.intensity[0], te.intensity[1], te.intensity[2], te.intensity[3], te.intensity[4], te.intensity[5]);
			changed = te.setIntensity(dir, intensity);
			if (debugBeamBlocks)
				System.out.printf("Floodlight.setBeamIntensity: Intensities after = %d, %d, %d, %d, %d, %d\n",
					te.intensity[0], te.intensity[1], te.intensity[2], te.intensity[3], te.intensity[4], te.intensity[5]);
			if (te.allIntensitiesZero()) {
				if (debugBeamBlocks || debugBeamBlockPlacement)
					System.out.printf("Floodlight.setBeamIntensity: Removing beam block at (%d,%d,%d)\n", x, y, z);
				world.setBlock(x, y, z, 0, 0, 0x0);
				world.markBlockForUpdate(x, y, z);
			}
		}
		return changed;
	}
	
	static TEFloodlightBeam getBeamTileEntity(World world, int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof TEFloodlightBeam)
			return (TEFloodlightBeam)te;
		else
			return null;
	}

}
