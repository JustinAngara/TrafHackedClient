package com.traf.hacks;

import com.traf.lifecycle.HandleKBMOutput;
import net.minecraft.client.player.LocalPlayer;

public class Speed extends Hack {
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

        double speed = 0.5;

        float yaw = (float) Math.toRadians(lp.getYRot());
        double forward = direction[X] * speed;
        double strafe = -direction[Z] * speed;
        double motionX = strafe * Math.cos(yaw) - forward * Math.sin(yaw);
        double motionZ = forward * Math.cos(yaw) + strafe * Math.sin(yaw);

        lp.setDeltaMovement(
                motionX,
                lp.getDeltaMovement().y + 10,
                motionZ
        );

        incrementTick();
        return true;
    }
}