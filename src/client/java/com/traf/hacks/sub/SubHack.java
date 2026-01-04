package com.traf.hacks.sub;


import com.traf.hacks.Hack;

public abstract class SubHack extends Hack {
    public SubHack(String s) {
        super(s);

    }

    @Override
    public final void setOn(boolean b) {
        throw new UnsupportedOperationException(
                "subHack isn't meant to be called"
        );
    }
    @Override
    public final boolean isOn() {
        throw new UnsupportedOperationException(
                "sub hack shouldn't be referencing on attributes"
        );
    }

}
