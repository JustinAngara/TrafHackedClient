package com.traf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;




public class HitHack extends Hack {
//    Robot r;
    public HitHack()  {
//        r = new Robot();
    }
    @Override
    public void run(LocalPlayer lp) {
        Minecraft mc = TrafModClient.getMinecraft();
        if (lp != null && mc.hitResult instanceof EntityHitResult ehr) {
            // this is a low low attack, far better
            HandleMouseOutput.leftClick();

            /* this is basically the packet version, but this is insafe
                Entity target = ehr.getEntity();
                mc.gameMode.attack(mc.player, target);
                mc.player.swing(InteractionHand.MAIN_HAND);
            */
        }
    }


}
