package com.traf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;




public class HitHack extends Hack {
    ViewLockHack vh;
    public HitHack()  {
        vh = new ViewLockHack();
    }
    @Override
    public void run(LocalPlayer lp) {
        if(lp == null) return;
        Minecraft mc = TrafModClient.getMinecraft();
        if (mc.hitResult instanceof EntityHitResult ehr) {

            //this is basically the packet version, but this is insafe
            HandleMouseOutput.leftClick();

//            Entity target = ehr.getEntity();
//            mc.gameMode.attack(mc.player, target);
//            mc.player.swing(InteractionHand.MAIN_HAND);
        }
    }


}
