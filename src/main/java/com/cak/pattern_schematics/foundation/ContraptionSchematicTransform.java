package com.cak.pattern_schematics.foundation;

import com.cak.pattern_schematics.foundation.mirror.PatternSchematicWorld;
import com.cak.pattern_schematics.foundation.util.ReflectionUtils;
import com.cak.pattern_schematics.foundation.util.Vec3iUtils;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.piston.PistonContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContraptionSchematicTransform<T extends Contraption> {
  public static class Handlers {
    
    public static ContraptionSchematicTransform<Contraption> NONE = new ContraptionSchematicTransform<>(Contraption.class);
    
    public static List<ContraptionSchematicTransform<?>> handlers = List.of(
        new ContraptionSchematicTransform.PistonContraptionTransform()
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
    System.out.println("up to date");
    return Vec3iUtils.abs(patternSchematicWorld.getBounds().getLength()).offset(1, 1, 1);
  }
  
  public BlockPos applyRealToSourcePosition(T currentContraption, PatternSchematicWorld patternSchematicWorld, BlockPos position) {
    Vec3i length = getSourceLengths(currentContraption, patternSchematicWorld);
    System.out.println(length);
    // ok so like seperate out the clamp pos stuff
    return new BlockPos(
        repeatingBounds(position.getX(), length.getX()),
        repeatingBounds(position.getY(), length.getY()),
        repeatingBounds(position.getZ(), length.getZ())
    ).offset(new Vec3i(patternSchematicWorld.getBounds().minX(), patternSchematicWorld.getBounds().minY(), patternSchematicWorld.getBounds().minZ()));
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
    return applyRealToSourcePosition((T) currentContraption, patternSchematicWorld, position);
  }
  
  /**Mod behaves not as needed when below 0*/
  private int repeatingBounds(int source, int length) {
    return (length + (source % length)) % length;
  }
  
  public static class PistonContraptionTransform extends ContraptionSchematicTransform<PistonContraption> {
  
    public PistonContraptionTransform() {
      super(PistonContraption.class);
    }
  
    @Override
    public BlockPos modifyPos(PistonContraption currentContraption, BlockPos globalPos) {
      return globalPos.rotate(getRotationOfPistonContraption(currentContraption));
    }
  
    @Override
    public Vec3i getSourceLengths(PistonContraption currentContraption, PatternSchematicWorld patternSchematicWorld) {
      return Vec3iUtils.abs(new BlockPos(super.getSourceLengths(currentContraption, patternSchematicWorld))
          .rotate(getRotationOfPistonContraption(currentContraption)));
    }
    @Override
    public BlockState modifyState(PistonContraption currentContraption, BlockState blockState, BlockPos position, LevelAccessor level) {
      return super.modifyState(currentContraption, blockState, position, level)
          .rotate(level, position, getRotationOfPistonContraption(currentContraption));
    }
    
    public Rotation getRotationOfPistonContraption(PistonContraption contraption) {
      return getRotation(ReflectionUtils.getRestrictedField(contraption, "orientation", Direction.class));
    }
  
    protected static Rotation getRotation(Direction direction) {
      return Rotation.values()[direction.get2DDataValue()];
    }
  
  }
  
}
