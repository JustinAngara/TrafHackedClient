package com.traf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

// inteded to inherint some stuff
public abstract class Hack {

    private boolean isOn = false;



    public abstract void run(LocalPlayer lp, Minecraft mc);

    // helper methods
    public void setOn(boolean b){ isOn = b; }
    public boolean isOn(){ return isOn; }
}
