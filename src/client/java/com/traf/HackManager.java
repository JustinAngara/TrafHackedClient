package com.traf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.ArrayList;
import java.util.List;

public class HackManager {
    private LocalPlayer player;
    private List<Hack> hacks = new ArrayList<>();

    public HackManager(){
        hacks.add(new ViewLockHack()); //add hacks i want

    }

    public void run(LocalPlayer lp, Minecraft mc){
        player = lp;
        // next run all the hacks
        for(Hack e : hacks){
            e.run(player, mc);
        }

    }
}
