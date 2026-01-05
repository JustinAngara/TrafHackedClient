package com.traf.hacks;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

/**
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 15 | item.minecraft.splash_potion x1
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 16 | item.minecraft.splash_potion x1
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 17 | item.minecraft.splash_potion x1
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 18 | item.minecraft.splash_potion x1
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 19 | item.minecraft.splash_potion x1
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 20 | item.minecraft.splash_potion x1
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 21 | item.minecraft.splash_potion x1
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 22 | item.minecraft.splash_potion x1
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 23 | item.minecraft.splash_potion x1
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 24 | item.minecraft.splash_potion x1
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 0 | item.minecraft.cooked_beef x63
 * [23:22:43] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 9 | item.minecraft.splash_potion x1
 *
 * */
public class AutoHeal extends Hack {

    public AutoHeal(String s) {
        super(s);
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if(lp == null) return false;


        // get inventory to see where the pot is
        Inventory inv = lp.getInventory();
        ItemStack stack;
        PotionContents contents;


        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            stack = inv.getItem(slot);
            if (stack.isEmpty()) continue;

            System.out.println(
                    "Slot " + slot +
                            " | " + stack.getItem().getDescriptionId() +
                            " x" + stack.getCount()
            );

            contents = stack.get(DataComponents.POTION_CONTENTS);
            if (stack.is(Items.SPLASH_POTION) && contents != null) {
                if (contents.potion().isPresent() && contents.potion().get() == Potions.HEALING) {
                    System.out.println("this is a splash potion of healing");

                }
            }
        }

        return false;
    }
}
