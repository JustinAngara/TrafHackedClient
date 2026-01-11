package com.traf.lifecycle.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ClientButton extends AbstractButton {
    private final Runnable onPress;
    private float hoverAnim = 0f;

    // theme
    private static final int BG_IDLE   = 0xFF141416;
    private static final int BG_HOVER  = 0xFF1F1F23;
    private static final int TXT_IDLE  = 0xFFBDBDBD;
    private static final int TXT_HOVER = 0xFFFFFFFF;
    private static final int ACCENT_RGB = 0x00B2FF; // rgb only

    public ClientButton(int x, int y, int w, int h, Component message, Runnable onPress) {
        super(x, y, w, h, message);
        this.onPress = onPress;
    }


    @Override
    public void onPress(InputWithModifiers inputWithModifiers) {

    }

    @Override
    protected void renderContents(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        boolean hov = this.isHoveredOrFocused();
        hoverAnim = Mth.lerp(0.18f, hoverAnim, hov ? 1f : 0f);

        int x1 = this.getX();
        int y1 = this.getY();
        int x2 = x1 + this.width;
        int y2 = y1 + this.height;

        int bg = lerpColor(BG_IDLE, BG_HOVER, hoverAnim);
        int border = lerpColor(0x22000000, 0x55FFFFFF, hoverAnim);

        // paint over vanilla
        gfx.fill(x1, y1, x2, y2, bg);
        gfx.renderOutline(x1, y1, this.width, this.height, border);

        // accent strip
        int accentA = (int)(40 + 140 * hoverAnim); // 40..180
        int accent = (accentA << 24) | (ACCENT_RGB & 0x00FFFFFF);
        gfx.fill(x1, y1, x1 + 2, y2, accent);

        // text
        Font font = Minecraft.getInstance().font;
        int textCol = lerpColor(TXT_IDLE, TXT_HOVER, hoverAnim);

        int cx = x1 + this.width / 2;
        int ty = y1 + (this.height - 8) / 2;
        int tw = font.width(this.getMessage());

        gfx.drawString(font, this.getMessage(), cx - tw / 2, ty, textCol, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput out) {
        out.add(NarratedElementType.TITLE, this.getMessage());
    }

    private static int lerpColor(int a, int b, float t) {
        t = Mth.clamp(t, 0f, 1f);

        int aA = (a >>> 24) & 0xFF, aR = (a >>> 16) & 0xFF, aG = (a >>> 8) & 0xFF, aB = a & 0xFF;
        int bA = (b >>> 24) & 0xFF, bR = (b >>> 16) & 0xFF, bG = (b >>> 8) & 0xFF, bB = b & 0xFF;

        int oA = (int)(aA + (bA - aA) * t);
        int oR = (int)(aR + (bR - aR) * t);
        int oG = (int)(aG + (bG - aG) * t);
        int oB = (int)(aB + (bB - aB) * t);

        return (oA << 24) | (oR << 16) | (oG << 8) | oB;
    }
}
