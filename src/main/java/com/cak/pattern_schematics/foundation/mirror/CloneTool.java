package com.cak.pattern_schematics.foundation.mirror;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.schematics.client.tools.ISchematicTool;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class CloneTool implements ISchematicTool {
  
  @Override
  public void init() {
  
  }
  @Override
  public void updateSelection() {
  
  }
  @Override
  public boolean handleRightClick() {
    return false;
  }
  @Override
  public boolean handleMouseWheel(double delta) {
    return false;
  }
  @Override
  public void renderTool(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera) {
  
  }
  @Override
  public void renderOverlay(ForgeGui gui, GuiGraphics graphics, float partialTicks, int width, int height) {
  
  }
  @Override
  public void renderOnSchematic(PoseStack ms, SuperRenderTypeBuffer buffer) {
  
  }
  
}
