package com.traf.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.player.LocalPlayer;

import java.lang.reflect.Field;

public class FullBright extends Hack {
    private final Minecraft mc;
    private Double originalGamma;
    private final double brightGamma = 100.0;

    private static Field GAMMA_VALUE_FIELD;
    static {
        try {
            GAMMA_VALUE_FIELD = OptionInstance.class.getDeclaredField("value");
            GAMMA_VALUE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public FullBright(String s) {
        super(s);
        this.mc = Minecraft.getInstance();
        this.setOn(true);
    }

    private void forceGamma(double v) {
        try {
            GAMMA_VALUE_FIELD.set(mc.options.gamma(), v);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if(!this.isOn()){
            originalGamma = mc.options.gamma().get();
            forceGamma(originalGamma);
            return false;
        }

        if (mc.options.gamma().get() != brightGamma) {
            forceGamma(brightGamma);
        }
        return true;
    }
}



//        if (!this.isOn()) {
//            if (originalGamma != null) {
//                forceGamma(originalGamma);
//                originalGamma = null;
//            }
//            return false;
//        }
//        if (originalGamma == null) {
//            originalGamma = mc.options.gamma().get();
//        }