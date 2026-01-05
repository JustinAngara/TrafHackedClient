package com.traf.hacks;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class AutoHeal extends Hack {

    public AutoHeal(String s) {
        super(s);
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if(lp == null) return false;


        // get inventory to see where the pot is
        Inventory inv = lp.getInventory();

        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            ItemStack stack = inv.getItem(slot);
            if (stack.isEmpty()) continue;

            System.out.println(
                    "Slot " + slot +
                            " | " + stack.getItem().getDescriptionId() +
                            " x" + stack.getCount()
            );
        }
        return false;
    }
}
