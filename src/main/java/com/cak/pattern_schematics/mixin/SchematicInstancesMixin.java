package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.content.PatternSchematicItem;
import com.cak.pattern_schematics.foundation.mirror.PatternSchematicWorld;
import com.simibubi.create.content.schematics.SchematicInstances;
import com.simibubi.create.content.schematics.SchematicWorld;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SchematicInstances.class, remap = false)
public class SchematicInstancesMixin {
  
  private static ItemStack lastThreadStack = null;
  
  @Inject(method = "loadWorld", at = @At(value = "HEAD"))
  private static void loadWorld(Level wrapped, ItemStack schematic, CallbackInfoReturnable<SchematicWorld> cir) {
    lastThreadStack = schematic;
  }
  
  @ModifyVariable(method = "loadWorld", at = @At("STORE"), ordinal = 0)
  private static SchematicWorld loadWorld(SchematicWorld value) {
    if (lastThreadStack.getItem() instanceof PatternSchematicItem) {
      System.out.println("mixin in");
      return new PatternSchematicWorld(value.anchor, value.getLevel());
    }
    return value;
  }
  
  
}
