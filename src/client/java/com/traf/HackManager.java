package com.traf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

public class HackManager {
    private LocalPlayer player;
    private List<Hack> hacks = new ArrayList<>();

    public HackManager(){
        hacks.add(new AutoAim("Auto Aim"));
        hacks.add(new Flight("Flight"));
        hacks.add(new Speed("Speed"));
    }

    public <T extends Hack> T getHack(Class<T> hackClass) {
        for (Hack hack : hacks) {
            if (hackClass.isInstance(hack)) {
                return hackClass.cast(hack);
            }
        }
        return null;
    }
    /*
    * This will loop through every hack that is queued up in this arraylist
    * */
    public void run(LocalPlayer lp){

        player = lp;
        // next run all the hacks
        for(Hack e : hacks){
            e.run(player);

            // update title
            if(e.isOn()) {
                Display.addDisplayHack(e.getName());
            } else {
                Display.removeDisplayHack(e.getName());
            }
        }

    }
}
