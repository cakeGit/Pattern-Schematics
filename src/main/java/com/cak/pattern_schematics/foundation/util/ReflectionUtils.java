package com.cak.pattern_schematics.foundation.util;

import java.lang.reflect.Field;

public class ReflectionUtils {
  
  public static <T> T getPrivateField(Object instance, String name, Class<T> clazz) {
    try {
      Field field = instance.getClass().getField(name);
      field.setAccessible(true);
      return (T) field.get(field);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
  
}
