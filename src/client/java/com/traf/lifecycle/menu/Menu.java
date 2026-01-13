package com.traf.lifecycle.menu;

import com.traf.hacks.Hack;
import com.traf.lifecycle.HackManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;

public class Menu extends Screen {
    private final Screen parent;
    private HackManager hm;
    private EditBox textField;

    private final int backgroundWidth = 210;
    private final int backgroundHeight = 200;

    private int x, y, yDelta = 28;



    public Menu(Screen parent, HackManager hm) {
        super(Component.literal("Traf Client Menu"));
        this.parent = parent;
        this.hm = hm;
    }

    private void setup(int pad, int tfy, int bw){
        this.textField = new EditBox(this.font, x + pad, tfy, bw, 20, Component.empty());
        this.textField.setMaxLength(50);
        this.textField.setBordered(false);
        this.textField.setTextColor(0xEDEDED);
        this.textField.setHint(Component.literal("enter text..."));
        this.addRenderableWidget(this.textField);
        this.setInitialFocus(this.textField);
    }

    // these are where components go to
    @Override
    protected void init() {
        super.init();
        List<Hack> hacks = hm.getAllHacks();

        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;

        // anoying setup
        int pad = 12;
        int bw = backgroundWidth - pad * 2;
        int rowY = y + 34;
        int btnH = 22;
        int tfY = rowY + (hacks.size() * yDelta) + 8; // text field goes UNDER the buttons so it can't overlap
        this.setup(pad, tfY, bw);


        // hack buttons
        for (int i = 0; i < hacks.size(); i++) {
            this.addRenderableWidget(new ClientButton(
                    x + pad,
                    rowY + (i * yDelta),
                    bw,
                    btnH,
                    Component.literal(hacks.get(i).getName()),
                    hacks.get(i)
            ));
        }

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

    private static void drawShadow(GuiGraphics gfx, int x, int y, int w, int h, int radius) {
        for (int i = 1; i <= radius; i++) {
            int a = (int)(70 * (1f - (i / (float)radius)));
            int col = (a << 24);
            gfx.fill(x - i, y - i, x + w + i, y + h + i, col);
        }
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
