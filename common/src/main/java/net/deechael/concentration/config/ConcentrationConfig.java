package net.deechael.concentration.config;

import net.deechael.concentration.AttachMode;
import net.deechael.concentration.FullscreenMode;
import net.minecraft.client.Options;

public interface ConcentrationConfig {

    // Make sure that config is loaded so that we can read and write
    void ensureLoaded();

    // Get the attach mode user selected
    AttachMode getAttachMode();

    // get the fullscreen mode user selected
    FullscreenMode getFullScreenMode();

    // Save the fullscreen mode to config and change the window view at the same time
    void setFullScreenMode(Options options, FullscreenMode fullScreenMode);

}
