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

        double speed = 0.2; // tweak
        lp.setDeltaMovement(
                lp.getDeltaMovement().x + (direction[0]),
                lp.getDeltaMovement().y,
                lp.getDeltaMovement().z + (direction[1])
        );
        incrementTick();
    }
}
