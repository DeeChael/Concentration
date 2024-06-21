package net.deechael.concentration.config;

import net.deechael.concentration.FullscreenMode;

public interface Config {

    FullscreenMode getFullscreenMode();

    void setFullscreenMode(FullscreenMode fullscreenMode);

    void save();

}
