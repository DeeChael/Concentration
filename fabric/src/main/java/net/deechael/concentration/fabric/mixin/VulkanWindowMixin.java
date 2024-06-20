package net.deechael.concentration.fabric.mixin;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import net.deechael.concentration.ConcentrationConstants;
import net.deechael.concentration.fabric.ConcentrationFabricCaching;
import net.deechael.concentration.fabric.config.ConcentrationConfig;
import org.lwjgl.glfw.GLFW;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Borderless implementation
 * @author DeeChael
 */
@Mixin(value = Window.class, priority = 2000)
public abstract class VulkanWindowMixin {

    @Shadow
    private boolean fullscreen;

    @Inject(method = "onMove", at = @At("HEAD"))
    private void inject$onMove$head(long window, int x, int y, CallbackInfo ci) {
        if (!this.fullscreen) {
            if (!ConcentrationFabricCaching.concentration$cachedPos) {
                ConcentrationConstants.LOGGER.info("Window position has been cached");
            }
            ConcentrationFabricCaching.concentration$cachedPos = true;
            ConcentrationFabricCaching.concentration$cachedX = x;
            ConcentrationFabricCaching.concentration$cachedY = y;
        }
    }

    @Inject(method = "onResize", at = @At("HEAD"))
    private void inject$onResize$head(long window, int width, int height, CallbackInfo ci) {
        if (!this.fullscreen && !ConcentrationFabricCaching.concentration$cacheSizeLock) {
            if (!ConcentrationFabricCaching.concentration$cachedSize) {
                ConcentrationConstants.LOGGER.info("Window size has been cached");
            }
            ConcentrationFabricCaching.concentration$cachedSize = true;
            ConcentrationFabricCaching.concentration$cachedWidth = width;
            ConcentrationFabricCaching.concentration$cachedHeight = height;
        }
    }

}