package com.cak.pattern_schematics;

import com.cak.pattern_schematics.registry.PatternSchematicPackets;
import com.cak.pattern_schematics.registry.PatternSchematicsItems;
import com.cak.pattern_schematics.registry.PatternSchematicsLang;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PatternSchematics.MODID)
public class PatternSchematics {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "create_pattern_schematics";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    
    public PatternSchematics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);
        PatternSchematicsItems.register();
        PatternSchematicPackets.registerPackets();
        PatternSchematicsLang.register();
    }
    
    public static ResourceLocation asResource(String loc) {
        return new ResourceLocation(MODID, loc);
    }
    
}
