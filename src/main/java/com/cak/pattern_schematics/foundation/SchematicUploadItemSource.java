package com.cak.pattern_schematics.foundation;

import com.cak.pattern_schematics.PatternSchematicItem;
import com.cak.pattern_schematics.PatternSchematics;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.schematics.SchematicItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;

public enum SchematicUploadItemSource {
  
  DEFAULT(SchematicItem::create, 0, AllItems.EMPTY_SCHEMATIC),
  PATTERN(PatternSchematicItem::create, 1, PatternSchematics.EMPTY_PATTERN_SCHEMATIC)
  ;
  
  final SchematicItemFactory factory;
  final int nbtValue;
  final ItemEntry<Item> schematicSourceItem;
  
  SchematicUploadItemSource(SchematicItemFactory factory, int nbtValue, ItemEntry<Item> schematicSourceItem) {
    this.factory = factory;
    this.nbtValue = nbtValue;
    this.schematicSourceItem = schematicSourceItem;
  }
  
  public SchematicItemFactory getFactory() {
    return factory;
  }
  
  public int getNbtValue() {
    return nbtValue;
  }
  
  public ItemEntry<Item> getSchematicSourceItem() {
    return schematicSourceItem;
  }
  
  public static SchematicUploadItemSource tryFromItemStack(ItemStack stack) {
    return Arrays.stream(values())
        .filter(source -> source.getSchematicSourceItem().isIn(stack))
        .findAny().orElse(null);
  }
  
  public static SchematicUploadItemSource tryFromInt(int nbtValue) {
    return Arrays.stream(values())
        .filter(source -> source.getNbtValue() == nbtValue)
        .findAny().orElse(null);
  }
  
  public static interface SchematicItemFactory {
    ItemStack create(HolderGetter<Block> lookup, String schematic, String owner);
  }
  
}
