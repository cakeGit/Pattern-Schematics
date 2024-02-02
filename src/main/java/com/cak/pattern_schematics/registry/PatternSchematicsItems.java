package com.cak.pattern_schematics.registry;

import com.cak.pattern_schematics.content.PatternSchematicsItem;
import com.simibubi.create.AllCreativeModeTabs;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static com.cak.pattern_schematics.PatternSchematics.REGISTRATE;

public class PatternSchematicsItems {
  
  public static final ItemEntry<Item> EMPTY_PATTERN_SCHEMATIC = REGISTRATE
      .item("empty_pattern_schematic", Item::new)
      .defaultModel()
      .properties(p -> p.stacksTo(1))
      .tab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
      .register();
  
  public static final ItemEntry<PatternSchematicsItem> PATTERN_SCHEMATIC = REGISTRATE
      .item("pattern_schematic", PatternSchematicsItem::new)
      .defaultModel()
      .properties(p -> p.stacksTo(1))
      //.tab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
      .register();
  
  public static void register() {}
  
}
