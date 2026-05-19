package com.traf.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;

import java.lang.reflect.Field;

public class FastMine extends Hack {
    private final Minecraft mc;

    private float boost = 0.3f;

    private static Field DESTROY_DELAY_FIELD;
    private static Field DESTROY_PROGRESS_FIELD;

    static {
        try {
            DESTROY_DELAY_FIELD = MultiPlayerGameMode.class.getDeclaredField("destroyDelay");
            DESTROY_DELAY_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            DESTROY_PROGRESS_FIELD = MultiPlayerGameMode.class.getDeclaredField("destroyProgress");
            DESTROY_PROGRESS_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public FastMine(String s) {
        super(s);
        this.mc = Minecraft.getInstance();
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if (!this.isOn() || lp == null || mc.gameMode == null) return false;

        try {
            if (DESTROY_DELAY_FIELD != null) {
                DESTROY_DELAY_FIELD.setInt(mc.gameMode, 0);
            }

            if (DESTROY_PROGRESS_FIELD != null) {
                float p = DESTROY_PROGRESS_FIELD.getFloat(mc.gameMode);
                if (p > 0f && p < 1f) {
                    DESTROY_PROGRESS_FIELD.setFloat(mc.gameMode, Math.min(p + boost, 1f));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void setBoost(float b) {
        this.boost = Math.max(0f, b);
    }

    public float getBoost() {
        return boost;
    }
}