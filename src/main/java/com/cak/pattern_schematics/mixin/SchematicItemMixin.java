package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.PatternSchematicsClient;
import com.cak.pattern_schematics.content.PatternSchematicItem;
import com.cak.pattern_schematics.foundation.mirror.PatternSchematicHandler;
import com.cak.pattern_schematics.foundation.mirror.PatternSchematicsToolType;
import com.cak.pattern_schematics.foundation.mirror.SimpleSchematicOutlineRenderer;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.schematics.SchematicItem;
import com.simibubi.create.content.schematics.client.SchematicHandler;
import com.simibubi.create.content.schematics.client.tools.SchematicToolBase;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SchematicItem.class, remap = false)
public class SchematicItemMixin {
  
  @Inject(method = "onItemUse", at = @At(value = "HEAD"), cancellable = true)
  public void onItemUse(Player player, InteractionHand hand, CallbackInfoReturnable<Boolean> cir) {
    if (player.getItemInHand(hand).getItem() instanceof PatternSchematicItem)
      cir.setReturnValue(false);
  }
  
}
