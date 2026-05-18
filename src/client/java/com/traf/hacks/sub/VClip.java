package com.traf.hacks.sub;

import net.minecraft.client.player.LocalPlayer;

public class VClip extends SubHack {

    private double distance = 10.0;

    public VClip(String s) {
        super(s);
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if (lp == null) return false;

        lp.setDeltaMovement(
                lp.getDeltaMovement().x,
                distance,
                lp.getDeltaMovement().z
        );

        return true;
    }

    public void setDistance(double d) { this.distance = d; }
    public double getDistance() { return distance; }
}