package com.traf;

import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Handle;

/**
 * this should send some 'flight' packet every ticket or something
 **/
public class Flight extends Hack {
    public Flight(String s){
        super(s);
    }

    public boolean isHoldingSpace(){
        return HandleKBMOutput.isSpaceHeld();
    }
    @Override
    public boolean run(LocalPlayer lp) {
        if (!this.isOn() || !isHoldingSpace()) {
            setCurrentTick(-1);
            return false;
        }

        double upwardSpeed = 0.15; // tweak
        int[] movement = HandleKBMOutput.getMovementHeld();
        if(movement.length!=4) return true;

        lp.setDeltaMovement(
                lp.getDeltaMovement().x,
                upwardSpeed + ( .05 *  getCurrentTick() ),
                lp.getDeltaMovement().z
        );
        incrementTick();
        return true;
    }
}
