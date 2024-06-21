package net.deechael.concentration.fabric.mixin;

import com.mojang.blaze3d.platform.Window;
import net.deechael.concentration.ConcentrationConstants;
import net.deechael.concentration.fabric.ConcentrationFabricCaching;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Borderless implementation
 *
 * @author DeeChael
 */
@Mixin(value = Window.class, priority = 2000)
public abstract class VulkanWindowMixin {

    @Shadow
    private boolean fullscreen;

    @Inject(method = "onMove", at = @At("HEAD"))
    private void inject$onMove$head(long window, int x, int y, CallbackInfo ci) {
        if (!this.fullscreen) {
            if (!ConcentrationFabricCaching.cachedPos) {
                ConcentrationConstants.LOGGER.info("Window position has been cached");
            }
            ConcentrationFabricCaching.cachedPos = true;
            ConcentrationFabricCaching.cachedX = x;
            ConcentrationFabricCaching.cachedY = y;
        }
    }

    @Inject(method = "onResize", at = @At("HEAD"))
    private void inject$onResize$head(long window, int width, int height, CallbackInfo ci) {
        if (!this.fullscreen && !ConcentrationFabricCaching.cacheSizeLock) {
            if (!ConcentrationFabricCaching.cachedSize) {
                ConcentrationConstants.LOGGER.info("Window size has been cached");
            }
            ConcentrationFabricCaching.cachedSize = true;
            ConcentrationFabricCaching.cachedWidth = width;
            ConcentrationFabricCaching.cachedHeight = height;
        }
    }

}