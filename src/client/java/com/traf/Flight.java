package com.traf;

import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Handle;

/**
 * this should send some 'flight' packet every ticket or something
 **/
public class Flight extends Hack {
    public boolean isHoldingSpace(){
        return HandleMouseOutput.isSpaceHeld();
    }
    @Override
    public void run(LocalPlayer lp) {
        if (!this.isOn() || !isHoldingSpace()) {
            setCurrentTick(-1);
            return;
        }

        double upwardSpeed = 0.15; // tweak
        int[] movement = HandleMouseOutput.getMovementHeld();
        if(movement.length!=4) return;

        lp.setDeltaMovement(
                lp.getDeltaMovement().x,
                upwardSpeed + ( .05 *  getCurrentTick() ),
                lp.getDeltaMovement().z
        );
        incrementTick();
    }
}
