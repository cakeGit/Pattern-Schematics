package com.cak.pattern_schematics.foundation.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class Vec3iUtils {
  
  public static void packVec3i(Vec3i vec3i, FriendlyByteBuf buf) {
    buf.writeVarInt(vec3i.getX());
    buf.writeVarInt(vec3i.getY());
    buf.writeVarInt(vec3i.getZ());
  }
  
  public static Vec3i unpackVec3i(FriendlyByteBuf buf) {
    return new Vec3i(buf.readVarInt(), buf.readVarInt(), buf.readVarInt());
  }
  
  public static void putVec3i(String name, Vec3i vec3i, CompoundTag tag) {
    tag.putInt(name + "_x", vec3i.getX());
    tag.putInt(name + "_y", vec3i.getY());
    tag.putInt(name + "_z", vec3i.getZ());
  }
  
  public static Vec3i getVec3i(String name, CompoundTag tag) {
    return new Vec3i(
        tag.getInt(name + "_x"),
        tag.getInt(name + "_y"),
        tag.getInt(name + "_z")
    );
  }
  
  public static Vec3i multiplyVec3i(Vec3i blockPos, Vec3i vec3i) {
    return new Vec3i(
        blockPos.getX() * vec3i.getX(),
        blockPos.getY() * vec3i.getY(),
        blockPos.getZ() * vec3i.getZ()
    );
  }
  
}
