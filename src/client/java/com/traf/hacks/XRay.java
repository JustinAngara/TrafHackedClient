package com.traf.hacks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.traf.lifecycle.data.Colors;
import com.traf.lifecycle.registry.OreRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class XRay extends ESP {

    // how far (in blocks, each axis) around the player to scan
    private int range = 32;

    // rescan every N ticks - scanning is expensive
    private int rescanInterval = 20;
    private int tickCounter = 0;

    // ore -> highlight color
    private final Map<Block, Color> oreColors = new HashMap<>();

    // position -> color
    private final Map<BlockPos, Color> hits = new HashMap<>();


    public XRay(String s) {
        super(s, false, false); // disable mob/item ESP - XRay only cares about blocks
    }


    @Override
    public boolean run(LocalPlayer lp) {
        if (!this.isOn() || lp == null) {
            if (!hits.isEmpty()) hits.clear();
            tickCounter = 0;
            return false;
        }

        if (tickCounter <= 0) {
            scan(lp);
            tickCounter = rescanInterval;
        } else {
            tickCounter--;
        }
        return true;
    }

    private void scan(LocalPlayer lp) {
        if (mc.level == null) return;
        hits.clear();

        BlockPos center = lp.blockPosition();
        BlockPos.MutableBlockPos cur = new BlockPos.MutableBlockPos();

        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                for (int dz = -range; dz <= range; dz++) {
                    cur.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    BlockState state = mc.level.getBlockState(cur);
                    Color c = OreRegistry.colorFor(state);
                    if (c != null) hits.put(cur.immutable(), c);
                }
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        if (!this.isOn() || mc.level == null || hits.isEmpty()) return;

        Vec3 camPos = mc.gameRenderer.getMainCamera().position();

        // see through walls
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (Map.Entry<BlockPos, Color> entry : hits.entrySet()) {
            BlockPos pos = entry.getKey();
            AABB box = new AABB(pos).move(-camPos.x, -camPos.y, -camPos.z);
            renderAABB(poseStack, bufferSource, box, entry.getValue());
        }

        bufferSource.endBatch(RenderTypes.LINES);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    // configurations
    public void addOre(Block block, Color color) {
        oreColors.put(block, color);
    }

    public void removeOre(Block block) {
        oreColors.remove(block);
        hits.entrySet().removeIf(en -> mc.level != null
                && mc.level.getBlockState(en.getKey()).getBlock() == block);
    }

    public void clearOres() {
        oreColors.clear();
        hits.clear();
    }

    public void setRange(int range) {
        this.range = Math.max(1, range);
    }

    public int getRange() {
        return range;
    }

    public void setRescanInterval(int ticks) {
        this.rescanInterval = Math.max(1, ticks);
    }

    public int getRescanInterval() {
        return rescanInterval;
    }
}