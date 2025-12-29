package com.traf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

// inteded to inherint some stuff
public abstract class Hack {

    private boolean isOn = false;
    private int currentTick = -1;


    public abstract void run(LocalPlayer lp);

    // helper methods
    public void setOn(boolean b){ isOn = b; }
    public boolean isOn(){ return isOn; }
    public int getCurrentTick(){ return currentTick; }
    public void setCurrentTick(int t){ currentTick = t; }
    public void incrementTick(){ currentTick++; }
}
