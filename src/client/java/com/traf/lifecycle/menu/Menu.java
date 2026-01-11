package com.traf.lifecycle.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class Menu extends Screen {
    private final Screen parent;

    private EditBox textField;

    private final int backgroundWidth = 190;
    private final int backgroundHeight = 170;

    private int x, y;

    public Menu(Screen parent) {
        super(Component.literal("client menu"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;

        int pad = 12;
        int bw = backgroundWidth - pad * 2;

        int rowY = y + 34;

        this.addRenderableWidget(new ClientButton(
                x + pad, rowY, bw, 22,
                Component.literal("feature 1"),
                this::onFeature1Click
        ));

        this.addRenderableWidget(new ClientButton(
                x + pad, rowY + 28, bw, 22,
                Component.literal("feature 2"),
                this::onFeature2Click
        ));

        this.textField = new EditBox(this.font, x + pad, y + 98, bw, 20, Component.empty());
        this.textField.setMaxLength(50);
        this.textField.setBordered(false);
        this.textField.setTextColor(0xEDEDED);
        this.textField.setHint(Component.literal("enter text..."));
        this.addRenderableWidget(this.textField);
        this.setInitialFocus(this.textField);

        this.addRenderableWidget(new ClientButton(
                x + pad, y + 128, 72, 22,
                CommonComponents.GUI_DONE,
                this::onClose
        ));
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        // dim world
        gfx.fill(0, 0, this.width, this.height, 0xA0000000);

        // panel shadow
        drawShadow(gfx, x, y, backgroundWidth, backgroundHeight, 8);

        // panel background
        gfx.fill(x, y, x + backgroundWidth, y + backgroundHeight, 0xE0141416);
        gfx.fill(x, y, x + backgroundWidth, y + 18, 0x401F1F23);
        gfx.renderOutline(x, y, backgroundWidth, backgroundHeight, 0x44FFFFFF);

        // title + label
        gfx.drawString(this.font, this.title, x + 12, y + 10, 0xEDEDED, false);
        gfx.drawString(this.font, Component.literal("custom features"), x + 12, y + 22, 0xA7A7A7, false);

        // editbox background (since we removed bordered)
        int pad = 12;
        int bw = backgroundWidth - pad * 2;

        int tfX1 = x + pad;
        int tfY1 = y + 98;
        int tfX2 = tfX1 + bw;
        int tfY2 = tfY1 + 20;

        gfx.fill(tfX1, tfY1, tfX2, tfY2, 0xFF111113);
        gfx.renderOutline(tfX1, tfY1, bw, 20, 0x22000000);

        super.render(gfx, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }

    private void onFeature1Click() {
        System.out.println("feature 1 activated");
    }

    private void onFeature2Click() {
        System.out.println("feature 2 activated");
        System.out.println("text: " + this.textField.getValue());
    }

    private static void drawShadow(GuiGraphics gfx, int x, int y, int w, int h, int radius) {
        for (int i = 1; i <= radius; i++) {
            int a = (int)(70 * (1f - (i / (float)radius)));
            int col = (a << 24);
            gfx.fill(x - i, y - i, x + w + i, y + h + i, col);
        }
    }
}
