package com.traf.hacks;

import net.minecraft.client.player.LocalPlayer;

public class AntiHunger extends Hack {

    private static volatile boolean active = false;

    public AntiHunger(String s) {
        super(s);
        this.setOn(true);
    }

    @Override
    public boolean run(LocalPlayer lp) {
        boolean nowActive = this.isOn() && lp != null;
        if (nowActive != active) {
            active = nowActive;
        }
        return nowActive;
    }

    public static boolean isActive() {
        return active;
    }
}