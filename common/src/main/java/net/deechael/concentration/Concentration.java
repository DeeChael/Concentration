package net.deechael.concentration;

import com.mojang.blaze3d.platform.Window;
import net.deechael.concentration.mixin.accessor.WindowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

/**
 * Main class of Concentration
 *
 * @author DeeChael
 */
public class Concentration {

    /**
     * Initialize the mod
     */
    public static void init() {
        ConcentrationConstants.LOGGER.info("Welcome to borderless world! Concentrate on your game!");
    }

    /**
     * Toggle fullscreen mode
     *
     * @param options should be Minecraft vanilla options
     * @param value   whether fullscreen is turned on
     */
    public static void toggleFullScreenMode(Options options, boolean value) {
        options.fullscreen().set(value);

        Window window = Minecraft.getInstance().getWindow();

        if (window.isFullscreen() != options.fullscreen().get()) {
            window.toggleFullScreen();
            options.fullscreen().set(window.isFullscreen());
        }

        if (options.fullscreen().get()) {
            ((WindowAccessor) (Object) window).setDirty(true);
            window.changeFullscreenVideoMode();
        }
    }

}