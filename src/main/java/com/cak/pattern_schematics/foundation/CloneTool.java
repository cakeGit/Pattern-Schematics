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
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class CloneTool extends SchematicToolBase {
  
  @Override
  public boolean handleRightClick() {
    return false;
  }
  
  @Override
  public boolean handleMouseWheel(double delta) {
    if (!schematicSelected)
      return true;
    
    if (!(schematicHandler instanceof PatternSchematicHandler patternSchematicHandler))
      throw new RuntimeException("Clone tool bound in a normal SchematicHandler!");
    
    boolean isPositive = selectedFace.getAxisDirection() == Direction.AxisDirection.POSITIVE;
    
    Vec3i cloneScale;
    if (isPositive)
      cloneScale = patternSchematicHandler.getCloneScaleMax();
    else
      cloneScale = patternSchematicHandler.getCloneScaleMin();
  
    cloneScale = cloneScale.relative(selectedFace, Mth.sign(delta));
    
    if (isPositive)
      patternSchematicHandler.setCloneScaleMax(cloneScale);
    else
      patternSchematicHandler.setCloneScaleMin(cloneScale);
  
    schematicHandler.markDirty();
    return true;
  }
  
}
