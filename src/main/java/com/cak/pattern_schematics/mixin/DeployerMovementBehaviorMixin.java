package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.foundation.mirror.PatternSchematicWorld;
import com.cak.pattern_schematics.foundation.util.ReflectionUtils;
import com.cak.pattern_schematics.registry.PatternSchematicsItems;
import com.simibubi.create.content.contraptions.TranslatingContraption;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.piston.PistonContraption;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import com.simibubi.create.content.kinetics.deployer.DeployerMovementBehaviour;
import com.simibubi.create.content.schematics.SchematicItem;
import com.simibubi.create.content.schematics.SchematicWorld;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DeployerMovementBehaviour.class, remap = false)
public class DeployerMovementBehaviorMixin {
  
  private static ItemStack currentThreadBlueprint;
  private static Direction currentThreadPlacementDirection;
  
  @Inject(method = "activateAsSchematicPrinter", at = @At("HEAD"))
  public void head_activateAsSchematicPrinter(MovementContext context, BlockPos pos, DeployerFakePlayer player, Level world, ItemStack filter, CallbackInfo ci) {
    currentThreadPlacementDirection = null;
    if (context.contraption instanceof TranslatingContraption translatingContraption) {
      currentThreadPlacementDirection = ReflectionUtils.getPrivateField(translatingContraption, "cachedColliderDirection", Direction.class);
    }
    if (currentThreadPlacementDirection.getAxis() == Direction.Axis.Y) {
      currentThreadPlacementDirection = null;
    }
  }
  
  @Redirect(method = "activate", at = @At(value = "INVOKE", target = "Lcom/tterrag/registrate/util/entry/ItemEntry;isIn(Lnet/minecraft/world/item/ItemStack;)Z"))
  public boolean isIn(ItemEntry<SchematicItem> instance, ItemStack stack) {
    currentThreadBlueprint = stack;
    return instance.isIn(stack) || PatternSchematicsItems.PATTERN_SCHEMATIC.isIn(stack);
  }
  
  @Redirect(method = "activateAsSchematicPrinter", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/BoundingBox;isInside(Lnet/minecraft/core/Vec3i;)Z"))
  public boolean isInside(BoundingBox instance, Vec3i vec3i) {
    if (PatternSchematicsItems.PATTERN_SCHEMATIC.isIn(currentThreadBlueprint)) {
      return true;
    }
    return instance.isInside(vec3i);
  }
  
  @Redirect(method = "activateAsSchematicPrinter", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/schematics/SchematicWorld;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
  public BlockState getBlockState(SchematicWorld instance, BlockPos globalPos) {
    return instance.getBlockState(modifyPos(globalPos, instance));
  }
  
  @Redirect(method = "activateAsSchematicPrinter", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/schematics/SchematicWorld;getBlockEntity(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;"))
  public BlockEntity getBlockEntity(SchematicWorld instance, BlockPos globalPos) {
    return instance.getBlockEntity(modifyPos(globalPos, instance));
  }
  
  private BlockPos modifyPos(BlockPos globalPos, SchematicWorld instance) {
    if (instance instanceof PatternSchematicWorld patternSchematicWorld) {
      return patternSchematicWorld.applyRealToSourceLoc(
          globalPos.subtract(patternSchematicWorld.anchor)
      ).offset(patternSchematicWorld.anchor);
    }
    return globalPos;
  }

}
