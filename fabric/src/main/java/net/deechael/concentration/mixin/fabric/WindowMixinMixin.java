package net.deechael.concentration.mixin.fabric;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import net.deechael.concentration.ConcentrationCaching;
import net.deechael.concentration.ConcentrationConstants;
import net.deechael.concentration.mixin.accessor.WindowAccessor;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.vulkanmod.mixin.window.WindowMixin", remap = false)
public class WindowMixinMixin {

    @Inject(method = "method_4488", remap = false, at = @At("HEAD"))
    private void inject$onResize$head(long window, int width, int height, CallbackInfo ci) {
        if (!Minecraft.getInstance().getWindow().isFullscreen() && !ConcentrationCaching.concentration$cacheSizeLock) {
            if (!ConcentrationCaching.concentration$cachedSize) {
                ConcentrationConstants.LOGGER.info("Window size has been cached");
            }
            ConcentrationCaching.concentration$cachedSize = true;
            ConcentrationCaching.concentration$cachedWidth = width;
            ConcentrationCaching.concentration$cachedHeight = height;
        }
    }

    @Redirect(method = "method_4479", remap = false, at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V"))
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

        if (Minecraft.getInstance().getWindow().isFullscreen()) {
            ConcentrationConstants.LOGGER.info("Trying to switch to borderless fullscreen mode");

            // If the game started with fullscreen mode, when switching to windowed mode, it will be forced to move to the primary monitor
            // Though size and position isn't be set at initialization, but I think the window should be at the initial monitor
            // So save the monitor and use the monitor value when the size isn't cached
            ConcentrationCaching.concentration$lastMonitor = monitor;

            // Lock caching, because when switching back, the window will be once resized to the maximum value and the cache value will be wrong
            // Position won't be affected, so it doesn't need lock
            ConcentrationCaching.concentration$cacheSizeLock = true;

            ConcentrationConstants.LOGGER.info("Locked size caching");

            // Get the monitor the user want to use and get the relative position in the system
            // The monitor is always non-null because when switching fullscreen mode, there must be a monitor to put the window
            Monitor monitorInstance = ((WindowAccessor) (Object) Minecraft.getInstance().getWindow()).getScreenManager().getMonitor(monitor);
            ConcentrationConstants.LOGGER.info("Current fullscreen monitor is {}", monitor);

            // Remove the title bar to prevent that user can see the title bar if they put their monitors vertically connected
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
            ConcentrationConstants.LOGGER.info("Trying to remove the title bar");

            // If we make the window not decorated and set the window size exactly the same with the screen size, it will become native fullscreen mode
            // to prevent this, I enlarge the height by 1 pixel and move up the window by 1 pixel which won't affect anything (unless you have a screen
            // which is added above the monitor which holds the game) and will have a good experience
            // Actually this is a little bit dirty, needs to find a better way to solve it
            finalWidth = width;
            finalHeight = height + 1;

            finalX = monitorInstance.getX();
            finalY = monitorInstance.getY() - 1;
        } else {
            ConcentrationConstants.LOGGER.info("Trying to switch to windowed mode");

            // Re-add the title bar so user can move the window and minimize, maximize and close the window
            ConcentrationConstants.LOGGER.info("Trying to add title bar back");
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);

            ConcentrationConstants.LOGGER.info("Trying to use cached value to resize the window");

            // Make sure that Concentration has cached position and size, because position size won't be cached when the game starting in fullscreen mode
            finalWidth = ConcentrationCaching.concentration$cachedSize ? ConcentrationCaching.concentration$cachedWidth : width;
            finalHeight = ConcentrationCaching.concentration$cachedSize ? ConcentrationCaching.concentration$cachedHeight : height;

            // To make sure that even starting with fullscreen mode can also make the window stay at the current monitor
            // So I set two ways to ensure the position
            if (ConcentrationCaching.concentration$cachedPos) {
                // If Concentration cached the pos, use the cached value
                finalX = ConcentrationCaching.concentration$cachedX;
                finalY = ConcentrationCaching.concentration$cachedY;
            } else if (ConcentrationCaching.concentration$lastMonitor != -1) {
                // or else maybe the game started with fullscreen mode, so I don't need to care about the size
                // only need to make sure that the position is in the correct monitor
                Monitor monitorInstance = ((WindowAccessor) (Object) Minecraft.getInstance().getWindow()).getScreenManager().getMonitor(ConcentrationCaching.concentration$lastMonitor);
                VideoMode videoMode = monitorInstance.getCurrentMode();
                finalX = (videoMode.getWidth() - finalWidth) / 2;
                finalY = (videoMode.getHeight() - finalHeight) / 2;
            } else {
                // if both value are missed, use the default value to prevent errors
                finalX = xpos;
                finalY = ypos;
            }

            // Unlock caching, because user can change the window size now
            ConcentrationCaching.concentration$cacheSizeLock = false;
            ConcentrationConstants.LOGGER.info("Unlocked size caching");
        }

        ConcentrationConstants.LOGGER.info("Window size: {}, {}, position: {}, {}", finalWidth, finalHeight, finalX, finalY);

        ConcentrationConstants.LOGGER.info("Trying to resize and reposition the window");
        GLFW.glfwSetWindowMonitor(window, 0L, finalX, finalY, finalWidth, finalHeight, -1);

        ConcentrationConstants.LOGGER.info("================= [Concentration End] =================");
    }

}
