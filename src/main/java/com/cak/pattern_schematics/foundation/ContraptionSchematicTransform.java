package com.cak.pattern_schematics.foundation;

import com.cak.pattern_schematics.foundation.mirror.PatternSchematicWorld;
import com.cak.pattern_schematics.foundation.util.ReflectionUtils;
import com.cak.pattern_schematics.foundation.util.Vec3iUtils;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

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
  
  public BlockPos modifyPos(T currentContraption, BlockPos globalPos) {
    return globalPos;
  }
  
  public BlockState modifyState(T currentContraption, BlockState blockState, BlockPos position, LevelAccessor level) {
    return blockState;
  }
  
  public Vec3i getSourceLengths(T currentContraption, PatternSchematicWorld patternSchematicWorld) {
    return Vec3iUtils.abs(patternSchematicWorld.getBounds().getLength()).offset(1, 1, 1);
  }
  
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
  public BlockPos castModifyPos(Contraption currentContraption, BlockPos globalPos) {
    return modifyPos((T) currentContraption, globalPos);
  }
  
  @SuppressWarnings("unchecked cast") //Type is already checked
  public BlockState castModifyState(Contraption currentContraption, BlockState blockState, BlockPos pos, LevelAccessor level) {
    return modifyState((T) currentContraption, blockState, pos, level);
  }
  
  @SuppressWarnings("unchecked cast") //Type is already checked
  public BlockPos castApplyRealToSourcePosition(Contraption currentContraption, PatternSchematicWorld patternSchematicWorld, BlockPos position) {
    System.out.println(mapPositionToRepeatingBounds((T) currentContraption, patternSchematicWorld, position));
    return mapPositionToRepeatingBounds((T) currentContraption, patternSchematicWorld, position);
  }
  
  private int repeatingBounds(int source, int min, int max) {
    return (Math.floorMod(source, (max-min)+1) + min);
  }
  
  public static class CarriageContraptionTransform extends ContraptionSchematicTransform<CarriageContraption> {
  
    public CarriageContraptionTransform() {
      super(CarriageContraption.class);
    }
  
    @Override
    public BlockPos modifyPos(CarriageContraption currentContraption, BlockPos globalPos) {
      return globalPos.rotate(getRotationOfTrainContraption(currentContraption));
    }
  
    @Override
    public Vec3i getSourceLengths(CarriageContraption currentContraption, PatternSchematicWorld patternSchematicWorld) {
      return Vec3iUtils.abs(new BlockPos(super.getSourceLengths(currentContraption, patternSchematicWorld))
          .rotate(getRotationOfTrainContraption(currentContraption)));
    }
    @Override
    public BlockState modifyState(CarriageContraption currentContraption, BlockState blockState, BlockPos position, LevelAccessor level) {
      return super.modifyState(currentContraption, blockState, position, level)
          .rotate(level, position, getRotationOfTrainContraption(currentContraption));
    }
    
    public boolean inRange(double a, double b, double range) {
      double dist = range / 2f;
      return b >= a-dist && b <= a + dist;
    }
    
    public Rotation getRotationOfAngle(double radians) {
      for (Rotation rotation : Rotation.values())
        if (inRange(rotation.ordinal() * (Math.PI/2), radians, Math.PI/2))
          return rotation;
      System.out.println("a");
      return Rotation.NONE;
    }
    
    public Rotation getRotationOfTrainContraption(CarriageContraption contraption) {
      Vec3 orientation = contraption.entity.applyRotation(new Vec3(1, 0, 0), 1f);
  
      Rotation rotation = getRotationOfAngle(Math.atan2(orientation.x, orientation.z));
      
      return rotation;
    }
  
    protected static Rotation getRotation(Direction direction) {
      return Rotation.values()[direction.get2DDataValue()];
    }
  
  }
  
}
