package com.traf.hacks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.awt.*;

public class ESP extends Hack {
    private boolean includeMobs;
    private final Minecraft mc;
    private float width;
    private Color playerColor;
    private Color mobColor;
    private Color monsterColor;


    public ESP(String s, boolean includeMobs) {
        super(s);
        this.includeMobs = includeMobs;
        this.mc = Minecraft.getInstance();
        this.width = 3.f;

        // setup colors
        playerColor = new Color(255,0,0,255);    // player ofc
        monsterColor = new Color(255,0,255,255); // hostile
        mobColor = new Color(255,255,255,255);   // passive

    }
    public ESP(String s) {
        this(s, true); // default val true
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if(!this.isOn() || lp == null) return false;
        return true;
    }

    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        if(!this.isOn() || mc.level == null) return;

        LocalPlayer player = mc.player;
        if(player == null) return;

        Vec3 camPos = mc.gameRenderer.getMainCamera().position();

        // show through walls
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // draw the entities
        for(Entity entity : mc.level.entitiesForRendering()) {
            if(entity == player) continue;
            if(!(entity instanceof LivingEntity)) continue;

            // filter entities based on settings
            if(entity instanceof Player) {
                renderEntityBox(poseStack, bufferSource, entity, camPos, playerColor);
            }

            //////////// handle mobs //////////////////
            if(!includeMobs) continue;
            if(entity instanceof Monster) {
                renderEntityBox(poseStack, bufferSource, entity, camPos, monsterColor);
            } else if(entity instanceof LivingEntity) {
                renderEntityBox(poseStack, bufferSource, entity, camPos, mobColor);
            }
            ////////////dont add code beyond here////////
        }

        // flush the buffer to actually draw
        bufferSource.endBatch(RenderTypes.LINES);


        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void renderEntityBox(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
                                 Entity entity, Vec3 camPos,
                                 Color c) {

        AABB box = entity.getBoundingBox().move(-camPos.x, -camPos.y, -camPos.z);

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

            // 8 corners of the box
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

            // 12 edges (pairs of corner indices)
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

    public void setIncludeMobs(boolean b) {
        includeMobs = b;
    }

    public boolean getIncludeMobs() {
        return includeMobs;
    }
}

