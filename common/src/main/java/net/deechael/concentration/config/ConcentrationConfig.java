package net.deechael.concentration.config;

import net.deechael.concentration.AttachMode;
import net.deechael.concentration.FullScreenMode;
import net.minecraft.client.Options;

public interface ConcentrationConfig {

    void ensureLoaded();

    AttachMode getAttachMode();

    FullScreenMode getFullScreenMode();

    void setFullScreenMode(Options options, FullScreenMode fullScreenMode);

}
