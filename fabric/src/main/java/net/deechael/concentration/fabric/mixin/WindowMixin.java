package net.deechael.concentration.fabric.mixin;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import net.deechael.concentration.ConcentrationConstants;
import net.deechael.concentration.FullscreenMode;
import net.deechael.concentration.fabric.ConcentrationFabricCaching;
import net.deechael.concentration.fabric.config.ConcentrationConfigFabric;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Borderless implementation
 *
 * @author DeeChael
 */
@Mixin(Window.class)
public abstract class WindowMixin {

    @Shadow
    private boolean fullscreen;

    @Shadow
    @Final
    private ScreenManager screenManager;

    @Shadow
    private int x;
    @Shadow
    private int y;
    @Shadow
    private int width;
    @Shadow
    private int height;

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

    @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V"))
    private void redirect$glfwSetWindowMonitor(long window, long monitor, int xpos, int ypos, int width, int height, int refreshRate) {
        ConcentrationConstants.LOGGER.info("================= [Concentration Start] =================");
        ConcentrationConstants.LOGGER.info("Trying to modify window monitor");

        // Monitor is 0 means the game is windowed mode, so the expression means if is toggling to fullscreen
        if (monitor != 0L) {
            ConcentrationConstants.LOGGER.info("Modifying window size limits");
            GLFW.glfwSetWindowSizeLimits(window, 0, 0, width, height);
        }

        // Because whether in fullscreen mode or windowed mode
        // The final step is same
        // So I extracted the value then execute the final step
        int finalWidth;
        int finalHeight;

        int finalX;
        int finalY;

        if (this.fullscreen) {
            ConcentrationConfigFabric config = ConcentrationConfigFabric.getInstance();

            // If the game started with fullscreen mode, when switching to windowed mode, it will be forced to move to the primary monitor
            // Though size and position isn't be set at initialization, but I think the window should be at the initial monitor
            // So save the monitor and use the monitor value when the size isn't cached
            ConcentrationFabricCaching.lastMonitor = monitor;

            // Lock caching, because when switching back, the window will be once resized to the maximum value and the cache value will be wrong
            // Position won't be affected, so it doesn't need lock
            ConcentrationFabricCaching.cacheSizeLock = true;
            ConcentrationConstants.LOGGER.info("Locked size caching");

            if (config.fullscreen == FullscreenMode.NATIVE) {
                ConcentrationConstants.LOGGER.info("Fullscreen mode is native, apply now!");
                GLFW.glfwSetWindowMonitor(window, monitor, xpos, ypos, width, height, -1);
                ConcentrationConstants.LOGGER.info("================= [Concentration End] =================");
                return;
            }

            ConcentrationConstants.LOGGER.info("Trying to switch to borderless fullscreen mode");

            // Get the monitor the user want to use and get the relative position in the system
            // The monitor is always non-null because when switching fullscreen mode, there must be a monitor to put the window
            Monitor monitorInstance = this.screenManager.getMonitor(monitor);
            ConcentrationConstants.LOGGER.info("Current fullscreen monitor is {}", monitor);

            // Remove the title bar to prevent that user can see the title bar if they put their monitors vertically connected
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
            ConcentrationConstants.LOGGER.info("Trying to remove the title bar");

            if (ConcentrationConfigFabric.getInstance().customized) {
                ConcentrationConstants.LOGGER.info("Customization enabled, so replace the fullscreen size with customized size");
                finalX = config.x + (config.related ? monitorInstance.getX() : 0);
                finalY = config.y - (config.height == height ? 1 : 0) + (config.related ? monitorInstance.getY() : 0);
                finalWidth = config.width;
                finalHeight = config.height + (config.height == height ? 1 : 0);
            } else {
                // If we make the window not decorated and set the window size exactly the same with the screen size, it will become native fullscreen mode
                // to prevent this, I enlarge the height by 1 pixel and move up the window by 1 pixel which won't affect anything (unless you have a screen
                // which is added above the monitor which holds the game) and will have a good experience
                // Actually this is a little bit dirty, needs to find a better way to solve it
                finalX = monitorInstance.getX();
                finalY = monitorInstance.getY() - 1;
                finalWidth = width;
                finalHeight = height + 1;
            }

            this.x = finalX;
            this.y = finalY;
            this.width = finalWidth;
            this.height = finalHeight;
        } else {
            ConcentrationConstants.LOGGER.info("Trying to switch to windowed mode");

            // Re-add the title bar so user can move the window and minimize, maximize and close the window
            ConcentrationConstants.LOGGER.info("Trying to add title bar back");
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);

            ConcentrationConstants.LOGGER.info("Trying to use cached value to resize the window");

            // Make sure that Concentration has cached position and size, because position size won't be cached when the game starting in fullscreen mode
            finalWidth = ConcentrationFabricCaching.cachedSize ? ConcentrationFabricCaching.cachedWidth : width;
            finalHeight = ConcentrationFabricCaching.cachedSize ? ConcentrationFabricCaching.cachedHeight : height;

            // To make sure that even starting with fullscreen mode can also make the window stay at the current monitor
            // So I set two ways to ensure the position
            if (ConcentrationFabricCaching.cachedPos) {
                // If Concentration cached the pos, use the cached value
                finalX = ConcentrationFabricCaching.cachedX;
                finalY = ConcentrationFabricCaching.cachedY;
            } else if (ConcentrationFabricCaching.lastMonitor != -1) {
                // or else maybe the game started with fullscreen mode, so I don't need to care about the size
                // only need to make sure that the position is in the correct monitor
                Monitor monitorInstance = this.screenManager.getMonitor(ConcentrationFabricCaching.lastMonitor);
                VideoMode videoMode = monitorInstance.getCurrentMode();
                finalX = (videoMode.getWidth() - finalWidth) / 2;
                finalY = (videoMode.getHeight() - finalHeight) / 2;
            } else {
                // if both value are missed, use the default value to prevent errors
                finalX = xpos;
                finalY = ypos;
            }

            // Unlock caching, because user can change the window size now
            ConcentrationFabricCaching.cacheSizeLock = false;
            ConcentrationConstants.LOGGER.info("Unlocked size caching");
        }

        ConcentrationConstants.LOGGER.info("Window size: {}, {}, position: {}, {}", finalWidth, finalHeight, finalX, finalY);

        ConcentrationConstants.LOGGER.info("Trying to resize and reposition the window");
        GLFW.glfwSetWindowMonitor(window, 0L, finalX, finalY, finalWidth, finalHeight, -1);

        ConcentrationConstants.LOGGER.info("================= [Concentration End] =================");
    }

    @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwGetWindowMonitor(J)J"))
    private long redirect$glfwGetWindowMonitor(long window) {
        return 1L;
    }

}