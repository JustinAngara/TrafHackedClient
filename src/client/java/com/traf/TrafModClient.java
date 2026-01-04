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

import java.awt.*;

public class TrafModClient implements ClientModInitializer {

	// this will be the lifetime of the world, not the indivdual hack runnign by itself
	private static long gameTicks=0;
	private static HackManager hm;
	private static Minecraft mc;
	private static KeyListenerManager klm;
	private static float lastHealth = 20.0f;
	private Display d;

	@Override
	public void onInitializeClient() {
		mc = Minecraft.getInstance();
		hm = new HackManager();


		// add the keybinds
		klm = new KeyListenerManager(hm);
		klm.start();


		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			TrafMod.LOGGER.info("Joined a world session");

		});
		// add disply stuff
		addDisplay();

		// internally lock onto game tick
		lockToGameTick();
	}

	public void addDisplay(){
		d = new Display(mc);
		HudElementRegistry.addLast(
			// this gets repeatedly called per tick
			Identifier.fromNamespaceAndPath(TrafMod.MOD_ID, "hack_list"),(guiGraphics, deltaTracker)->{
				d.run(guiGraphics);
			}
		);
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
			hm.run(lp);
			gameTicks++;
		});


		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			float currentHealth = client.player.getHealth();

			if (currentHealth < lastHealth) {
				System.out.println("You got damaged: "+currentHealth);
			}

			lastHealth = currentHealth;
		});
	}

	public static Minecraft getMinecraft(){ return mc; }
}
