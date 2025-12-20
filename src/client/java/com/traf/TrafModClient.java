package com.traf;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;

import org.lwjgl.glfw.GLFW;

public class TrafModClient implements ClientModInitializer {

	private static long gameTicks=0;
	private static KeyMapping swingKey;
	private static HackManager hm;
	private static int autoSwingCooldownTicks = 0;


	private static Minecraft mc;
	private Display d;

	@Override
	public void onInitializeClient() {
		mc = Minecraft.getInstance();
		hm = new HackManager();
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			TrafMod.LOGGER.info("Joined a world session");
		});

		// add the keybinds
		addKeybinds();

		// add disply stuff
		addDisplay();

		// internally lock onto game tick
		lockToGameTick();
	}

	public void addDisplay(){
		d = new Display(mc);
		HudElementRegistry.addLast(
				Identifier.fromNamespaceAndPath(TrafMod.MOD_ID, "health_overlay"),(guiGraphics, deltaTracker)->{
					d.run(guiGraphics);
				});
	}
	public static long getGameTicks() { return gameTicks; }
	public static void lockToGameTick(){
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			gameTicks = 0;
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			gameTicks = 0;
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			if (client.isPaused()) return;
			LocalPlayer lp = client.player;
			hm.run(lp, mc);
			gameTicks++;
		});
	}

	public void addKeybinds(){
		// add the keybinds
		swingKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.modid.swing",
				InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				KeyMapping.Category.MISC
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			// Cooldown tick-down
			if (autoSwingCooldownTicks > 0) {
				autoSwingCooldownTicks--;
			}

			// Manual swing key
			while (swingKey.consumeClick()) {
				if (client.player != null) {
					client.player.swing(InteractionHand.MAIN_HAND);
				}
			}
		});


	}
}
