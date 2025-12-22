package com.traf;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class KeyListenerManager {
    private static KeyMapping swingKey;
    private static Map<KeyMapping, Hack> km;
    private HackManager hm;
    public KeyListenerManager(HackManager hm) {
        this.hm = hm;
        km = new HashMap<>();
        // create the key binds here
//        createKeyBind(new ViewLockHack(), "LOCK", GLFW.GLFW_KEY_R );
//        createKeyBind(new HitHack(), "HIT", GLFW.GLFW_KEY_V );
        createKeyBind(this.hm.getHack(AutoAim.class), "autoaim", GLFW.GLFW_KEY_V);
    }

    public void start(){

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for(Map.Entry<KeyMapping, Hack> e : km.entrySet()){
                if(e.getKey().consumeClick()){
                    // reverse teh switch
                    Hack temp = e.getValue();
                    temp.setOn(!temp.isOn());
                    System.out.println("this is the value: "+temp.getClass()+" "+temp.isOn());
                }
            }

        });
    }

    public void createKeyBind(Hack hack, String feature, int keyCode){
        KeyMapping temp = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.modid."+feature,
                InputConstants.Type.KEYSYM,
                keyCode,
                KeyMapping.Category.MISC
        ));
        km.put(temp, hack);
    }
}
