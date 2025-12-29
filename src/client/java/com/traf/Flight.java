package com.traf;

import net.minecraft.client.player.LocalPlayer;

/**
 * this should send some flight packet every ticket or something
 **/
public class Flight extends Hack {
    private int currentTick = 0;
    public boolean isHoldingSpace(){
        return HandleMouseOutput.isSpaceHeld();
    }
    @Override
    public void run(LocalPlayer lp) {
        if (!this.isOn() || !isHoldingSpace()) {
            currentTick = -1;
            return;
        }

        double upwardSpeed = 0.15; // tweak
        lp.setDeltaMovement(
                lp.getDeltaMovement().x,
                upwardSpeed + (.05*currentTick ),
                lp.getDeltaMovement().z
        );
        currentTick++;
    }
}
