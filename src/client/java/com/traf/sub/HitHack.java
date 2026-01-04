package com.traf.sub;

import com.traf.lifecycle.HandleKBMOutput;
import com.traf.lifecycle.TrafModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;

public class HitHack extends SubHack {

    public HitHack(String s)  {
        super(s);
    }
    @Override
    public boolean run(LocalPlayer lp) {
        Minecraft mc = TrafModClient.getMinecraft();
        if (mc.hitResult instanceof EntityHitResult ehr) { // only use ehr if you want to perform packet click
            // this will perform external windows mouse click
            HandleKBMOutput.leftClick();
        }
        return true;
    }


//            Entity target = ehr.getEntity();
//            mc.gameMode.attack(mc.player, target);
//            mc.player.swing(InteractionHand.MAIN_HAND);


}
