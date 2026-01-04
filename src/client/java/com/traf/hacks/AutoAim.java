package com.traf.hacks;

import com.traf.hacks.sub.HitHack;
import com.traf.hacks.sub.ViewLockHack;
import net.minecraft.client.player.LocalPlayer;

public class AutoAim extends Hack {
    // create a reference to View Lock Hack
    private static ViewLockHack vlh;
    private static HitHack hh;
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
