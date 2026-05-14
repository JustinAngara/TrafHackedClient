package com.traf.hacks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class XRay extends Hack {
    private final Minecraft mc;
    private final float width = 2.f;

    // how far (in blocks, each axis) around the player to scan
    private int range = 32;

    // rescan every N ticks - scanning is expensive
    private int rescanInterval = 20;
    private int tickCounter = 0;

    // ore -> highlight color
    private final Map<Block, Color> oreColors = new HashMap<>();

    // last scan results: position -> color
    private final Map<BlockPos, Color> hits = new HashMap<>();

    public XRay(String s) {
        super(s);
        this.mc = Minecraft.getInstance();
        setupOres();
    }

    private void setupOres() {
        Color diamond  = new Color(0,   255, 255, 255);
        Color gold     = new Color(255, 215, 0,   255);
        Color iron     = new Color(216, 175, 147, 255);
        Color emerald  = new Color(0,   255, 0,   255);
        Color redstone = new Color(255, 0,   0,   255);
        Color lapis    = new Color(0,   90,  255, 255);
        Color coal     = new Color(60,  60,  60,  255);
        Color copper   = new Color(220, 140, 70,  255);
        Color debris   = new Color(150, 70,  150, 255);
        Color quartz   = new Color(245, 230, 220, 255);

        oreColors.put(Blocks.DIAMOND_ORE,           diamond);
        oreColors.put(Blocks.DEEPSLATE_DIAMOND_ORE, diamond);

        oreColors.put(Blocks.GOLD_ORE,              gold);
        oreColors.put(Blocks.DEEPSLATE_GOLD_ORE,    gold);
        oreColors.put(Blocks.NETHER_GOLD_ORE,       gold);

        oreColors.put(Blocks.IRON_ORE,              iron);
        oreColors.put(Blocks.DEEPSLATE_IRON_ORE,    iron);

        oreColors.put(Blocks.EMERALD_ORE,           emerald);
        oreColors.put(Blocks.DEEPSLATE_EMERALD_ORE, emerald);

        oreColors.put(Blocks.REDSTONE_ORE,          redstone);
        oreColors.put(Blocks.DEEPSLATE_REDSTONE_ORE,redstone);

        oreColors.put(Blocks.LAPIS_ORE,             lapis);
        oreColors.put(Blocks.DEEPSLATE_LAPIS_ORE,   lapis);

        oreColors.put(Blocks.COAL_ORE,              coal);
        oreColors.put(Blocks.DEEPSLATE_COAL_ORE,    coal);

        oreColors.put(Blocks.COPPER_ORE,            copper);
        oreColors.put(Blocks.DEEPSLATE_COPPER_ORE,  copper);

        oreColors.put(Blocks.ANCIENT_DEBRIS,        debris);
        oreColors.put(Blocks.NETHER_QUARTZ_ORE,     quartz);
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
                    Color c = oreColors.get(state.getBlock());
                    if (c != null) {
                        hits.put(cur.immutable(), c);
                    }
                }
            }
        }
    }

    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        if (!this.isOn() || mc.level == null || hits.isEmpty()) return;

        Vec3 camPos = mc.gameRenderer.getMainCamera().position();

        // see through walls
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (Map.Entry<BlockPos, Color> entry : hits.entrySet()) {
            renderBlockBox(poseStack, bufferSource, entry.getKey(), camPos, entry.getValue());
        }

        bufferSource.endBatch(RenderTypes.LINES);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void renderBlockBox(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
                                BlockPos pos, Vec3 camPos, Color c) {

        AABB box = new AABB(pos).move(-camPos.x, -camPos.y, -camPos.z);

        double minX = box.minX, minY = box.minY, minZ = box.minZ;
        double maxX = box.maxX, maxY = box.maxY, maxZ = box.maxZ;

        float r = (float) c.getRed()   / 255.f;
        float g = (float) c.getGreen() / 255.f;
        float b = (float) c.getBlue()  / 255.f;
        float a = (float) c.getAlpha() / 255.f;

        poseStack.pushPose();
        try {
            VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.LINES);
            Matrix4f matrix = poseStack.last().pose();

            double[][] p = {
                    {minX, minY, minZ}, // 0
                    {maxX, minY, minZ}, // 1
                    {maxX, minY, maxZ}, // 2
                    {minX, minY, maxZ}, // 3
                    {minX, maxY, minZ}, // 4
                    {maxX, maxY, minZ}, // 5
                    {maxX, maxY, maxZ}, // 6
                    {minX, maxY, maxZ}, // 7
            };

            int[][] e = {
                    {0,1},{1,2},{2,3},{3,0}, // bottom
                    {4,5},{5,6},{6,7},{7,4}, // top
                    {0,4},{1,5},{2,6},{3,7}  // verticals
            };

            for (int[] edge : e) {
                double[] a0 = p[edge[0]];
                double[] a1 = p[edge[1]];
                drawLine(buffer, matrix,
                        a0[0], a0[1], a0[2],
                        a1[0], a1[1], a1[2],
                        r, g, b, a);
            }
        } finally {
            poseStack.popPose();
        }
    }

    private void drawLine(VertexConsumer buffer, Matrix4f matrix,
                          double x1, double y1, double z1,
                          double x2, double y2, double z2,
                          float r, float g, float b, float a) {

        buffer.addVertex(matrix, (float)x1, (float)y1, (float)z1)
                .setColor(r, g, b, a)
                .setNormal(1f, 0f, 0f)
                .setLineWidth(width);
        buffer.addVertex(matrix, (float)x2, (float)y2, (float)z2)
                .setColor(r, g, b, a)
                .setNormal(1f, 0f, 0f)
                .setLineWidth(width);
    }

    // ----- configuration -----

    public void addOre(Block block, Color color) {
        oreColors.put(block, color);
    }

    public void removeOre(Block block) {
        oreColors.remove(block);
        // existing hits with this block stay until next scan; force it
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