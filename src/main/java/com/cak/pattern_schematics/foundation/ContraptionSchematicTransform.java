package com.cak.pattern_schematics.foundation;

import com.cak.pattern_schematics.foundation.util.ReflectionUtils;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.piston.PistonContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContraptionSchematicTransform<T extends Contraption> {
  
  public static class Handlers {
    
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
      return null;
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
  
  @SuppressWarnings("unchecked cast") //Type is already checked
  public BlockPos castModifyPos(Contraption currentContraption, BlockPos globalPos) {
    return modifyPos((T) currentContraption, globalPos);
  }
  
  @SuppressWarnings("unchecked cast") //Type is already checked
  public BlockState castModifyState(Contraption currentContraption, BlockState blockState, BlockPos pos, LevelAccessor level) {
    return modifyState((T) currentContraption, blockState, pos, level);
  }
  
  public BlockPos modifyPos(T currentContraption, BlockPos globalPos) {
    return globalPos;
  }
  
  public BlockState modifyState(T currentContraption, BlockState blockState, BlockPos pos, LevelAccessor level) {
    return blockState;
  }
  
  protected static BlockState rotate(Direction direction, BlockState blockState, BlockPos pos, LevelAccessor level) {
    Rotation rotation = Rotation.values()[direction.get2DDataValue()];
    return blockState.rotate(level, pos, rotation);
  }
  
  public static class PistonContraptionTransform extends ContraptionSchematicTransform<PistonContraption> {
  
    public PistonContraptionTransform() {
      super(PistonContraption.class);
    }
  
    @Override
    public BlockState modifyState(PistonContraption currentContraption, BlockState blockState, BlockPos position, LevelAccessor level) {
      return rotate(
          ReflectionUtils.getPrivateField(currentContraption, "orientation", Direction.class),
          super.modifyState(currentContraption, blockState, position, level),
          position, level
      );
    }
  
  }
  
}
