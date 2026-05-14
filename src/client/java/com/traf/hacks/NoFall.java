package com.traf.hacks;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class NoFall extends Hack {

    public NoFall(String s) {
        super(s);
        this.setOn(true);
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if (lp == null || lp.connection == null) {
            return false;
        }

        if (lp.fallDistance > 2.5F) {
            lp.connection.send(new ServerboundMovePlayerPacket.StatusOnly(true, lp.horizontalCollision));
            lp.fallDistance = 0.0F;

            return true;
        }
        return false;
    }
}