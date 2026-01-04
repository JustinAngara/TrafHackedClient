package com.traf.hacks.sub;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public class ViewLockHack extends SubHack {
    private boolean wantInstant = true;

    private static final double CLOSE_RANGE = 12.0;

    private static final double MAX_RANGE = 64.0;
    private static final double MAX_AIM_OFFSET = 0.35;
    private static final float ROTATION_SMOOTHING = 0.3f;

    public ViewLockHack(String s){
        super(s);
    }


    @Override
    public boolean run(LocalPlayer lp) {
        Player target = findTarget(lp);
        if (target != null) {
            lockView(lp, target);

        }
        return true;
    }


    // target
    private Player findTarget(LocalPlayer lp) {
        List<Player> players = lp.level().getEntitiesOfClass(
                Player.class,
                lp.getBoundingBox().inflate(MAX_RANGE),
                p -> p.isAlive() && p != lp  && !p.isSpectator() // basically just condiitons
        );

        // closest visible player within CLOSE_RANGE
        Player closest = null;
        double closestDist = Double.MAX_VALUE;

        for (Player p : players) {
            double dist = lp.distanceTo(p);
            if (dist <= CLOSE_RANGE && dist < closestDist && isVisible(lp, p)) {
                closest = p;
                closestDist = dist;
            }
        }

        if (closest != null) {
            return closest;
        }

        // best FOV-aligned visible player
        Vec3 lookVec = lp.getLookAngle().normalize();
        Vec3 eyePos = lp.getEyePosition(1.0f);

        Player best = null;
        double bestDot = -1.0;

        for (Player p : players) {
            if (!isVisible(lp, p)) continue;

            Vec3 toPlayer = p.getBoundingBox()
                    .getCenter()
                    .subtract(eyePos)
                    .normalize();

            double dot = lookVec.dot(toPlayer);
            if (dot > bestDot) {
                bestDot = dot;
                best = p;
            }
        }

        return best;
    }

    // vis check

    private boolean isVisible(LocalPlayer lp, Player target) {
        Vec3 eye = lp.getEyePosition(1.0f);
        Vec3 targetPoint = target.getBoundingBox().getCenter();

        ClipContext ctx = new ClipContext(
                eye,
                targetPoint,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                lp
        );

        HitResult result = lp.level().clip(ctx);
        return result.getType() == HitResult.Type.MISS;
    }

    // viewlock

    private void lockView(LocalPlayer lp, Player target) {
        Vec3 eye = lp.getEyePosition(1.0f);
        Vec3 aimPoint = getBiasedAimPoint(lp, target);
        Vec3 delta = aimPoint.subtract(eye);

        double dx = delta.x;
        double dy = delta.y;
        double dz = delta.z;

        float targetYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float targetPitch = (float) (-Math.toDegrees(
                Math.atan2(dy, Math.sqrt(dx * dx + dz * dz))
        ));

        if (wantInstant) {
            // snap immediately
            lp.setYRot(targetYaw);
            lp.setXRot(targetPitch);
            lp.yRotO = targetYaw;
            lp.xRotO = targetPitch;
            return;
        }

        // smooth lock
        float yaw = lerpAngle(lp.getYRot(), targetYaw, ROTATION_SMOOTHING);
        float pitch = lerp(lp.getXRot(), targetPitch, ROTATION_SMOOTHING);

        lp.setYRot(yaw);
        lp.setXRot(pitch);
        lp.yRotO = yaw;
        lp.xRotO = pitch;
    }



    // aim point
    private Vec3 getBiasedAimPoint(LocalPlayer lp, Player target) {
        AABB box = target.getBoundingBox();

        Vec3 center = box.getCenter();
        Vec3 eye = lp.getEyePosition(1.0f);
        Vec3 look = lp.getLookAngle().normalize();

        Vec3 rayPoint = eye.add(look.scale(MAX_RANGE));
        Vec3 rawAim = closestPointOnAABB(box, rayPoint);

        Vec3 offset = rawAim.subtract(center);
        if (offset.length() > MAX_AIM_OFFSET) {
            offset = offset.normalize().scale(MAX_AIM_OFFSET);
        }

        return center.add(offset);
    }

    // math stuff

    private Vec3 closestPointOnAABB(AABB box, Vec3 point) {
        return new Vec3(
                clamp(point.x, box.minX, box.maxX),
                clamp(point.y, box.minY, box.maxY),
                clamp(point.z, box.minZ, box.maxZ)
        );
    }

    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(v, max));
    }

    private float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    private float lerpAngle(float current, float target, float t) {
        float delta = wrapDegrees(target - current);
        return current + t * delta;
    }

    private float wrapDegrees(float angle) {
        angle %= 360.0f;
        if (angle >= 180.0f) angle -= 360.0f;
        if (angle < -180.0f) angle += 360.0f;
        return angle;
    }
}
