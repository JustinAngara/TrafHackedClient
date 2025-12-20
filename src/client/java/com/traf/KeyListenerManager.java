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

    public KeyListenerManager() throws AWTException {
        km = new HashMap<>();

        // create the key binds here
        createKeyBind(new ViewLockHack(), "lock", GLFW.GLFW_KEY_R );
        createKeyBind(new HitHack(), "HIT", GLFW.GLFW_KEY_V );
    }

    public void start(){

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for(Map.Entry<KeyMapping, Hack> e : km.entrySet()){
                if(e.getKey().consumeClick()){
                    System.out.println("this is assuming this shit works");
                    e.getValue().run(client.player);
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
