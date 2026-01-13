package com.traf.lifecycle.display;

import com.traf.lifecycle.TrafModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Display {
    private final Minecraft mc;
    private GuiGraphics guiGraphics;
    private static List<String> hackTitles;
    private int displayStartX = 10, displayStartY = 10, deltaYChange = 9;

    // colors
    private int baseR, baseG, baseB;
    private static final int MIN_CHANNEL = 80;
    private static final int RANGE = 256 - MIN_CHANNEL;


    public Display(Minecraft mc){
        hackTitles = new ArrayList<>();
        // point to the current instance of minecraft
        this.mc = mc;
        if (mc.player == null) return;
        baseR = ThreadLocalRandom.current().nextInt(256);
        baseG = ThreadLocalRandom.current().nextInt(256);
        baseB = ThreadLocalRandom.current().nextInt(256);
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
                animatedColorRGBA(),
                true
        );
    }



    public static boolean addDisplayHack(String hack){
        if (!hackTitles.contains(hack)) {
            hackTitles.add(hack);
            return true;
        }
        return false;
    }

    public static boolean removeDisplayHack(String hack) {
        return hackTitles.removeIf(h -> h.equals(hack));
    }



    int animatedColorRGBA() {
        int tick = (int) TrafModClient.getGameTicks();

        int r = MIN_CHANNEL + ((baseR + tick) % RANGE);
        int g = MIN_CHANNEL + ((baseG + tick * 2) % RANGE);
        int b = MIN_CHANNEL + ((baseB + tick * 3) % RANGE);

        int a = 0xFF;
        return (r << 24) | (g << 16) | (b << 8) | a;
    }


    private void displayLifetime(){
        guiGraphics.drawString(
                mc.font,
                "Lifetime: "+ TrafModClient.getGameTicks(),
                displayStartX,
                displayStartY,
                0xFFFF00FF,
                true
        );
    }


    private void displayHealth() {
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
