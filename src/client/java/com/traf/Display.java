package com.traf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;

public class Display {
    private final Minecraft mc;
    private GuiGraphics guiGraphics;
    private static List<String> hackTitles;
    private int displayStartX = 10, displayStartY = 10, deltaYChange = 20;


    public Display(Minecraft mc){
        hackTitles = new ArrayList<>();
        // point to the current instance of minecraft
        this.mc = mc;
        if (mc.player == null) return;

    }

    // this method gets repeatedly called
    public void run(GuiGraphics guiGraphics){
        // keep a reference to the guigraphics within each frame
        this.guiGraphics = guiGraphics;

        // just add the displays you want
        List<Runnable> displays = new ArrayList<>();
        displays.add(this::displayHealth);
        displays.add(this::displayLifetime);

        // this will make the dynamic y for each label change
        for(int i = 0; i < displays.size(); i++){
            displays.get(i).run();
            displayStartY += deltaYChange;
        }

        // add the hacks after the certain displays
        for(int i = 0; i < hackTitles.size(); i++){
            addHackTitle(hackTitles.get(i));
            displayStartY += deltaYChange;
        }

        // reset to default after making sucessfully rendering one frame
        displayStartY = 10;

    }
    public void addHackTitle(String hack){
        guiGraphics.drawString(
                mc.font,
                hack,
                displayStartX,
                displayStartY,
                0xFFFF00FF,
                true
        );
    }

    public static void addDisplayHack(String hack){
        if (!hackTitles.contains(hack)) {
            hackTitles.add(hack);
        }
    }

    public static void removeDisplayHack(String hack) {
        hackTitles.removeIf(h -> h.equals(hack));
    }


    public void displayLifetime(){
        guiGraphics.drawString(
                mc.font,
                "Lifetime: "+ TrafModClient.getGameTicks(),
                displayStartX,
                displayStartY,
                0xFFFF00FF,
                true
        );
    }

    public void displayHealth() {
        float hp = mc.player.getHealth();
        float max = mc.player.getMaxHealth();

        guiGraphics.drawString(
                mc.font,
                "Health: " + hp + " / " + max,
                displayStartX,
                displayStartY,
                0xFFFF00FF,
                true
        );
    }
}
