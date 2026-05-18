package com.traf.hacks;

import com.traf.lifecycle.HandleKBMOutput;
import net.minecraft.client.player.LocalPlayer;

public class Speed extends Hack {
    private double speed = 1.1;
    public Speed(String s){
        super(s);
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if (!this.isOn()) {
            setCurrentTick(-1);
            return false;
        }

        int[] direction = HandleKBMOutput.getDirection();
        final int X = 0, Z = 1;
        if (direction[X] == 0 && direction[Z] == 0) {
            return true;
        }

        double delay  = 0.05;
        double maxGrowth = 3.0;
        double time   = this.getCurrentTick();
        double growth = Math.min(1 + (delay * time), maxGrowth); // include the max

        float yaw = (float) Math.toRadians(lp.getYRot());
        double forward = direction[X] * speed * growth;
        double strafe  = -direction[Z] * speed * growth;
        double motionX = strafe * Math.cos(yaw) - forward * Math.sin(yaw);
        double motionZ = forward * Math.cos(yaw) + strafe * Math.sin(yaw);

        lp.setDeltaMovement(
                motionX,
                lp.getDeltaMovement().y,
                motionZ
        );

        this.incrementTick();
        return true;
    }
}