package com.traf.lifecycle;

import com.traf.hacks.*;
import com.traf.lifecycle.display.Display;
import com.traf.hacks.sub.SubHack;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

public class HackManager {
    private LocalPlayer player;
    private List<Hack> hacks = new ArrayList<>();

    public HackManager(){
        setupESP(new ESP("ESP")); // this needs to render aognside of hacks
        hacks.add(new AntiAim("Anti Aim"));
        hacks.add(new Flight("Flight"));
        hacks.add(new Speed("Speed"));
        hacks.add(new AutoHeal("AutoHeal"));
        hacks.add(new AutoAim("Auto Aim"));
    }

    private ESP setupESP(ESP esp){
        hacks.add(esp);
        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            esp.render(
                    context.matrices(),
                    Minecraft.getInstance().renderBuffers().bufferSource()
            );
        });
        return esp;
    }


    public void run(LocalPlayer lp){

        player = lp;
        // next run all the hacks
        for(Hack e : hacks){
            if(e instanceof SubHack) continue; // we don't run sub h acks (aka helpers)
            e.run(player);

            // update title
            if(e.isOn()) {
                Display.addDisplayHack(e.getName());
            } else {
                Display.removeDisplayHack(e.getName());
            }
        }

    }

    /*
     * this will loop through every hack that is queued up in this arraylist
     * */
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
