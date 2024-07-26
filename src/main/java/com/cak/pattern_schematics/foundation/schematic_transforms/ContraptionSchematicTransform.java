package com.cak.pattern_schematics.foundation.schematic_transforms;

import com.cak.pattern_schematics.foundation.mirror.PatternSchematicWorld;
import com.cak.pattern_schematics.foundation.util.Vec3iUtils;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContraptionSchematicTransform<T extends Contraption> {
  public static class Handlers {
    
    public static ContraptionSchematicTransform<Contraption> NONE = new ContraptionSchematicTransform<>(Contraption.class);
    
    public static List<ContraptionSchematicTransform<?>> handlers = List.of(
        new CarriageContraptionTransform()
    );
  
    @SuppressWarnings("unchecked cast") //Type is already checked
    public static <T extends Contraption> ContraptionSchematicTransform<T> get(T contraption) {
      for (ContraptionSchematicTransform<?> handler : Handlers.handlers) {
        if (handler.getTargetClass().isInstance(contraption)) {
          ContraptionSchematicTransform<T> castHandler = (ContraptionSchematicTransform<T>) handler;
          cachedHandlers.put(contraption, castHandler);
          return castHandler;
        }
      }
      return (ContraptionSchematicTransform<T>) NONE;
    }
  
  }
  
  public static Map<Contraption, ContraptionSchematicTransform<?>> cachedHandlers = new HashMap<>();
  
  protected Class<T> target;
  
  public ContraptionSchematicTransform(Class<T> target) {
    this.target = target;
  }
  
  public Class<T> getTargetClass() {
    return target;
  }
  
  public BlockPos modifyPos(T currentContraption, PatternSchematicWorld patternSchematicWorld, BlockPos globalPos) {
    return globalPos;
  }
  
  public BlockState modifyState(T currentContraption, PatternSchematicWorld patternSchematicWorld, BlockState blockState, BlockPos position, LevelAccessor level) {
    return blockState;
  }
  
//  public Vec3i getSourceLengths(T currentContraption, PatternSchematicWorld patternSchematicWorld) {
//    return Vec3iUtils.abs(patternSchematicWorld.getBounds().getLength()).offset(1, 1, 1);
//  }
  
  public BoundingBox getBounds(PatternSchematicWorld patternSchematicWorld) {
    return patternSchematicWorld.getBounds();
  }
  
  public BlockPos mapPositionToRepeatingBounds(T currentContraption, PatternSchematicWorld patternSchematicWorld, BlockPos position) {
    BoundingBox box = getBounds(patternSchematicWorld);
    return new BlockPos(
        repeatingBounds(position.getX(), box.minX(), box.maxX()),
        repeatingBounds(position.getY(), box.minY(), box.maxY()),
        repeatingBounds(position.getZ(), box.minZ(), box.maxZ())
    );
  }
  
  @SuppressWarnings("unchecked cast") //Type is already checked
  public BlockPos castModifyPos(Contraption currentContraption, PatternSchematicWorld patternSchematicWorld, BlockPos globalPos) {
    return modifyPos((T) currentContraption, patternSchematicWorld, globalPos);
  }
  
  @SuppressWarnings("unchecked cast") //Type is already checked
  public BlockState castModifyState(Contraption currentContraption, PatternSchematicWorld patternSchematicWorld, BlockState blockState, BlockPos pos, LevelAccessor level) {
    return modifyState((T) currentContraption, patternSchematicWorld, blockState, pos, level);
  }
  
  @SuppressWarnings("unchecked cast") //Type is already checked
  public BlockPos castApplyRealToSourcePosition(Contraption currentContraption, PatternSchematicWorld patternSchematicWorld, BlockPos position) {
    System.out.println(mapPositionToRepeatingBounds((T) currentContraption, patternSchematicWorld, position));
    return mapPositionToRepeatingBounds((T) currentContraption, patternSchematicWorld, position);
  }
  
  private int repeatingBounds(int source, int min, int max) {
    return (Math.floorMod(source, (max-min)+1) + min);
  }
  
}
