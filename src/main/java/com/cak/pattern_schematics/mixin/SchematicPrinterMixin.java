package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.content.PatternSchematicItem;
import com.cak.pattern_schematics.foundation.mirror.PatternSchematicWorld;
import com.simibubi.create.content.schematics.SchematicPrinter;
import com.simibubi.create.content.schematics.SchematicWorld;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SchematicPrinter.class, remap = false)
public class SchematicPrinterMixin {
  
  private static ItemStack lastThreadStack = null;
  private static StructureTemplate lastThreadStructureTemplate = null;
  
  @Shadow
  private SchematicWorld blockReader;
  
  @Inject(method = "loadSchematic", at = @At(value = "HEAD"))
  private void loadSchematic_head(ItemStack blueprint, Level originalWorld, boolean processNBT, CallbackInfo ci) {
    lastThreadStack = blueprint;
  }
  
  @ModifyVariable(method = "loadSchematic", ordinal = 0, at = @At(value = "STORE"))
  private StructureTemplate store_activeTemplate(StructureTemplate template) {
    lastThreadStructureTemplate = template;
    return template;
  }
  
  @Inject(method = "loadSchematic", at = @At(value = "FIELD", shift = At.Shift.AFTER, opcode = Opcodes.PUTFIELD, target = "Lcom/simibubi/create/content/schematics/SchematicPrinter;blockReader:Lcom/simibubi/create/content/schematics/SchematicWorld;"))
  private void loadSchematic(ItemStack blueprint, Level originalWorld, boolean processNBT, CallbackInfo ci) {
    if (lastThreadStack.getItem() instanceof PatternSchematicItem) {
      PatternSchematicWorld patternSchematicWorld = new PatternSchematicWorld(blockReader.anchor, blockReader.getLevel());
      patternSchematicWorld.putExtraData(blueprint, lastThreadStructureTemplate);
      blockReader = patternSchematicWorld;
    }
  }
  
  
}
