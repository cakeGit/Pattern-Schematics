package com.cak.pattern_schematics;

import com.cak.pattern_schematics.content.PatternSchematicsItem;
import com.mojang.logging.LogUtils;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.Create;
import com.tterrag.registrate.util.entry.ItemEntry;
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
    
    public static final ItemEntry<Item> EMPTY_PATTERN_SCHEMATIC = Create.REGISTRATE
        .item("empty_pattern_schematic", Item::new)
        .properties(p -> p.stacksTo(1))
        .tab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
        .register();
    
    public static final ItemEntry<PatternSchematicsItem> PATTERN_SCHEMATIC = Create.REGISTRATE
        .item("pattern_schematic", PatternSchematicsItem::new)
        .properties(p -> p.stacksTo(1))
        //.tab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
        .register();
    
    public PatternSchematics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    }
    
}
