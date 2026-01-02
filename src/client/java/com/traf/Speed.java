package com.traf;

import net.minecraft.client.player.LocalPlayer;
import org.objectweb.asm.Handle;

public class Speed extends Hack {

    @Override
    public void run(LocalPlayer lp) {
        if (!this.isOn()) {
            setCurrentTick(-1);
            return;
        }

        int[] direction = HandleMouseOutput.getDirection();
        final int X = 0, Z = 1;
        if (direction[X] == 0 && direction[Z] == 0) {
            return;
        }

        double speed = 0.5;

        float yaw = (float) Math.toRadians(lp.getYRot());
        double forward = direction[X] * speed;
        double strafe = -direction[Z] * speed;
        double motionX = strafe * Math.cos(yaw) - forward * Math.sin(yaw);
        double motionZ = forward * Math.cos(yaw) + strafe * Math.sin(yaw);

        lp.setDeltaMovement(
                motionX,
                lp.getDeltaMovement().y,
                motionZ
        );

        incrementTick();
    }
}