//------------------------------------------------------
//
//   Greg's Lighting - Main Class
//
//------------------------------------------------------

package gcewing.lighting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Hashtable;

import net.minecraft.block.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;

import net.minecraftforge.common.*;
import net.minecraftforge.oredict.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.registry.*;
import cpw.mods.fml.common.event.*;

@Mod(modid = Info.modID, name = Info.modName, version = Info.versionNumber)

@NetworkMod(clientSideRequired = true, serverSideRequired = true, versionBounds = Info.versionBounds)

public class GregsLighting extends BaseMod {

	public final static String modName = "GregsLighting";
//	public final static String textureDir = "/gcewing/lighting/resources/";
//	public final static String textureFile = textureDir + "textures.png";
	
	public final static int guiFloodlightCarbide = 1;

	public static BlockFloodlightCarbide floodlightCarbide;
	public static BlockFloodlight floodlight;
	public static BlockFloodlightBeam floodlightBeam;

	public static Item calciumCarbide;
	public static Item bonemealAndCharcoal;
	public static Item gaslightMantle;
	public static Item gasNozzle;
	public static Item glowingIngot;
	public static Item ic2Filament;
	public static Item ic2Bulb;
	
//	static String configName = modName + ".cfg";
//	static File cfgfile;
//	static OrderedProperties config;
//	static Map <Integer, String> idToName = new Hashtable <Integer, String>();
//	static int nextBlockID = 1;
//	static int nextItemID = 1;
//	static boolean autoAssign = true;
	
//	public static boolean isServer;
	public static GregsLighting mod;
//	public static CreativeTabs itemTab = CreativeTabs.tabMisc;
	
//	@SidedProxy(
//		clientSide = "gcewing.lighting.GregsLightingClient",
//		serverSide = "gcewing.lighting.GregsLightingServer")
//	public static GregsLightingBase proxy;
	
	public GregsLighting() {
		super();
		mod = this;
//		isServer = server;
	}
	
	@Override
	BaseModClient initClient() {
		return new GregsLightingClient(this);
	}

//	public boolean clientSideRequired() {
//		return true;
//	}
//	
//	public boolean serverSideRequired() {
//		return true;
//	}

	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		Floodlight.configure(config);
	}

	@Mod.Init
	public void init(FMLInitializationEvent e) {
		super.init(e);
//		File mcdir = proxy.getMinecraftDirectory();
//		load(mcdir);
//		proxy.load();
	}

	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

//	public void load(File mcdir) {
//		//System.out.printf("GregsLighting: load()\n");
//		//showLoadedMods();
//		NetworkRegistry.instance().registerGuiHandler(this, proxy);
//		loadConfig(mcdir);
//		registerBlocks();
//		registerTileEntities();
//		registerItems();
//		addRecipes();
////		registerChannels();
//		saveConfig();
//	}
	
	void registerOther() {
		registerIC2();
	}
	
//	void showLoadedMods() {
//		for (Object mod : ModLoader.getLoadedMods()) {
//			System.out.printf("GregsLighting: Mod present: %s\n",
//				((BaseMod)mod).getName());
//		}
//	}
	
//	void loadConfig(File mcdir) {
//		File cfgdir = new File(mcdir, "/config/");
//		cfgfile = new File(cfgdir, configName);
//		config = new OrderedProperties();
//		try {
//			config.load(new FileInputStream(cfgfile));
//		}
//		catch (FileNotFoundException e) {
//			System.out.printf("%s: No existing config file\n", modName);
//		}
//		catch (IOException e) {
//			System.out.printf("%s: Failed to read %s\n%s\n", modName,
//				cfgfile.getPath(), e);
//		}
////		autoAssign = getConfigBool("autoAssign", false);
//		config.extended = false;
//		Floodlight.init(config);
//	}
	


//	public String getVersion() {
//		return version;
//	}
	
//	void registerChannels() {
//		ModLoader.registerPacketChannel(this, channelName);
//	}
	
	void registerBlocks() {
		//floodlightCarbide = new BlockFloodlightCarbide(getBlockID(/*253*/1032, "carbideFloodlight"));
		floodlightCarbide = newBlock("carbideFloodlight", BlockFloodlightCarbide.class, "Carbide Floodlight");
		floodlightCarbide.setHardness(1.5F).setCreativeTab(CreativeTabs.tabMisc);
		//addBlock("Carbide Floodlight", floodlightCarbide);
		//floodlightBeam = new BlockFloodlightBeam(getBlockID(/*254*/1031, "floodlightBeam"));
		floodlightBeam = newBlock("floodlightBeam", BlockFloodlightBeam.class, "[Floodlight Beam]");
		//addBlock("Floodlight Beam", floodlightBeam);
		//floodlight = new BlockFloodlight(getBlockID(/*255*/1030, "floodlight"));
		floodlight = newBlock("floodlight", BlockFloodlight.class, "Floodlight");
		floodlight.setHardness(1.5F).setCreativeTab(CreativeTabs.tabMisc);
		//addBlock("Floodlight", floodlight);
	}
	
