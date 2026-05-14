package com.traf.mixin.client;

import com.traf.hacks.AntiHunger;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class MixinFoodData {

    @Inject(method = "addExhaustion", at = @At("HEAD"), cancellable = true)
    private void traf_antihunger$skipExhaustion(float amount, CallbackInfo ci) {
        if (AntiHunger.isActive()) {
            ci.cancel();
        }
    }
}