package com.cak.pattern_schematics.foundation.mirror;

import com.simibubi.create.content.schematics.SchematicChunkSource;
import com.simibubi.create.content.schematics.SchematicWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.HashMap;

public class PatternSchematicWorld extends SchematicWorld {
  
  public PatternSchematicWorld(Level original) {
    super(original);
  }
  
  public PatternSchematicWorld(BlockPos anchor, Level original) {
    super(original);
    setChunkSource(new SchematicChunkSource(this));
    this.blocks = new HashMap<>();
    this.blockEntities = new HashMap<>();
    this.bounds = new BoundingBox(BlockPos.ZERO);
    this.anchor = anchor;
    this.entities = new ArrayList<>();
    this.renderedBlockEntities = new ArrayList<>();
  }
  
  public void putCloneData(ItemStack blueprint) {
  
  }
  
}
