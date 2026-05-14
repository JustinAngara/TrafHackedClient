package com.traf.lifecycle;

import com.traf.hacks.*;
import com.traf.lifecycle.display.Display;
import com.traf.hacks.sub.SubHack;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

public class HackManager {
    private LocalPlayer player;
    private List<Hack> hacks = new ArrayList<>();

    public HackManager(){
        setupESP(new ESP("ESP"));
        setupESP(new XRay("XRay"));

        hacks.add(new FullBright("Full Bright"));
        hacks.add(new AntiAim("Anti Aim"));
        hacks.add(new Flight("Flight"));
        hacks.add(new Speed("Speed"));
        hacks.add(new AutoHeal("AutoHeal"));
        hacks.add(new AutoAim("Auto Aim"));
        hacks.add(new Regen("Regen"));
        hacks.add(new NoFall("NoFall"));
    }

    private ESP setupESP(ESP esp) {
        hacks.add(esp);
        LevelRenderEvents.AFTER_SOLID_FEATURES.register((context) -> {
            esp.render(
                    context.poseStack(),
                    Minecraft.getInstance().renderBuffers().bufferSource()
            );
        });
        return esp;
    }

    public void run(LocalPlayer lp){
        player = lp;
        for(Hack e : hacks){
            if(e instanceof SubHack) continue;
            e.run(player);

            if(e.isOn()) {
                Display.addDisplayHack(e.getName());
            } else {
                Display.removeDisplayHack(e.getName());
            }
        }
    }

    public <T extends Hack> T getHack(Class<T> hackClass) {
        for (Hack hack : hacks) {
            if (hackClass.isInstance(hack)) {
                return hackClass.cast(hack);
            }
        }
        return null;
    }

    public List<Hack> getAllHacks(){ return hacks; }
}