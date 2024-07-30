package com.cak.pattern_schematics;

import com.cak.pattern_schematics.foundation.mirror.PatternSchematicHandler;

public class PatternSchematicsClient {
  
  public static final PatternSchematicHandler PATTERN_SCHEMATIC_HANDLER = new PatternSchematicHandler();
  //todo: use?
  public static void invalidateRenderers() {
    PATTERN_SCHEMATIC_HANDLER.updateRenderers();
  }
  
}
