package com.traf.hacks;

import com.traf.hacks.sub.HitHack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.List;

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
 *
 * [18:55:42] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 0 | item.minecraft.cooked_beef x62
 * [18:55:42] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 1 | item.minecraft.bowl x1
 * [18:55:42] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 0 | item.minecraft.cooked_beef x62
 * [18:55:42] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 1 | item.minecraft.bowl x1
 * [18:55:42] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 1 | item.minecraft.bowl x1
 * [18:55:42] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 1 | item.minecraft.bowl x1
 * [18:55:46] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 1 | item.minecraft.mushroom_stew x1
 * [18:55:46] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 2 | item.minecraft.mushroom_stew x1
 * [18:55:46] [Render thread/INFO] (Minecraft) [STDOUT]: Slot 3 | item.minecraft.mushroom_stew x1
 *
 * */
public class AutoHeal extends Hack {
    public enum HealType{     // what type of it is it
        HEALING,
        SOUP
    };

    public AutoHeal(String s) {
        super(s);
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if(lp == null) return false;

        // get inventory to see where the pot is
        Inventory inv = lp.getInventory();
        ItemStack stack;
        PotionContents potionContents;

        List<ItemSlot> slots = new ArrayList<>();

        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            stack = inv.getItem(slot);
            if (stack.isEmpty()) continue;

            System.out.println(
                    "Slot " + slot +
                            " | " + stack.getItem().getDescriptionId() +
                            " x" + stack.getCount()
            );


            if(stack.is(Items.MUSHROOM_STEW)){
                slots.add(new ItemSlot(slot, HealType.SOUP);
                continue;
            }

            potionContents = stack.get(DataComponents.POTION_CONTENTS);
            if( potionContents != null || !stack.is(Items.SPLASH_POTION)) continue;

            if (potionContents.potion().isPresent() && potionContents.potion().get() == Potions.HEALING) {
                slots.add(new ItemSlot(slot, HealType.HEALING));
            }


        }

        return false;
    }


    private static class ItemSlot {

        private ItemSlot item; // return an item reference to itself (quantity is always one)
        private int slot;      // where its located
        private AutoHeal.HealType type;

        public ItemSlot(int slot, AutoHeal.HealType type){
            this.slot = slot;
            this.type = type;
        }

        ItemSlot getItemSlot(){ return this; }
    }
}
