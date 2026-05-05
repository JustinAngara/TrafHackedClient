package com.traf.lifecycle.menu;

// -550, 2300

import com.traf.hacks.Hack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ClientButton extends AbstractWidget {
    private final Object onPress;
    private float hoverAnim = 0f;

    private static final int BG_IDLE    = 0xFF141416;
    private static final int BG_HOVER   = 0xFF1F1F23;
    private static final int TXT_IDLE   = 0xFFBDBDBD;
    private static final int TXT_HOVER  = 0xFFFFFFFF;
    private static final int ACCENT_RGB = 0x00B2FF;

    public ClientButton(int x, int y, int w, int h, Component message, Hack onPress) {
        super(x, y, w, h, message);
        this.onPress = onPress;
    }

    public ClientButton(int x, int y, int w, int h, Component message, Runnable onPress) {
        super(x, y, w, h, message);
        this.onPress = onPress;
    }

    // overloaded method
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // this method is modified i dont know if this works
        int left = 263;
        MouseButtonInfo mbi = new MouseButtonInfo(button, left);
        MouseButtonEvent mbc = new MouseButtonEvent(mouseX, mouseY, mbi);
        if (this.active && this.visible && this.isValidClickButton(mbi) && this.mouseClicked(mbc, false)) {
            if (onPress instanceof Hack temp) {
                temp.setOn(!temp.isOn());
            } else if (onPress instanceof Runnable r) {
                r.run();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float delta) {
        boolean hov = this.isHoveredOrFocused();
        hoverAnim = Mth.lerp(0.18f, hoverAnim, hov ? 1f : 0f);

        int x1 = this.getX(), y1 = this.getY();
        int x2 = x1 + this.width, y2 = y1 + this.height;

        gfx.fill(x1, y1, x2, y2, lerpColor(BG_IDLE, BG_HOVER, hoverAnim));
        gfx.outline(x1, y1, this.width, this.height, lerpColor(0x22000000, 0x55FFFFFF, hoverAnim));

        int accentA = (int)(40 + 140 * hoverAnim);
        gfx.fill(x1, y1, x1 + 2, y2, (accentA << 24) | (ACCENT_RGB & 0x00FFFFFF));

        Font font = Minecraft.getInstance().font;
        int tw = font.width(this.getMessage());
        gfx.text(font, this.getMessage(),
                x1 + this.width / 2 - tw / 2,
                y1 + (this.height - 8) / 2,
                lerpColor(TXT_IDLE, TXT_HOVER, hoverAnim), false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput out) {
        out.add(NarratedElementType.TITLE, this.getMessage());
    }

    private static int lerpColor(int a, int b, float t) {
        t = Mth.clamp(t, 0f, 1f);
        int aA = (a>>>24)&0xFF, aR = (a>>>16)&0xFF, aG = (a>>>8)&0xFF, aB = a&0xFF;
        int bA = (b>>>24)&0xFF, bR = (b>>>16)&0xFF, bG = (b>>>8)&0xFF, bB = b&0xFF;
        return ((int)(aA+(bA-aA)*t)<<24)|((int)(aR+(bR-aR)*t)<<16)|((int)(aG+(bG-aG)*t)<<8)|(int)(aB+(bB-aB)*t);
    }
}