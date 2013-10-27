package gcewing.lighting;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.util.*;

public class BlockFloodlightCarbide extends BlockFloodlight implements ITileEntityProvider {

	public BlockFloodlightCarbide(int id) {
		super(id, Material.rock, TEFloodlightCarbide.class);
	}
	
	@Override
	public void registerIcons(IconRegister reg) {
		icons = new Icon[6];
		icons[0] = getIcon(reg, "flcarbide-bottom");
		icons[1] = getIcon(reg, "flcarbide-side");
		icons[2] = getIcon(reg, "flcarbide-top");
		icons[3] = getIcon(reg, "flcarbide-bottom-lit");
		icons[4] = getIcon(reg, "flcarbide-side-lit");
		icons[5] = getIcon(reg, "flcarbide-top-lit");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
		int par6, float par7, float par8, float par9)
	{
		System.out.printf("BlockFloodlightCarbide.onBlockActivated: %s\n", world);
		if (!player.isSneaking()) {
			if (!world.isRemote) {
				System.out.printf("BlockFloodlightCarbide.blockActivated: opening gui\n");
				GregsLighting.openGuiFloodlightCarbide(world, x, y, z, player);
			}
			return true;
		}
		else
			return false;
	}

//	@Override
//	public TileEntity createNewTileEntity(World world) {
//		//System.out.printf("BlockFloodlightCarbide.createNewTileEntity\n");
//		return new TEFloodlightCarbide();
//	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int metadata) {
		Utils.dumpInventoryIntoWorld(world, x, y, z);
		super.breakBlock(world, x, y, z, id, metadata);
	}
	
	@Override
	public boolean isActive(World world, int x, int y, int z) {
		TEFloodlightCarbide te = (TEFloodlightCarbide)world.getBlockTileEntity(x, y, z);
		return te != null && te.isActive();
	}
	
	@Override
	public void update(World world, int x, int y, int z) {
		if (!world.isRemote) {
			TEFloodlightCarbide te = (TEFloodlightCarbide)world.getBlockTileEntity(x, y, z);
			if (te != null)
				te.refillCarbide();
		}
		super.update(world, x, y, z);
	}

}
