package com.traf.lifecycle;

import com.mojang.blaze3d.platform.InputConstants;
import com.traf.hacks.*;
import com.traf.hacks.sub.VClip;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

// getMenu()
public class KeyListenerManager {
    private static KeyMapping swingKey;
    private static Map<KeyMapping, Hack> km;
    private HackManager hm;
    public KeyListenerManager(HackManager hm) {
        this.hm = hm;
        km = new HashMap<>();
        // create the key binds here
        createKeyBind(this.hm.getHack(MobAura.class), "mobaura", GLFW.GLFW_KEY_R);
        createKeyBind(this.hm.getHack(Flight.class), "flight", GLFW.GLFW_KEY_F);
        createKeyBind(this.hm.getHack(Speed.class), "speed", GLFW.GLFW_KEY_V);
        createKeyBind(this.hm.getHack(AutoHeal.class), "autoheal", GLFW.GLFW_KEY_KP_0);
        createKeyBind(this.hm.getHack(ESP.class), "esp", GLFW.GLFW_KEY_Y);
        createKeyBind(this.hm.getHack(XRay.class), "xray", GLFW.GLFW_KEY_X);

        createKeyBind(this.hm.getHack(VClip.class), "vclip", GLFW.GLFW_KEY_F7);
        createKeyBind(null, "menu", GLFW.GLFW_KEY_RIGHT_SHIFT);

    }

    public void start(){

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for(var e : km.entrySet()){
                if(!e.getKey().consumeClick()) continue;

                Object temp = e.getValue();

                // for menu specefically
                if(e.getValue()==null){
                    TrafModClient.openMenu();
                    continue;
                }


                try{
                    // reverse teh switch
                    Hack x = (Hack) temp;
                    x.setOn(!x.isOn());
                    System.out.println("this is the value: "+temp.getClass()+" "+x.isOn());
                } catch (IllegalArgumentException exc){
                    System.out.println("should only include objects" + exc);
                }
            }




        });
    }

    public KeyMapping getKeyMapping(String feature, int keyCode){
        return KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.modid." + feature,
                InputConstants.Type.KEYSYM,
                keyCode,
                KeyMapping.Category.MISC
        ));
    }

    public void createKeyBind(Hack hack, String feature, int keyCode){
        KeyMapping temp = getKeyMapping(feature, keyCode);
        km.put(temp, hack);
    }

}
