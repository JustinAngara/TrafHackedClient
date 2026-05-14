package com.traf.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class FullBright extends Hack {
    private final Minecraft mc;
    private Double originalGamma;
    private final double brightGamma = 100.0;

    public FullBright(String s) {
        super(s);
        this.mc = Minecraft.getInstance();
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if (!this.isOn()) {
            // restore original gamma if we previously overrode it
            if (originalGamma != null) {
                mc.options.gamma().set(originalGamma);
                originalGamma = null;
            }
            return false;
        }

        // stash the original on first activation
        if (originalGamma == null) {
            originalGamma = mc.options.gamma().get();
        }

        // force gamma to "bright" if it isn't already
        if (mc.options.gamma().get() != brightGamma) {
            mc.options.gamma().set(brightGamma);
        }

        return true;
    }
}