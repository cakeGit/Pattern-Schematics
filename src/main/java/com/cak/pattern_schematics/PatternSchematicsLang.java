package com.cak.pattern_schematics;

public class PatternSchematicsLang {
  
  public static void register() {
    putLang(
        "create_pattern_schematics.schematic.tool.clone", "Clone"
    );
  }
  
  public static void putLang(String... entryKeyPairs) {
    for (int i = 0; i < entryKeyPairs.length/2; i++) {
      PatternSchematics.REGISTRATE
          .addRawLang(entryKeyPairs[i], entryKeyPairs[i+1]);
    }
  }
  
}
