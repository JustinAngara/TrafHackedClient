package com.traf.mixin.client;

import com.traf.hacks.AntiHunger;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener {


    @Redirect(
            method = "handleSetHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/food/FoodData;setFoodLevel(I)V"
            )
    )
    private void traf_antihunger$skipFoodLevelSync(FoodData fd, int food) {
        if (!AntiHunger.isActive()) {
            fd.setFoodLevel(food);
        }
    }

    @Redirect(
            method = "handleSetHealth",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/food/FoodData;setSaturation(F)V"
            )
    )
    private void traf_antihunger$skipSaturationSync(FoodData fd, float sat) {
        if (!AntiHunger.isActive()) {
            fd.setSaturation(sat);
        }
    }
}