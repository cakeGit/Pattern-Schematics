package com.cak.pattern_schematics;

import com.cak.pattern_schematics.content.PatternSchematicPackets;
import com.cak.pattern_schematics.content.PatternSchematicsItem;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PatternSchematics.MODID)
public class PatternSchematics
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "create_pattern_schematics";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    
    public static final ItemEntry<Item> EMPTY_PATTERN_SCHEMATIC = REGISTRATE
        .item("empty_pattern_schematic", Item::new)
        .defaultModel()
        .properties(p -> p.stacksTo(1))
        .tab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
        .register();
    
    public static final ItemEntry<PatternSchematicsItem> PATTERN_SCHEMATIC = REGISTRATE
        .item("pattern_schematic", PatternSchematicsItem::new)
        .defaultModel()
        .properties(p -> p.stacksTo(1))
        //.tab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
        .register();
    
    public PatternSchematics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);
        PatternSchematicPackets.registerPackets();
        PatternSchematicsLang.register();
    }
    
    public static ResourceLocation asResource(String loc) {
        return new ResourceLocation(MODID, loc);
    }
    
}
