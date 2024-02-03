package com.cak.pattern_schematics.foundation.mirror;

import com.cak.pattern_schematics.foundation.util.Vec3iUtils;
import com.simibubi.create.content.schematics.SchematicChunkSource;
import com.simibubi.create.content.schematics.SchematicItem;
import com.simibubi.create.content.schematics.SchematicWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class PatternSchematicWorld extends SchematicWorld {
  
  Vec3i cloneScaleMin, cloneScaleMax, cloneOffset;
  BoundingBox sourceBounds;
  
  public PatternSchematicWorld(BlockPos anchor, Level original) {
    super(anchor, original);
    setChunkSource(new SchematicChunkSource(this));
    this.blocks = new HashMap<>();
    this.blockEntities = new HashMap<>();
    this.bounds = new BoundingBox(BlockPos.ZERO);
    this.anchor = anchor;
    this.entities = new ArrayList<>();
    this.renderedBlockEntities = new ArrayList<>();
  }
  
  public void putExtraData(ItemStack blueprint, StructureTemplate template) {
    CompoundTag tag = blueprint.getTag();
    assert tag != null;
    cloneScaleMin = Vec3iUtils.getVec3i("CloneScaleMin", tag);
    cloneScaleMax = Vec3iUtils.getVec3i("CloneScaleMax", tag);
    cloneOffset = Vec3iUtils.getVec3i("CloneOffset", tag);
  
    sourceBounds = template.getBoundingBox(SchematicItem.getSettings(blueprint), anchor);
  }
  
  @Override
  public boolean setBlock(BlockPos pos, BlockState arg1, int arg2) {
    AtomicBoolean result = new AtomicBoolean(false);
    forEachClone(clonePos -> {
      result.set(result.get() || super.setBlock(getRealCloneLoc(pos, clonePos), arg1, arg2));
    });
    return result.get();
  }
  protected  void forEachClone(Consumer<Vec3i> consumer) {
    for (int x = cloneScaleMin.getX(); x <= cloneScaleMax.getX(); x++) {
      for (int y = cloneScaleMin.getY(); y <= cloneScaleMax.getY(); y++) {
        for (int z = cloneScaleMin.getZ(); z <= cloneScaleMax.getZ(); z++) {
          consumer.accept(new Vec3i(x, y, z));
        }
      }
    }
  }
  
  @Override
  public BoundingBox getBounds() {
    return super.getBounds();
  }
  
  protected BlockPos getRealCloneLoc(Vec3i local, Vec3i clone) {
    //return new BlockPos(local);
    System.out.println(sourceBounds);
    return new BlockPos(local.offset(Vec3iUtils.multiplyVec3i(clone, sourceBounds.getLength().multiply(2))));
  }
//
//  protected class PatternWrappedMap<V> extends HashMap<BlockPos, V> {
//
//    @Override
//    public V put(BlockPos key, V value) {
//      System.out.println("jbi");
//      if (value instanceof BlockState) {
//        System.out.println("jbi");
//        forEachClone(clonePos -> {
//          super.put(getRealCloneLoc(key, clonePos), value);
//        });
//      } else {
//        return super.put(key, value);
//      }
//      return value;
//    }
//
//
//
//  }
  
}
