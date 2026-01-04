package com.traf.lifecycle;

import com.traf.lifecycle.display.Display;
import com.traf.hacks.AutoAim;
import com.traf.hacks.Flight;
import com.traf.hacks.Hack;
import com.traf.hacks.Speed;
import com.traf.sub.SubHack;
import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

public class HackManager {
    private LocalPlayer player;
    private List<Hack> hacks = new ArrayList<>();

    public HackManager(){
        hacks.add(new Flight("Flight"));
        hacks.add(new Speed("Speed"));
        hacks.add(new AutoAim("Auto Aim"));
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
            if(e instanceof SubHack) continue; // we don't run sub hacks (aka helpers)
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
