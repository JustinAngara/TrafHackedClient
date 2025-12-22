package com.traf;

import net.minecraft.client.player.LocalPlayer;

public class AutoAim extends Hack {
    // create a reference to View Lock Hack
    static ViewLockHack vlh;
    static HitHack hh;
    static{
        vlh = new ViewLockHack();
        hh = new HitHack();
    }

    public AutoAim(){
        this.setOn(false);

    }
    @Override
    public void run(LocalPlayer lp) {
        if(isOn()==false) return;
        vlh.run(lp);
        hh.run(lp);
    }
}
