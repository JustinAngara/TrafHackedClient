package com.traf;

import net.minecraft.client.player.LocalPlayer;

/**
 * this should send some flight packet every ticket or something
 **/
public class Flight extends Hack {

    @Override
    public void run(LocalPlayer lp) {
        if (!this.isOn()) return;

        double upwardSpeed = 0.08; // tweak

        lp.setDeltaMovement(
                lp.getDeltaMovement().x,
                upwardSpeed,
                lp.getDeltaMovement().z
        );
    }
}
