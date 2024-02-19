package com.cak.pattern_schematics.mixin;

import com.cak.pattern_schematics.foundation.mirror.PatternSchematicWorld;
import com.cak.pattern_schematics.foundation.util.ReflectionUtils;
import com.simibubi.create.content.schematics.SchematicPrinter;
import com.simibubi.create.content.schematics.SchematicWorld;
import com.simibubi.create.content.schematics.packet.SchematicPlacePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.network.NetworkEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;

@Mixin(value = SchematicPlacePacket.class, remap = false)
public class SchematicPlacePacketMixin {
  
  @Shadow public ItemStack stack;
  
//  @Redirect(method = "handle", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/network/NetworkEvent$Context;enqueueWork(Ljava/lang/Runnable;)Ljava/util/concurrent/CompletableFuture;"))
//  private CompletableFuture<Void> handle(NetworkEvent.Context context, Runnable runnable) {
//    return context.enqueueWork(() -> {
//      runnable.run();
//
//      ServerPlayer player = context.getSender();
//      if (player == null)
//        return;
//      if (!player.isCreative())
//        return;
//
//      Level world = player.level();
//      SchematicPrinter printer = new SchematicPrinter();
//      printer.loadSchematic(stack, world, !player.canUseGameMasterBlocks());
//      if (!printer.isLoaded() || printer.isErrored())
//        return;
//
//      SchematicWorld schematicWorld = ReflectionUtils.getPrivateField(printer, "blockReader", SchematicWorld.class);
//
//      if (schematicWorld instanceof PatternSchematicWorld) {
//        BoundingBox box = schematicWorld.getBounds();
//        BlockPos anchor = schematicWorld.anchor;
//        for (int x = box.minX(); x <= box.maxX(); x++) {
//          for (int y = box.minX(); y <= box.maxX(); y++) {
//            for (int z = box.minX(); z <= box.maxX(); z++) {
//              BlockPos pos = anchor.offset(x, y, z);
//              BlockState state = world.getBlockState(pos);
////              for (Direction direction : Direction.values()) {
////                world.setBlock(pos, state.updateShape(direction, state, world, pos, pos.relative(direction)), 18);
////              }
//              world.updateNeighborsAt(pos, null);
//            }
//          }
//        }
//      }
//    });
//  }
  
}
