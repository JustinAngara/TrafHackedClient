package com.traf.lifecycle.menu;

import com.traf.lifecycle.data.Colors;
import com.traf.lifecycle.data.Dimensions;
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


    public Menu(Screen parent, HackManager hm) {
        super(Component.literal("Traf Client"));
        this.parent = parent;
        this.hm = hm;
    }

    @Override
    protected void init() {
        super.init();
        Map<String, List<Hack>> categorized = categorize(hm.getAllHacks());

        int x = Dimensions.PANEL_X0;
        int y = Dimensions.PANEL_Y0;
        int rowMaxH = 0;

        for (Map.Entry<String, List<Hack>> entry : categorized.entrySet()) {
            CategoryPanel panel = new CategoryPanel(x, y, Dimensions.PANEL_WIDTH, entry.getKey(), entry.getValue());
            this.addRenderableWidget(panel);

            rowMaxH = Math.max(rowMaxH, panel.getFullHeight());
            x += Dimensions.PANEL_WIDTH + Dimensions.PANEL_GAP;

            // wrap to next row if we run off the screen
            if (x + Dimensions.PANEL_WIDTH > this.width - Dimensions.PANEL_X0) {
                x = Dimensions.PANEL_X0;
                y += rowMaxH + Dimensions.PANEL_GAP;
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
        gfx.fill(0, 0, this.width, this.height, Colors.DIM);

        gfx.text(this.font, this.title, Dimensions.PANEL_X0, 4, Colors.TITLE, false);

        // let the framework draw all the renderable widgets category panels
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