package com.traf;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.EntityHitResult;




public class HitHack extends Hack {
//    Robot r;
    public HitHack() throws AWTException {
//        r = new Robot();
    }
    @Override
    public void run(LocalPlayer lp) {
        Minecraft mc = TrafModClient.getMinecraft();
        if (lp != null && mc.hitResult instanceof EntityHitResult ehr) {

            mb1Attack();

            /* this is basically the packet version, but this is insafe
                Entity target = ehr.getEntity();
                mc.gameMode.attack(mc.player, target);
                mc.player.swing(InteractionHand.MAIN_HAND);
            */
        }
    }


    // external to make it harder to detect
    public void mb1Attack() {
//        r.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//        r.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    }
}
