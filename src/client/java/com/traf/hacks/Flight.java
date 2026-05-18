package com.traf.hacks;

import com.traf.lifecycle.HandleKBMOutput;
import net.minecraft.client.player.LocalPlayer;

/**
 * this should send some 'flight' packet every tick or something
 **/
public class Flight extends Hack {

    double upwardSpeed = 0.15;

    public Flight(String s){
        super(s);
    }
    enum Movement {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    };

    public boolean isHoldingSpace(){
        return HandleKBMOutput.isSpaceHeld();
    }
    @Override
    public boolean run(LocalPlayer lp) {
        if (!this.isOn() || !isHoldingSpace()) {
            setCurrentTick(-1);
            return false;
        }

        double delay = .05;
        int time = getCurrentTick();
        int[] movement = HandleKBMOutput.getMovementHeld(); // returns the movement keys pressed represented in int
        if(movement.length!=4) return true;

        lp.setDeltaMovement(
                lp.getDeltaMovement().x,
                upwardSpeed + ( delay * time ),
                lp.getDeltaMovement().z
        );

        this.incrementTick();
        return true;
    }
}
