package com.traf.hacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class MobAura extends Hack {
    private final Minecraft mc;

    private double range = 5.0;

    private boolean rotate = false;

    private boolean hostileOnly = false;

    public MobAura(String s) {
        super(s);
        this.mc = Minecraft.getInstance();
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if (!this.isOn() || lp == null) return false;
        if (mc.level == null || mc.gameMode == null) return false;

        // wait for the swing cooldown so each hit deals full damage
        if (lp.getAttackStrengthScale(0f) < 1.0f) return true;

        Mob target = findClosestMob(lp);
        if (target == null) return true;

        if (rotate) faceEntity(lp, target);

        mc.gameMode.attack(lp, target);
        lp.swing(InteractionHand.MAIN_HAND);
        return true;
    }

    private Mob findClosestMob(LocalPlayer lp) {
        Vec3 pos = lp.getEyePosition();
        Mob closest = null;
        double closestSq = range * range;

        for (Entity e : mc.level.entitiesForRendering()) {
            if (!(e instanceof Mob mob)) continue;
            if (!mob.isAlive()) continue;
            if (hostileOnly && !(mob instanceof net.minecraft.world.entity.monster.Monster)) continue;

            double dsq = mob.getEyePosition().distanceToSqr(pos);
            if (dsq < closestSq) {
                closestSq = dsq;
                closest = mob;
            }
        }
        return closest;
    }

    private void faceEntity(LocalPlayer lp, Entity target) {
        Vec3 eye = lp.getEyePosition();
        Vec3 tgt = target.position().add(0, target.getBbHeight() * 0.5, 0);
        double dx = tgt.x - eye.x;
        double dy = tgt.y - eye.y;
        double dz = tgt.z - eye.z;
        double horiz = Math.sqrt(dx * dx + dz * dz);

        float yaw   = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90f);
        float pitch = (float) -Math.toDegrees(Math.atan2(dy, horiz));

        lp.setYRot(yaw);
        lp.setXRot(pitch);
    }

    // setters getters
    public void setRange(double r)         { this.range = Math.max(1.0, r); }
    public double getRange()               { return range; }

    public void setRotate(boolean b)       { this.rotate = b; }
    public boolean getRotate()             { return rotate; }

    public void setHostileOnly(boolean b)  { this.hostileOnly = b; }
    public boolean getHostileOnly()        { return hostileOnly; }
}