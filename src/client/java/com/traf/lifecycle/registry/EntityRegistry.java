package com.traf.lifecycle.registry;

import com.traf.lifecycle.data.Colors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;

import java.awt.Color;

public final class EntityRegistry {
    private EntityRegistry() {}

    public static Color colorFor(Entity e) {
        if (e instanceof Player)       return Colors.PLAYER;
        if (e instanceof ItemEntity)   return Colors.ITEM;
        if (e instanceof Monster)      return Colors.MONSTER;
        if (e instanceof LivingEntity) return Colors.MOB;
        return null;
    }

    public static boolean shouldHighlight(Entity e) {
        return colorFor(e) != null;
    }
}