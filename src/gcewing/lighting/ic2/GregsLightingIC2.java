//------------------------------------------------------
//
//   Greg's Lighting - IC2
//
//------------------------------------------------------

package gcewing.lighting.ic2;

import net.minecraft.block.*;
import net.minecraft.creativetab.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import cpw.mods.fml.common.registry.*;
import ic2.api.item.Items;
import gcewing.lighting.*;

public class GregsLightingIC2 {

	public static BlockFloodlight floodlightIC2, floodlightIC2on;
	public static ItemStack tinCable, insulatedCopperCable;
	static GregsLighting mod;

	public static void init() {
		mod = GregsLighting.mod;
		try {
			getItems();
		}
		catch (MissingItem e) {
			System.out.printf("GregsLighting: Not all IC2 items available\n");
			return;
		}
		registerBlocks();
		registerItems();
		registerRecipes();
//		registerTileEntities();
	}
	
	static void registerBlocks() {
		//floodlightIC2 = new BlockFloodlightIC2(GregsLighting.getBlockID(1033, "ic2ElectricFloodlight"), false);
		floodlightIC2 = mod.newBlock("ic2ElectricFloodlight", BlockFloodlightIC2.Off.class, "IC2 Electric Floodlight");
		//floodlightIC2on = new BlockFloodlightIC2(GregsLighting.getBlockID(1034, "ic2ElectricFloodlightOn"), true);
		floodlightIC2on = mod.newBlock("ic2ElectricFloodlightOn", BlockFloodlightIC2.On.class, "[IC2 Electric Floodlight - On]");
		//GregsLighting.addBlock("IC2 Electric Floodlight", floodlightIC2);
	}
	
	static void registerItems() {
	}

	static void registerRecipes() {
		mod.newRecipe(GregsLighting.ic2Filament, 1, " t ", "t t", "cic",
			't', tinCable, 'i', insulatedCopperCable, 'c', "ingotCopper");
		mod.newRecipe(GregsLighting.ic2Bulb, 1, "rGr", "GfG",
			'G', Block.glass, 'f', GregsLighting.ic2Filament, 'r', Item.redstone);
		mod.newRecipe(floodlightIC2, 1, "IrI", "IbI", "GGG",
			'I', Item.ingotIron, 'r', Item.redstone, 'b', GregsLighting.ic2Bulb, 'G', Block.glass);
	}
	
//	static void registerTileEntities() {
//		GameRegistry.registerTileEntity(TEFloodlightIC2.class, "gcewing.IC2ElectricFloodlight");
//	}
	
	static void getItems() throws MissingItem {
		tinCable = ic2Item("tinCableItem");
		insulatedCopperCable = ic2Item("insulatedCopperCableItem");
	}
	
	static ItemStack ic2Item(String name) throws MissingItem {
		ItemStack stack = Items.getItem(name);
		if (stack == null)
			throw new MissingItem();
		return stack;
	}

}

class MissingItem extends Exception {
}

