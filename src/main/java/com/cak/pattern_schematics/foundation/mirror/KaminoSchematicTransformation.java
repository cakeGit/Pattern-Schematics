package com.cak.pattern_schematics.foundation.mirror;

import com.simibubi.create.content.schematics.client.SchematicTransformation;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Field;
import java.util.Arrays;

public class KaminoSchematicTransformation extends SchematicTransformation {
  
  public KaminoSchematicTransformation(SchematicTransformation transformation) {
    super();
    transferPrivateFieldValue("chasingPos", transformation, this);
    transferPrivateFieldValue("prevChasingPos", transformation, this);
    transferPrivateFieldValue("target", transformation, this);
    
    transferPrivateFieldValue("scaleFrontBack", transformation, this);
    transferPrivateFieldValue("scaleLeftRight", transformation, this);
  
    transferPrivateFieldValue("rotation", transformation, this);
  
    transferPrivateFieldValue("xOrigin", transformation, this);
    transferPrivateFieldValue("zOrigin", transformation, this);
  }
  
  private void transferPrivateFieldValue(String fieldName, SchematicTransformation transform1, KaminoSchematicTransformation transform2) {
    try {
      Field field1 = transform1.getClass().getDeclaredField(fieldName);
      field1.setAccessible(true);
  
      field1.set(transform2, field1.get(transform1));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  
  }
  
}
