package com.cak.pattern_schematics.registry;

import com.cak.pattern_schematics.PatternSchematics;

public class PatternSchematicsLang {
  
  public static void register() {
    putLang(
        "create_pattern_schematics.schematic.tool.clone", "Clone",
        "create_pattern_schematics.schematic.tool.clone.description.0", "Repeats the selected schematic",
        "create_pattern_schematics.schematic.tool.clone.description.1", "Point at the Schematic and [CTRL]-Scroll to repeat it.",
        "create_pattern_schematics.schematic.tool.clone.description.2", "",
        "create_pattern_schematics.schematic.tool.clone.description.3", ""
    );
  }
  
  public static void putLang(String... entryKeyPairs) {
    for (int i = 0; i < entryKeyPairs.length; i+=2) {
      PatternSchematics.REGISTRATE
          .addRawLang(entryKeyPairs[i], entryKeyPairs[i+1]);
    }
  }
  
}
