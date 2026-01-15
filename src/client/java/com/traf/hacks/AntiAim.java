package com.traf.hacks;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class AntiAim extends Hack {
    private float yawSpeed = 20.0f;
    private float currentSpinYaw = 0.0f;

    private float realYaw = 0.0f;
    private float realPitch = 0.0f;

    private AntiAimMode mode = AntiAimMode.SPIN;
    private int tickCounter = 0;

    public enum AntiAimMode {
        SPIN,
        JITTER,
        RANDOM,
        DOWN,
        BACKWARD
    }

    public AntiAim(String s) {
        super(s);
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if (lp == null) return false;

        tickCounter++;
        realYaw = lp.getYRot();
        realPitch = lp.getXRot();

        float fakeYaw = realYaw;
        float fakePitch = realPitch;

        switch (mode) {
            case SPIN:
                float[] spin = spinMode();
                fakeYaw = spin[0];
                fakePitch = spin[1];
                break;
            case JITTER:
                float[] jitter = jitterMode();
                fakeYaw = jitter[0];
                fakePitch = jitter[1];
                break;
            case RANDOM:
                float[] random = randomMode();
                fakeYaw = random[0];
                fakePitch = random[1];
                break;
            case DOWN:
                float[] down = downMode();
                fakeYaw = down[0];
                fakePitch = down[1];
                break;
            case BACKWARD:
                float[] backward = backwardMode();
                fakeYaw = backward[0];
                fakePitch = backward[1];
                break;
        }

        if (lp.connection != null) {
            lp.connection.send(new ServerboundMovePlayerPacket.PosRot(
                    lp.position(),
                    fakeYaw,
                    fakePitch,
                    lp.onGround(),
                    lp.horizontalCollision
            ));
        }

        return true;
    }

    private float[] spinMode() {
        currentSpinYaw += yawSpeed;
        if (currentSpinYaw >= 360.0f) {
            currentSpinYaw -= 360.0f;
        }

        return new float[]{currentSpinYaw, 89.0f};
    }

    private float[] jitterMode() {
        float jitterYaw = (tickCounter % 2 == 0) ? realYaw + 90.0f : realYaw - 90.0f;
        float jitterPitch = (tickCounter % 4 < 2) ? 89.0f : -89.0f;

        return new float[]{jitterYaw, jitterPitch};
    }

    private float[] randomMode() {
        if (tickCounter % 3 == 0) {
            currentSpinYaw = (float) (Math.random() * 360.0f);
        }
        float randomPitch = (float) (Math.random() * 180.0f - 90.0f);

        return new float[]{currentSpinYaw, randomPitch};
    }

    private float[] downMode() {
        return new float[]{realYaw, 90.0f};
    }

    private float[] backwardMode() {
        currentSpinYaw += yawSpeed * 0.5f;
        if (currentSpinYaw >= 360.0f) {
            currentSpinYaw -= 360.0f;
        }

        return new float[]{realYaw + 180.0f + currentSpinYaw, 0.0f};
    }

    private void sendFakeRotation(LocalPlayer lp, float yaw, float pitch) {
        if (lp.connection != null) {
            lp.connection.send(new ServerboundMovePlayerPacket.PosRot(
                    lp.position(),
                    yaw,
                    pitch,
                    lp.onGround(),
                    lp.horizontalCollision
            ));
        }

        lp.yBodyRot = yaw;
        lp.yHeadRot = yaw;
        lp.yBodyRotO = yaw;
        lp.yHeadRotO = yaw;
    }

    public void setMode(AntiAimMode mode) {
        this.mode = mode;
    }

    public void setYawSpeed(float speed) {
        this.yawSpeed = speed;
    }

    public AntiAimMode getMode() {
        return mode;
    }

    public float getYawSpeed() {
        return yawSpeed;
    }

}