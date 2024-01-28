package com.cak.pattern_schematics;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.contraptions.elevator.ElevatorControlsHandler;
import com.simibubi.create.content.trains.TrainHUD;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class PatternSchematicsClientEvents {
  
  @SubscribeEvent
  public static void onTick(TickEvent.ClientTickEvent event) {
    if (Minecraft.getInstance().level == null || Minecraft.getInstance().player == null)
      return;
    PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER.tick();
  }
  
  @SubscribeEvent
  public static void onRenderWorld(RenderLevelStageEvent event) {
    PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER.render(
        event.getPoseStack(), SuperRenderTypeBuffer.getInstance(),
        Minecraft.getInstance().gameRenderer.getMainCamera().getPosition()
    );
  }
  
  @SubscribeEvent
  public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
    event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "pattern_schematic", PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER);
  }
  
  @SubscribeEvent
  public static void onKeyInput(InputEvent.Key event) {
    boolean pressed = !(event.getAction() == 0);
    PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER.onKeyInput(event.getKey(), pressed);
  }
  
  @SubscribeEvent
  public static void onMouseScrolled(InputEvent.MouseScrollingEvent event) {
    if (Minecraft.getInstance().screen != null)
      return;
    
    double delta = event.getScrollDelta();
    boolean cancelled = PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER.mouseScrolled(delta);
    event.setCanceled(cancelled);
  }
  
  @SubscribeEvent
  public static void onMouseInput(InputEvent.MouseButton.Pre event) {
    if (Minecraft.getInstance().screen != null)
      return;
    
    int button = event.getButton();
    boolean pressed = !(event.getAction() == 0);
    
    if (PatternSchematicsClient.PATTERN_SCHEMATIC_HANDLER.onMouseInput(button, pressed))
      event.setCanceled(true);
  }
  
}
