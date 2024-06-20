package net.deechael.concentration.mixin.fabric;

import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import net.deechael.concentration.Concentration;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Hooks sodium options to make sure that changing fullscreen behaviour will use Concentration function instead of vanilla function
 * @author DeeChael
 */
@Mixin(SodiumGameOptionPages.class)
public class SodiumVideoOptionsScreenMixin {

    @Inject(method = "lambda$general$12", at = @At("HEAD"), cancellable = true)
    private static void inject$general(Options opts, Boolean value, CallbackInfo ci) {
        Concentration.toggleFullScreenMode(opts, value);
        ci.cancel();
    }

}
