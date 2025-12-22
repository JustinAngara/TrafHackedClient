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
        if(isOn()==false) return;
        Minecraft mc = TrafModClient.getMinecraft();
        if (lp != null && mc.hitResult instanceof EntityHitResult ehr) {
            // this is a low low attack, far better
            vh.run(lp);
//            HandleMouseOutput.leftClick();

            //this is basically the packet version, but this is insafe
            Entity target = ehr.getEntity();
            mc.gameMode.attack(mc.player, target);
            mc.player.swing(InteractionHand.MAIN_HAND);

        }
    }


}
