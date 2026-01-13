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

public class ESP extends Hack {
    private boolean includeMobs;
    private final Minecraft mc;
    private float width;
    public ESP(String s, boolean includeMobs) {
        super(s);
        this.includeMobs = includeMobs;
        this.mc = Minecraft.getInstance();
        this.width = 3.f;
    }

    @Override
    public boolean run(LocalPlayer lp) {
        if(!this.isOn() || lp == null) return false;
        return true;
    }

    /**
     * Call this from your render event handler
     */
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
                renderEntityBox(poseStack, bufferSource, entity, camPos, 0f, 1f, 0f, 1f);
            } else if(includeMobs && entity instanceof Monster) {
                renderEntityBox(poseStack, bufferSource, entity, camPos, 1f, 0f, 0f, 1f);
            } else if(includeMobs && entity instanceof LivingEntity) {
                renderEntityBox(poseStack, bufferSource, entity, camPos, 1f, 1f, 0f, 1f);
            }
        }

        // flush the buffer to actually draw
        bufferSource.endBatch(RenderTypes.LINES);


        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void renderEntityBox(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
                                 Entity entity, Vec3 camPos,
                                 float r, float g, float b, float a) {
        AABB box = entity.getBoundingBox();

        double minX = box.minX - camPos.x;
        double minY = box.minY - camPos.y;
        double minZ = box.minZ - camPos.z;
        double maxX = box.maxX - camPos.x;
        double maxY = box.maxY - camPos.y;
        double maxZ = box.maxZ - camPos.z;

        poseStack.pushPose();

        VertexConsumer buffer = bufferSource.getBuffer(RenderTypes.LINES);
        Matrix4f matrix = poseStack.last().pose();


        // bottom face
        drawLine(buffer, matrix, minX, minY, minZ, maxX, minY, minZ, r, g, b, a);
        drawLine(buffer, matrix, maxX, minY, minZ, maxX, minY, maxZ, r, g, b, a);
        drawLine(buffer, matrix, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        drawLine(buffer, matrix, minX, minY, maxZ, minX, minY, minZ, r, g, b, a);

        // top face
        drawLine(buffer, matrix, minX, maxY, minZ, maxX, maxY, minZ, r, g, b, a);
        drawLine(buffer, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, r, g, b, a);
        drawLine(buffer, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        drawLine(buffer, matrix, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);

        // vertical edges
        drawLine(buffer, matrix, minX, minY, minZ, minX, maxY, minZ, r, g, b, a);
        drawLine(buffer, matrix, maxX, minY, minZ, maxX, maxY, minZ, r, g, b, a);
        drawLine(buffer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, r, g, b, a);
        drawLine(buffer, matrix, minX, minY, maxZ, minX, maxY, maxZ, r, g, b, a);

        poseStack.popPose();
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