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
        double speed = 0.05;

        float yaw = (float) Math.toRadians(lp.getYRot());

        // direction[0] is strafe (left/right), direction[1] is forward/back
        double forward = direction[1] * speed;
        double strafe = direction[0] * speed;

        // convert to x/z based on player rotation
        double motionX = strafe * Math.cos(yaw) - forward * Math.sin(yaw);
        double motionZ = forward * Math.cos(yaw) + strafe * Math.sin(yaw);

        lp.setDeltaMovement(
                lp.getDeltaMovement().x + motionX,
                lp.getDeltaMovement().y,
                lp.getDeltaMovement().z + motionZ
        );
        incrementTick();
    }
}