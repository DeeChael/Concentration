package net.deechael.concentration.mixin;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.Window;
import org.lwjgl.glfw.GLFW;
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
@Mixin(Window.class)
public abstract class WindowMixin {

    @Shadow
    private boolean fullscreen;

    @Shadow
    @Final
    private ScreenManager screenManager;

    @Unique
    private boolean concentration$cachedSize = false;
    @Unique
    private boolean concentration$cachedPos = false;
    @Unique
    private boolean concentration$cacheSizeLock = false;

    @Unique
    private int concentration$cachedX = 0;
    @Unique
    private int concentration$cachedY = 0;
    @Unique
    private int concentration$cachedWidth = 0;
    @Unique
    private int concentration$cachedHeight = 0;

    @Inject(method = "onMove", at = @At("HEAD"))
    private void inject$onMove$head(long window, int x, int y, CallbackInfo ci) {
        if (!this.fullscreen) {
            this.concentration$cachedPos = true;
            this.concentration$cachedX = x;
            this.concentration$cachedY = y;
        }
    }

    @Inject(method = "onResize", at = @At("HEAD"))
    private void inject$onResize$head(long window, int width, int height, CallbackInfo ci) {
        if (!this.fullscreen && !this.concentration$cacheSizeLock) {
            this.concentration$cachedSize = true;
            this.concentration$cachedWidth = width;
            this.concentration$cachedHeight = height;
        }
    }

    @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V"))
    private void redirect$glfwSetWindowMonitor(long window, long monitor, int xpos, int ypos, int width, int height, int refreshRate) {
        if (monitor != 0L) {
            GLFW.glfwSetWindowSizeLimits(window, 0, 0, width, height);
        }

        if (this.fullscreen) {
            // Lock caching, because when switching back, the window will be once resized to the maximum value and the cache value will be wrong
            // Position won't be affected, so it doesn't need lock
            this.concentration$cacheSizeLock = true;

            // Get the monitor the user want to use and get the relative position in the system
            // The monitor is always non-null because when switching fullscreen mode, there must be a monitor to put the window
            Monitor monitorInstance = this.screenManager.getMonitor(monitor);

            // Remove the title bar to prevent that user can see the title bar if they put their monitors vertically connected
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);

            // If we make the window not decorated and set the window size exactly the same with the screen size, it will become native fullscreen mode
            // to prevent this, I enlarge the height by 1 pixel and move up the window by 1 pixel which won't affect anything (unless you have a screen
            // which is added above the monitor which holds the game) and will have a good experience
            // Actually this is a little bit dirty, needs to find a better way to solve it
            GLFW.glfwSetWindowMonitor(window, 0L, monitorInstance.getX(), monitorInstance.getY() - 1, width, height + 1, -1);
        } else {
            // Re-add the title bar so user can move the window and minimize, maximize and close the window
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);

            // Make sure that Concentration has cached position and size, because position size won't be cached when the game starting in fullscreen mode
            int finalX = concentration$cachedPos ? concentration$cachedX : xpos;
            int finalY = concentration$cachedPos ? concentration$cachedY : ypos;

            int finalWidth = concentration$cachedSize ? concentration$cachedWidth : width;
            int finalHeight = concentration$cachedSize ? concentration$cachedHeight : height;

            // Unlock caching, because user can change the window size now
            this.concentration$cacheSizeLock = false;

            GLFW.glfwSetWindowMonitor(window, 0L, finalX, finalY, finalWidth, finalHeight, -1);
        }
    }

    @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwGetWindowMonitor(J)J"))
    private long redirect$glfwGetWindowMonitor(long window) {
        return 1L;
    }

}