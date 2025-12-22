package com.traf;

import net.minecraft.client.player.LocalPlayer;

public class AutoAim extends Hack {
    // create a reference to View Lock Hack
    static ViewLockHack vlh;
    static{
         vlh = new ViewLockHack();
    }

    public AutoAim(){
        this.setOn(false);

    }
    @Override
    public void run(LocalPlayer lp) {
        while(this.isOn()){
//            System.out.println("This works now in the auto aim");
            vlh.run(lp);
        }
    }
}
