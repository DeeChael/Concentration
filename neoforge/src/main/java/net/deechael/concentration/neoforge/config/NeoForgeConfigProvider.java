package net.deechael.concentration.neoforge.config;

import net.deechael.concentration.config.Config;
import net.deechael.concentration.config.ConfigProvider;

public class NeoForgeConfigProvider implements ConfigProvider {

    @Override
    public Config ensureLoaded() {
        return ConcentrationConfigNeoForge.ensureLoaded();
    }

}
