package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.foundation.ContraptionSchematicTransform;
import com.cak.pattern_schematics.foundation.mirror.PatternSchematicWorld;
import com.cak.pattern_schematics.registry.PatternSchematicsItems;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import com.simibubi.create.content.kinetics.deployer.DeployerMovementBehaviour;
import com.simibubi.create.content.schematics.SchematicItem;
import com.simibubi.create.content.schematics.SchematicWorld;
import com.simibubi.create.foundation.utility.Color;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DeployerMovementBehaviour.class, remap = false)
public class DeployerMovementBehaviorMixin {

  private ItemStack currentBlueprint;
  private Contraption currentContraption;
  private ContraptionSchematicTransform<?> currentContraptionSchematicTransform;
  private Level currentLevel;
  private BlockPos currentBlockPos;
  
  
  @Inject(method = "activateAsSchematicPrinter", at = @At("HEAD"))
  public void head_activateAsSchematicPrinter(MovementContext context, BlockPos blockPos, DeployerFakePlayer player, Level world, ItemStack filter, CallbackInfo ci) {
    currentContraption = context.contraption;
    currentBlockPos = blockPos;
    currentLevel = world;
  }
  
  @Redirect(method = "activate", at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/ItemEntry;isIn(Lnet/minecraft/world/item/ItemStack;)Z"))
  public boolean isIn(ItemEntry<SchematicItem> instance, ItemStack stack) {
    currentBlueprint = stack;
    return instance.isIn(stack) || PatternSchematicsItems.PATTERN_SCHEMATIC.isIn(stack);
  }
  
  @Redirect(method = "activateAsSchematicPrinter", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/BoundingBox;isInside(Lnet/minecraft/core/Vec3i;)Z"))
  public boolean isInside(BoundingBox instance, Vec3i vec3i) {
    if (PatternSchematicsItems.PATTERN_SCHEMATIC.isIn(currentBlueprint)) {
      return true;
    }
    return instance.isInside(vec3i);
  }
  
  @Redirect(method = "activateAsSchematicPrinter", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/schematics/SchematicWorld;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
  public BlockState getBlockState(SchematicWorld instance, BlockPos globalPos) {
    return transformBlock(instance.getBlockState(modifyPos(globalPos, instance)));
  }
  
  @Redirect(method = "activateAsSchematicPrinter", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/schematics/SchematicWorld;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"))
  public BlockEntity getBlockEntity(SchematicWorld instance, BlockPos globalPos) {
    BlockEntity blockEntity = instance.getBlockEntity(modifyPos(globalPos, instance));
    if (blockEntity != null)
      blockEntity.setBlockState(transformBlock(blockEntity.getBlockState()));
    return blockEntity;
  }
  
  private BlockState transformBlock(BlockState blockState) {
    //System.out.println(blockState.getBlock());
    if (blockState == null)
      return null;
    currentContraptionSchematicTransform = ContraptionSchematicTransform.Handlers.get(currentContraption);
    if (currentContraptionSchematicTransform != null) {
      blockState = currentContraptionSchematicTransform.castModifyState(currentContraption, blockState, currentBlockPos, currentLevel);
    }
    return blockState;
  }
  
  private BlockPos modifyPos(BlockPos globalPos, SchematicWorld instance) {
    if (instance instanceof PatternSchematicWorld patternSchematicWorld) {
      
      currentContraptionSchematicTransform = ContraptionSchematicTransform.Handlers.get(currentContraption);
      
      globalPos = currentContraptionSchematicTransform.castModifyPos(
          currentContraption,
          globalPos.subtract(currentContraption.anchor)
      ).offset(currentContraption.anchor);
  
      System.out.println(globalPos);
      
      return currentContraptionSchematicTransform.castApplyRealToSourcePosition(
          currentContraption, patternSchematicWorld, globalPos
      );
      
    }
    return globalPos;
  }

}
