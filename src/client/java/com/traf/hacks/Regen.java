package com.traf.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class Regen extends Hack {
    private final Minecraft mc;
    private int packetsPerTick = 20;

    public Regen(String s) {
        super(s);
        this.mc = Minecraft.getInstance();
        this.setOn(true);
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if (!this.isOn() || lp == null) return false;
        if (mc.getConnection() == null) return false;

        // only bother if regen is actually possible
        if (lp.getHealth() >= lp.getMaxHealth()) return true;
        if (lp.getFoodData().getFoodLevel() < 18) return true;

        // spam status-only move packets so the server thinks many ticks
        // have passed without us moving -> food-based regen ticks faster
        for (int i = 0; i < packetsPerTick; i++) {
            mc.getConnection().send(
                    new ServerboundMovePlayerPacket.StatusOnly(
                            lp.onGround(),
                            lp.horizontalCollision
                    )
            );
        }

        return true;
    }

    public void setPacketsPerTick(int n) {
        this.packetsPerTick = Math.max(1, n);
    }

    public int getPacketsPerTick() {
        return packetsPerTick;
    }
}