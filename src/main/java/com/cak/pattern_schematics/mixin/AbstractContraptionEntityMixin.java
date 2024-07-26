package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.foundation.schematic_transforms.ContraptionSchematicTransform;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**Get rid of the entry in the {@link ContraptionSchematicTransform#cachedHandlers}*/
@Mixin(value = AbstractContraptionEntity.class, remap = false)
public class AbstractContraptionEntityMixin {
  
  @Shadow protected Contraption contraption;
  
  @Inject(method = "remove", at = @At("HEAD"))
  private void remove(Entity.RemovalReason p_146834_, CallbackInfo ci) {
    ContraptionSchematicTransform.cachedHandlers.remove(contraption);
  }
  
}
