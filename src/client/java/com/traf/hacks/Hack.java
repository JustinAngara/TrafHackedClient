package com.traf.hacks;

import net.minecraft.client.player.LocalPlayer;

import java.awt.*;

// inteded to inherint some stuff
public abstract class Hack {

    private boolean isOn = false;
    private int currentTick = -1;
    private String name;
    private FeatureProperties fp;
    public Hack(String s) {
        name = s;
        fp = new FeatureProperties();
    }
    public abstract boolean run(LocalPlayer lp);

    // helper methods
    public void setOn(boolean b)       { isOn = b; }
    public boolean isOn()              { return isOn; }

    public int getCurrentTick()        { return currentTick; }
    public void setCurrentTick(int t)  { currentTick = t; }
    public String getName()           { return name; }

    public void setName(String s)     { name = s; }

    // intended for internally seeing properties/params of a given hack
    private FeatureProperties getFeatureProperties()  { return fp; }

    // optional: explains the lifetime of a component
    public void incrementTick()        { currentTick++; }
}