//	void registerTileEntities() {
//		GameRegistry.registerTileEntity(TEFloodlightCarbide.class, "gcewing.CarbideFloodlight");
//		GameRegistry.registerTileEntity(TEFloodlightBeam.class, "gcewing.FloodlightBeam");
//	}
	
	void registerItems() {
		//calciumCarbide = addItem("Calcium Carbide", newItemI(10300, "calciumCarbide", 0x20));
		calciumCarbide = newItem("calciumCarbide", "Calcium Carbide");
		//bonemealAndCharcoal = addItem("Bonemeal-Charcoal Mixture", newItemI(10301, "bonemealAndCharcoal", 0x21));
		bonemealAndCharcoal = newItem("bonemealAndCharcoal", "Bonemeal-Charcoal Mixture");
		//gaslightMantle = addItem("Gaslight Mantle", newItemI(10302, "gaslightMantle", 0x22));
		gaslightMantle = newItem("gaslightMantle", "Gaslight Mantle");
		//ic2Bulb = GregsLighting.addItem("IC2 Floodlight Bulb", GregsLighting.newItemI(10303, "glowstoneBulb", 0x23));
		ic2Bulb = newItem("glowstoneBulb", "IC2 Floodlight Bulb");
		//gasNozzle = addItem("Gas Nozzle", newItemI(10304, "gasNozzle", 0x25));
		gasNozzle = newItem("gasNozzle", "Gas Nozzle");
		//glowingIngot = addItem("Glowing Alloy Ingot", newItemI(10305, "glowingIngot", 0x27));
		glowingIngot = newItem("glowingIngot", "Glowing Alloy Ingot");
		//ic2Filament = GregsLighting.addItem("IC2 Filament Assembly", GregsLighting.newItemI(10306, "ic2FilamentAssembly", 0x28));
		ic2Filament = newItem("ic2FilamentAssembly", "IC2 Filament Assembly");
	}

	void registerRecipes() {
		newShapelessRecipe(bonemealAndCharcoal, 1, new ItemStack(Item.dyePowder, 1, 15), new ItemStack(Item.coal, 1, 1));
		if (config.getBoolean("options", "enableSimpleFloodlight", true)) {
			newRecipe(floodlight, 1, "IrI", "IgI", "GGG",
				'I', Item.ingotIron, 'r', Item.redstone, 'g', glowingIngot, 'G', Block.glass);
			newRecipe(glowingIngot, 1, "GGG", "GgG", "GGG",
				'G', Block.glowStone, 'g', Item.goldNugget);
		}
		newSmeltingRecipe(calciumCarbide, 1, bonemealAndCharcoal);
		//newRecipe(gaslightMantle, 4, "WgW", "gdg", "WgW",
		//	'W', Block.cloth, 'g', Item.lightStoneDust, 'd', Item.diamond);
		newRecipe(gasNozzle, 4, "I", "I",
			'I', Item.ingotIron);
		//newRecipe(floodlightCarbide, 1, "CrC", "CmC", "GGG",
		//	'C', Block.cobblestone, 'r', Item.redstone, 'm', gaslightMantle, 'G', Block.glass);
		newRecipe(floodlightCarbide, 1, "CrC", "CnC", "GGG",
			'C', Block.cobblestone, 'r', Item.redstone, 'n', gasNozzle, 'G', Block.glass);
	}
	
	@Override
	void registerContainers() {
		//System.out.printf("GregsLighting.registerContainers\n");
		addContainer(guiFloodlightCarbide, ContainerFLC.class);
	}

	void registerIC2() {
		if (Loader.isModLoaded("IC2")) {
			System.out.printf("GregsLighting: Linking to IC2\n");
			try {
				Class cls = Class.forName("gcewing.lighting.ic2.GregsLightingIC2");
				java.lang.reflect.Method init = cls.getMethod("init");
				//System.out.printf("GregsLighting.registerIC2: Invoking %s\n", init);
				init.invoke(null);
			}
			catch (java.lang.reflect.InvocationTargetException e) {
				throw new RuntimeException(e.getCause());
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
//	public static void addRecipe(Item product, int qty, Object... params) {
//		GameRegistry.addRecipe(new ItemStack(product, qty), params);
//	}
//
//	public static void addRecipe(Block product, int qty, Object... params) {
//		GameRegistry.addRecipe(new ItemStack(product, qty), params);
//	}
//
//	public static void addOreRecipe(Item product, int qty, Object... params) {
//		IRecipe recipe = new ShapedOreRecipe(new ItemStack(product, qty), params);
//		CraftingManager.getInstance().getRecipeList().add(recipe);
//	}
//
//	public static void addShapelessRecipe(Item product, int qty, Object... params) {
//		GameRegistry.addShapelessRecipe(new ItemStack(product, qty), params);
//	}
//
//	public static void addSmeltingRecipe(Item product, int qty, Item input) {
//		GameRegistry.addSmelting(input.shiftedIndex, new ItemStack(product, qty), 0);
//	}
//	
//	public static void addSmeltingRecipe(Item product, int qty, Block input) {
//		GameRegistry.addSmelting(input.blockID, new ItemStack(product, qty), 0);
//	}
	
//	public static int getBlockID(int defaultID, String name) {
//		if (autoAssign)
//			defaultID = nextUnusedBlockID();
//		return getBlockOrItemID(defaultID, "block." + name, 0);
//	}
//	
//	public static int getItemID(int defaultID, String name) {
//		if (autoAssign)
//			defaultID = nextUnusedItemID();
//		return getBlockOrItemID(defaultID, "item." + name, 256);
//	}
//	
//	public static int getBlockOrItemID(int defaultID, String name, int offset) {
//		String key = name + ".id";
//		int id;
//		if (config.containsKey(key)) {
//			String value = (String) config.get(key);
//			id = Integer.parseInt(value);
//		}
//		else {
//			config.put(key, Integer.toString(defaultID));
//			id = defaultID;
//		}
//		idToName.put(Integer.valueOf(offset + id), name);
//		return id;
//	}

//	public static Item newItem(int id, String name) {
//		return customItem(getItemID(id, name));
//	}
//	
//	public static Item newItemI(int id, String name, int icon) {
//		return customItem(getItemID(id, name)).setIconIndex(icon);
//	}
//	
//	public static Item newItemSI(int id, String name, int maxStack, int icon) {
//		return customItem(getItemID(id, name)).setMaxStackSize(maxStack).setIconIndex(icon);
//	}
//	
//	public static Item customItem(int id) {
//		return new TexturedItem(id);
//	}
	
//	public static Block addBlock(String name, Block block) {
//		//System.out.printf("%s: Adding block %s id %s\n", modName, name, block.blockID);
//		block.setBlockName("gcewing." + idToName.get(block.blockID));
//		GameRegistry.registerBlock(block);
//		LanguageRegistry.addName(block, name);
//		return block;
//	}
//
//	public static Item addItem(String name, Item item) {
//		item.setItemName("gcewing." + idToName.get(item.shiftedIndex));
//		LanguageRegistry.addName(item, name);
//		item.setCreativeTab(itemTab);
//		return item;
//	}
	
//	public boolean shiftKeyDown() {
//		return false;
//	}
	
//	static int nextUnusedBlockID() {
//		//System.out.printf("%s: nextUnusedBlockID\n", modName);
//		while (nextBlockID < 4096) {
//			if (Block.blocksList[nextBlockID] == null)
//				return nextBlockID;
//			nextBlockID += 1;
//		}
//		throw new RuntimeException(modName + ": Out of block IDs");
//	}
//	
//	static int nextUnusedItemID() {
//		//System.out.printf("%s: nextUnusedItemID\n", modName);
//		while (nextItemID < 32768) {
//			if (Item.itemsList[nextItemID + 256] == null)
//				return nextItemID;
//			nextItemID += 1;
//		}
//		throw new RuntimeException(modName + ": Out of item IDs");
//	}

	//-----------------------------------------------------------------------------------------
	
	public static void openGuiFloodlightCarbide(World world, int x, int y, int z, EntityPlayer player) {
		//System.out.printf("GregsLighting.openGuiFloodlightCarbide\n");
		mod.openGui(player, guiFloodlightCarbide, world, x, y, z);
	}

	public static boolean explodeMachineAt(World world, int x, int y, int z) {
		try {
			Class<?> mainIC2Class = Class.forName("ic2.common.IC2");
			mainIC2Class.getMethod("explodeMachineAt", World.class, Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(null, world, x, y, z);
			return true;
		}
		catch (Exception e) {
			System.out.printf("GregsLighting.explodeMachineAt: %s\n", e);
			return false;
		}
	}

}
