package com.cak.pattern_schematics.foundation;

import com.cak.pattern_schematics.foundation.mirror.PatternSchematicHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.schematics.client.SchematicTransformation;
import com.simibubi.create.content.schematics.client.tools.ISchematicTool;
import com.simibubi.create.content.schematics.client.tools.SchematicToolBase;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class CloneTool extends SchematicToolBase {
  
  @Override
  public boolean handleRightClick() {
    return false;
  }
  
  @Override
  public boolean handleMouseWheel(double delta) {
    if (!schematicSelected || !selectedFace.getAxis().isHorizontal())
      return true;
  
    SchematicTransformation transformation = schematicHandler.getTransformation();
    
    if (!(schematicHandler instanceof PatternSchematicHandler patternSchematicHandler))
      throw new RuntimeException("Clone tool bound in a normal SchematicHandler!");
  
    patternSchematicHandler.setCloneData(
        new Vec3i(-1, 0, -1),
        new Vec3i(1, 0, 1)
    );
    schematicHandler.markDirty();
  
    return true;
  }
  
}
