package com.rld.createtfa.common.mixin;

import com.tterrag.registrate.AbstractRegistrate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractRegistrate.class)
public class AbstractRegistrateMixin {

    //Used to bypass exceptions in datagen relating to blockentities/models in production
    @Inject(method = "isDevEnvironment()Z", at = @At("HEAD"), cancellable = true)
    private static void onIsDevEnvironment(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
        cir.cancel();
    }
}
