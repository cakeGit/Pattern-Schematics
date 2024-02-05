package com.cak.pattern_schematics.foundation.util;

import java.lang.reflect.Field;

public class ReflectionUtils {
  
  @SuppressWarnings("unchecked cast")
  public static <T> T getPrivateField(Object instance, String name, Class<T> clazz) {
    try {
      Field field = instance.getClass().getDeclaredField(name);
      field.setAccessible(true);
      if (field.getType() != clazz)
        throw new RuntimeException("Incorrect field type for getPrivateField");
      return (T) field.get(instance);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
  
}
