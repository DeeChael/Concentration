package net.deechael.concentration.mixin.fabric;

import com.mojang.blaze3d.platform.Window;
import net.deechael.concentration.ConcentrationCaching;
import net.deechael.concentration.ConcentrationConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {

    @Shadow
    private boolean fullscreen;

    @Inject(method = "onMove", at = @At("HEAD"))
    private void inject$onMove$head(long window, int x, int y, CallbackInfo ci) {
        if (!this.fullscreen) {
            if (!ConcentrationCaching.concentration$cachedPos) {
                ConcentrationConstants.LOGGER.info("Window position has been cached");
            }
            ConcentrationCaching.concentration$cachedPos = true;
            ConcentrationCaching.concentration$cachedX = x;
            ConcentrationCaching.concentration$cachedY = y;
        }
    }

}
