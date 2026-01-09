package com.traf.hacks;

import com.traf.hacks.sub.HitHack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.ArrayList;
import java.util.List;

public class AutoHeal extends Hack {
    private int lastRan = -1;
    private final int delay = 15;
    public enum HealType{
        HEALING,
        SOUP
    };

    public AutoHeal(String s) {
        super(s);
        setOn(true);
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if(lp == null) return false;

        Inventory inv = lp.getInventory();
        ItemStack stack;
        PotionContents potionContents;

        int itemInHotbar = findHealItem(inv, 0, 9);

        if(itemInHotbar != -1) {
            List<ItemSlot> slots = new ArrayList<>();
            stack = inv.getItem(itemInHotbar);

            if(stack.is(Items.MUSHROOM_STEW)){
                slots.add(new ItemSlot(itemInHotbar, HealType.SOUP));
            } else {
                potionContents = stack.get(DataComponents.POTION_CONTENTS);
                if(potionContents != null && stack.is(Items.SPLASH_POTION)) {
                    if(potionContents.potion().isPresent() && potionContents.potion().get() == Potions.HEALING) {
                        slots.add(new ItemSlot(itemInHotbar, HealType.HEALING));
                    }
                }
            }

            runHeal(lp, slots);
            incrementTick();
            lastRan = getCurrentTick();
            return true;
        }

        int itemInInventory = findHealItem(inv, 9, 36);

        if(itemInInventory != -1) {
            Minecraft.getInstance().gameMode.handleInventoryMouseClick(0, itemInInventory, 0, ClickType.QUICK_MOVE, lp);
        }

        incrementTick();
        lastRan = getCurrentTick();
        return true;
    }

    private int findHealItem(Inventory inv, int startSlot, int endSlot) {
        ItemStack stack;
        PotionContents potionContents;

        for(int i = startSlot; i < endSlot; i++) {
            stack = inv.getItem(i);
            if(stack.isEmpty()) continue;

            if(stack.is(Items.MUSHROOM_STEW)){
                return i;
            }

            potionContents = stack.get(DataComponents.POTION_CONTENTS);
            if(potionContents != null && stack.is(Items.SPLASH_POTION)) {
                if(potionContents.potion().isPresent() && potionContents.potion().get() == Potions.HEALING) {
                    return i;
                }
            }
        }

        return -1;
    }

    private void useItem(LocalPlayer lp, HealType type) {
        Minecraft mc = Minecraft.getInstance();
        if(!(lp.getHealth() < lp.getMaxHealth() * 0.75)) return;
        if(type == HealType.HEALING) {
            float oldPitch = lp.getXRot();
            lp.setXRot(85);
            mc.gameMode.useItem(lp, InteractionHand.MAIN_HAND);
            lp.setXRot(oldPitch);
        }
        else if(type == HealType.SOUP) {
            mc.gameMode.useItem(lp, InteractionHand.MAIN_HAND);
        }
    }

    private void runHeal(LocalPlayer lp, List<ItemSlot> stacks) {
        if(stacks.isEmpty()) return;

        for(ItemSlot itemSlot : stacks) {
            if(itemSlot.slot >= 0 && itemSlot.slot <= 8) {
                lp.getInventory().setSelectedSlot(itemSlot.slot);
                useItem(lp, itemSlot.type);
                return;
            }
        }
    }

    private void printStack(List<ItemSlot> stacks) {
        for(ItemSlot e : stacks){
            System.out.println(e.toString());
        }
    }

    private static class ItemSlot {
        private int slot;
        private AutoHeal.HealType type;

        public ItemSlot(int slot, AutoHeal.HealType type){
            this.slot = slot;
            this.type = type;
        }

        ItemSlot getItemSlot(){ return this; }

        @Override
        public String toString(){
            return "Slot: "+slot+"\nHealType: "+ (type==HealType.HEALING ? "HEAL POT" : "SOUP");
        }
    }
}