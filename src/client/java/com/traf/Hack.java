package com.traf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

// inteded to inherint some stuff
public abstract class Hack {

    private boolean isOn = false;
    private int currentTick = -1;
    private String name;

    public Hack(String s){ name = s; }
    public abstract boolean run(LocalPlayer lp);

    // helper methods
    public void setOn(boolean b)       { isOn = b; }
    public boolean isOn()              { return isOn; }

    public int getCurrentTick()        { return currentTick; }
    public void setCurrentTick(int t)  { currentTick = t; }

    private String getName()           { return name; }
    private void setName(String s)     { name = s; }

    public void incrementTick()        { currentTick++; }
}
