//------------------------------------------------------------------------------------------------
//
//   Greg's Mod Base - Generic Block
//
//------------------------------------------------------------------------------------------------

package gcewing.lighting;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;

public class BaseBlock extends BaseContainerBlock<TileEntity> {

	public BaseBlock(int id, Material material) {
		super(id, material, null);
	}

}
