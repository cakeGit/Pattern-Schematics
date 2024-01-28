package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.PatternSchematicsClient;
import com.cak.pattern_schematics.foundation.PatternSchematicsToolType;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.client.SchematicHandler;
import com.simibubi.create.content.schematics.client.tools.SchematicToolBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SchematicToolBase.class, remap = false)
public class SchematicToolBaseMixin {
  
  @Shadow
  protected SchematicHandler schematicHandler;
  
  /**Praise be to simi that none of the calls to this are specific enough that a slapped on extends in {@link com.cak.pattern_schematics.foundation.mirror.PatternSchematicHandler} works*/
  @Inject(method = "init", at = @At("TAIL"))
  public void init(CallbackInfo ci) {
    schematicHandler =
        (PatternSchematicsToolType.isPatternSchematicTool((SchematicToolBase) (Object) this) ?
        PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER : CreateClient.SCHEMATIC_HANDLER);
  }
  
}
