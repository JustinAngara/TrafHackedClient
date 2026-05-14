package com.traf.lifecycle.registry;

import com.traf.lifecycle.data.Colors;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.Color;
import java.util.Map;

/**
 * ore -> color lookup should go through here
 */
public final class OreRegistry {
    private OreRegistry() {}

    public static final Map<Block, Color> COLORS = Map.ofEntries(
            Map.entry(Blocks.DIAMOND_ORE,            Colors.DIAMOND),
            Map.entry(Blocks.DEEPSLATE_DIAMOND_ORE,  Colors.DIAMOND),

            Map.entry(Blocks.GOLD_ORE,               Colors.GOLD),
            Map.entry(Blocks.DEEPSLATE_GOLD_ORE,     Colors.GOLD),
            Map.entry(Blocks.NETHER_GOLD_ORE,        Colors.GOLD),

            Map.entry(Blocks.IRON_ORE,               Colors.IRON),
            Map.entry(Blocks.DEEPSLATE_IRON_ORE,     Colors.IRON),

            Map.entry(Blocks.EMERALD_ORE,            Colors.EMERALD),
            Map.entry(Blocks.DEEPSLATE_EMERALD_ORE,  Colors.EMERALD),

            Map.entry(Blocks.REDSTONE_ORE,           Colors.REDSTONE),
            Map.entry(Blocks.DEEPSLATE_REDSTONE_ORE, Colors.REDSTONE),

            Map.entry(Blocks.LAPIS_ORE,              Colors.LAPIS),
            Map.entry(Blocks.DEEPSLATE_LAPIS_ORE,    Colors.LAPIS),

            Map.entry(Blocks.COAL_ORE,               Colors.COAL),
            Map.entry(Blocks.DEEPSLATE_COAL_ORE,     Colors.COAL),

            Map.entry(Blocks.COPPER_ORE,             Colors.COPPER),
            Map.entry(Blocks.DEEPSLATE_COPPER_ORE,   Colors.COPPER),

            Map.entry(Blocks.ANCIENT_DEBRIS,         Colors.DEBRIS),
            Map.entry(Blocks.NETHER_QUARTZ_ORE,      Colors.QUARTZ),

            Map.entry(Blocks.CHEST,                  Colors.CHEST),
            Map.entry(Blocks.HOPPER,                 Colors.CHEST),
            Map.entry(Blocks.ENDER_CHEST,            Colors.CHEST)
    );

    // callers that already have a BlockState, returns null if not registered
    public static Color colorFor(BlockState state) {
        return COLORS.get(state.getBlock());
    }
}