package net.deechael.concentration.mixin;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

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

    @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowMonitor(JJIIIII)V"))
    private void redirect$glfwSetWindowMonitor(long window, long monitor, int xpos, int ypos, int width, int height, int refreshRate) {
        if (monitor != 0L) {
            GLFW.glfwSetWindowSizeLimits(window, 0, 0, width, height);
        }

        if (this.fullscreen) {
            Monitor monitorInstance = this.screenManager.getMonitor(monitor);
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);
            // If we make the window not decorated and set the window size exactly the same with the screen size, it will become native fullscreen mode
            // to prevent this, I enlarge the height by 1 pixel and move up the window by 1 pixel which won't affect anything (unless you have a screen
            // which is added above the monitor which holds the game) and will have a good experience
            // Actually this is a little bit dirty, needs to find a better way to solve it
            GLFW.glfwSetWindowMonitor(window, 0L, monitorInstance.getX(), monitorInstance.getY() - 1, width, height + 1, -1);
        } else {
            GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);
            GLFW.glfwSetWindowMonitor(window, 0L, xpos, ypos, width, height, refreshRate);
        }
    }

    @Redirect(method = "setMode", at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/glfw/GLFW;glfwGetWindowMonitor(J)J"))
    private long redirect$glfwGetWindowMonitor(long window) {
        return 1L;
    }

    // Pretend to the constructor code (that creates the window) that it is not fullscreen
    @Redirect(method = "<init>",
            at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/Window;fullscreen:Z", opcode = 0xb4),
            // currentFullscreen still needs to be set correctly
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lcom/mojang/blaze3d/platform/Window;actuallyFullscreen:Z",
                            opcode = 0xb5,
                            ordinal = 1)
            )
    )
    private boolean constructorIsFullscreen(Window window) {
        return fullscreen;
    }

}