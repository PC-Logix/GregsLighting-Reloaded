package gcewing.lighting;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.renderer.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;

public class GuiFloodlightCarbide extends BaseGuiContainer {

	public static GuiScreen create(EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getBlockTileEntity(x, y, z);
		if (te instanceof TEFloodlightCarbide)
			return new GuiFloodlightCarbide(player, (TEFloodlightCarbide)te);
		else
			return null;
	}

	TEFloodlightCarbide te;
	
	public GuiFloodlightCarbide(EntityPlayer player, TEFloodlightCarbide te) {
		super(new ContainerFLC(player, te));
		this.te = te;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		//fontRenderer.drawString("Carbide Floodlight", 60, 6, 4210752);
		//fontRenderer.drawString("Inventory", 8, ySize - 96 + 2, 4210752);
		beginDrawing();
		// Water in tank
		int level = 24 * te.waterLevel / te.maxWaterLevel;
		//System.out.printf("GuiFloodlightCarbide.drawGuiContainerForegroundLayer(%d, %d): water level = %d\n", par1, par2, level);
		drawTexturedModalRect(76, 32-level, 176, 24-level, 24, level);
		// Water tank scale
		drawTexturedModalRect(76, 8, 208, 0, 12, 24);
		// Water drip
		if (te.isActive())
			drawTexturedModalRect(84, 34, 200, 0, 8, 16);
		// Carbide in chamber
		level = 13 * te.carbideLevel / te.maxCarbideLevel;
		drawTexturedModalRect(83, 58+13-level, 227, 2+13-level, 12, level);
		endDrawing();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindTexture();
		int x0 = (this.width - this.xSize) / 2;
		int y0 = (this.height - this.ySize) / 2;
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glTranslatef(x0, y0, 0);
		this.drawTexturedModalRect(0, 0, 0, 0, this.xSize, this.ySize);
		GL11.glPopMatrix();
		unbindTexture();
	}
	
	void beginDrawing() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindTexture();
	}
	
	void endDrawing() {
		unbindTexture();
	}

//	void bindTexture() {
//		RenderEngine re = this.mc.renderEngine;
//		String path = GregsLighting.mod.resourceDir + "gui_floodlight_carbide.png";
//		re.bindTexture(path);
//	}
	
	void bindTexture() {
		bindTexture(GregsLighting.mod.textureLocation("gui/gui_floodlight_carbide.png"));
	}

	void unbindTexture() {
//		RenderEngine re = this.mc.renderEngine;
//		int tex = re.getTexture("/terrain.png");
//		re.bindTexture(tex);
	}

}
