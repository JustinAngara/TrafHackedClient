package com.traf;

import net.minecraft.client.player.LocalPlayer;

public class AutoAim extends Hack {
    // create a reference to View Lock Hack
    static ViewLockHack vlh;
    static HitHack hh;
    static{
        vlh = new ViewLockHack("View Lock Hack");
        hh = new HitHack("Hit Hack");
    }

    public AutoAim(String s) {
        super(s);
        this.setOn(false);

    }
    @Override
    public boolean run(LocalPlayer lp) {
        if(isOn()==false) return false;
        
        vlh.run(lp);
        hh.run(lp);
        return true;
    }
}
