package com.traf.lifecycle.menu;

import com.traf.lifecycle.data.Colors;
import com.traf.hacks.Hack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class CategoryPanel extends AbstractWidget {

    private static final int HEADER_H = 18;
    private static final int ENTRY_H  = 14;
    private static final int PAD      = 4;


    private final Component titleText;
    private final Component glyphOpen  = Component.literal("-");
    private final Component glyphClose = Component.literal("+");

    private final List<Hack> hacks;
    private final List<Component> hackNames;
    private boolean expanded = true;

    public CategoryPanel(int x, int y, int w, String title, List<Hack> hacks) {
        super(x, y, w, fullHeight(hacks.size()), Component.literal(title));
        this.titleText = Component.literal(title);
        this.hacks = hacks;
        this.hackNames = new ArrayList<>(hacks.size());
        for (Hack h : hacks) this.hackNames.add(Component.literal(h.getName()));
    }

    private static int fullHeight(int n) {
        return HEADER_H + (n * ENTRY_H) + PAD;
    }

    public int getFullHeight() {
        return fullHeight(hacks.size());
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float delta) {
        final int x = this.getX();
        final int y = this.getY();
        final int w = this.width;
        final int h = this.height;

        Font font = Minecraft.getInstance().font;

        gfx.fill(x, y, x + w, y + h, Colors.BG);
        gfx.outline(x, y, w, h, Colors.OUTLINE);

        gfx.fill(x, y, x + w, y + HEADER_H, Colors.HEADER_BG);
        gfx.text(font, titleText, x + 6, y + 5, Colors.TXT, false);

        Component glyph = expanded ? glyphOpen : glyphClose;
        int glyphW = font.width(glyph);
        gfx.text(font, glyph, x + w - 6 - glyphW, y + 5, Colors.TXT_DIM, false);

        if (!expanded) return;
        for (int i = 0; i < hacks.size(); i++) {
            Hack hk = hacks.get(i);
            int ey = y + HEADER_H + (i * ENTRY_H);

            if (mouseX >= x && mouseX < x + w && mouseY >= ey && mouseY < ey + ENTRY_H) {
                gfx.fill(x, ey, x + w, ey + ENTRY_H, Colors.HOVER_BG);
            }

            int dot = hk.isOn() ? Colors.ON_DOT : Colors.OFF_DOT;
            gfx.fill(x + 6, ey + 5, x + 10, ey + 9, dot);

            gfx.text(font, hackNames.get(i),
                    x + 14, ey + 3,
                    hk.isOn() ? Colors.TXT : Colors.TXT_DIM, false);
        }
    }

    @Override
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        double mouseY = event.y();
        final int y = this.getY();

        if (mouseY < y + HEADER_H) {
            expanded = !expanded;
            this.height = expanded ? fullHeight(hacks.size()) : HEADER_H;
            return;
        }
        if (!expanded) return;

        int idx = (int) ((mouseY - y - HEADER_H) / ENTRY_H);
        if (idx >= 0 && idx < hacks.size()) {
            Hack hk = hacks.get(idx);
            hk.setOn(!hk.isOn());
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput out) {
        out.add(NarratedElementType.TITLE, titleText);
    }
}