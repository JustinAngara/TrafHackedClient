package com.traf.lifecycle.menu;

import com.traf.hacks.Hack;
import com.traf.lifecycle.HackManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Menu extends Screen {
    private final Screen parent;
    private final HackManager hm;

    // panel grid layout
    private static final int PANEL_WIDTH = 110;
    private static final int PANEL_GAP   = 10;
    private static final int PANEL_X0    = 14;
    private static final int PANEL_Y0    = 14;

    public Menu(Screen parent, HackManager hm) {
        super(Component.literal("Click GUI"));
        this.parent = parent;
        this.hm = hm;
    }

    @Override
    protected void init() {
        super.init();
        Map<String, List<Hack>> categorized = categorize(hm.getAllHacks());

        int x = PANEL_X0;
        int y = PANEL_Y0;
        int rowMaxH = 0;

        for (Map.Entry<String, List<Hack>> entry : categorized.entrySet()) {
            CategoryPanel panel = new CategoryPanel(x, y, PANEL_WIDTH, entry.getKey(), entry.getValue());
            this.addRenderableWidget(panel);

            rowMaxH = Math.max(rowMaxH, panel.getFullHeight());
            x += PANEL_WIDTH + PANEL_GAP;

            // wrap to next row if we run off the screen
            if (x + PANEL_WIDTH > this.width - PANEL_X0) {
                x = PANEL_X0;
                y += rowMaxH + PANEL_GAP;
                rowMaxH = 0;
            }
        }
    }

    private Map<String, List<Hack>> categorize(List<Hack> hacks) {
        Map<String, List<Hack>> result = new LinkedHashMap<>();
        result.put("Combat",   new ArrayList<>());
        result.put("Movement", new ArrayList<>());
        result.put("Render",   new ArrayList<>());
        result.put("Player",   new ArrayList<>());
        result.put("World",    new ArrayList<>());
        result.put("Misc",     new ArrayList<>());

        for (Hack h : hacks) {
            String n = h.getName().toLowerCase();
            if (n.contains("aim") || n.contains("aura") || n.contains("kill")) {
                result.get("Combat").add(h);
            } else if (n.contains("flight") || n.contains("speed") || n.contains("fall") || n.contains("clip") || n.contains("step")) {
                result.get("Movement").add(h);
            } else if (n.contains("esp") || n.contains("xray") || n.contains("bright") || n.contains("tracer")) {
                result.get("Render").add(h);
            } else if (n.contains("heal") || n.contains("regen") || n.contains("hunger") || n.contains("food")) {
                result.get("Player").add(h);
            } else if (n.contains("chest") || n.contains("scaffold") || n.contains("mine")) {
                result.get("World").add(h);
            } else {
                result.get("Misc").add(h);
            }
        }

        // drop empty categories so they don't render as empty headers
        result.values().removeIf(List::isEmpty);
        return result;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float delta) {
        // dim world behind GUI
        gfx.fill(0, 0, this.width, this.height, 0xA0000000);

        // title bar
        gfx.text(this.font, this.title, PANEL_X0, 4, 0xFFEDEDED, false);

        // let the framework draw all the renderable widgets (the CategoryPanels)
        super.extractRenderState(gfx, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }
}