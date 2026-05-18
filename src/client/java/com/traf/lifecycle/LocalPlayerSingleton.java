package com.traf.lifecycle;

import net.minecraft.client.player.LocalPlayer;

/*
* should control view angles, registry, etc.
* different than hack manager as this will be the way how we interact with common methods
* TODO: Refactor auto aim
* */
public class LocalPlayerSingleton {
    private static LocalPlayer lp;

    public static LocalPlayer get(){
        return lp;
    }
}
