package com.cak.pattern_schematics.foundation.mirror;

import com.cak.pattern_schematics.foundation.util.Vec3iUtils;
import com.simibubi.create.content.schematics.SchematicChunkSource;
import com.simibubi.create.content.schematics.SchematicItem;
import com.simibubi.create.content.schematics.SchematicWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;

public class PatternSchematicWorld extends SchematicWorld {
  
  /**Tracking data from {@link com.cak.pattern_schematics.mixin.StructureTemplateMixin}*/
  public static boolean isPlacingTemplate;
  public static boolean isPlacingUpdate;
  
  public Vec3i cloneScaleMin;
  public Vec3i cloneScaleMax;
  public Vec3i cloneOffset;
  public BoundingBox sourceBounds;
  
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
  
  public boolean setCloneBlock(BlockPos pos, BlockState state, int i) {
    AtomicBoolean result = new AtomicBoolean(false);
    System.out.println(pos);
    forEachClone(clonePos -> {
      System.out.println("=>" + applyCloneToRealLoc(pos, clonePos));
      if (super.setBlock(applyCloneToRealLoc(pos, clonePos), state, i))
        result.set(true);
    });
    return result.get();
  }
  
  @Override
  public boolean addFreshEntity(Entity entityIn) {
    return applyToClones(clonePos -> {
      Entity newEntity = cloneEntity(entityIn);
      newEntity.setPos(applyCloneToRealLoc(newEntity.position(), clonePos));
      return super.addFreshEntity(newEntity);
    });
  }
  
  protected Entity cloneEntity(Entity source) {
    CompoundTag tag = new CompoundTag();
    source.save(tag);
    Entity newEntity = EntityType.create(tag, world).orElseThrow();
    newEntity.setUUID(UUID.randomUUID());
    return newEntity;
  }
  
  protected boolean applyToClones(Function<Vec3i, Boolean> function) {
    AtomicBoolean result = new AtomicBoolean(false);
    forEachClone(clonePos -> {
      if (function.apply(clonePos))
        result.set(true);
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
    Vec3i lengths = sourceBounds.getLength().offset(1, 1, 1);
  
    Vec3i minBounds = Vec3iUtils.multiplyVec3i(lengths, cloneScaleMin);
    Vec3i maxBounds = Vec3iUtils.multiplyVec3i(lengths, cloneScaleMax.offset(1, 1, 1));

    return BoundingBox.fromCorners(minBounds, maxBounds);
  }
  public BlockPos applyCloneToRealLoc(Vec3i local, Vec3i clone) {
    return new BlockPos(local.offset(Vec3iUtils.multiplyVec3i(clone, sourceBounds.getLength().offset(1, 1, 1))));
  }
  public Vec3 applyCloneToRealLoc(Vec3 local, Vec3i clone) {
    return local.add(Vec3.atLowerCornerOf(Vec3iUtils.multiplyVec3i(clone, sourceBounds.getLength().offset(1, 1, 1))));
  }
  
  public BlockPos applyRealToSourceLoc(BlockPos local) {
    Vec3i length = sourceBounds.getLength().offset(1, 1, 1);
    return new BlockPos(
        repeatingBounds(local.getX(), length.getX()),
        repeatingBounds(local.getY(), length.getY()),
        repeatingBounds(local.getZ(), length.getZ())
    );
  }
  
  private int repeatingBounds(int source, int length) {
    return (length + (source % length)) % length;
  }
  
}
