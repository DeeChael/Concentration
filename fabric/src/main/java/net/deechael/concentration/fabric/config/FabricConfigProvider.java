package net.deechael.concentration.fabric.config;

import net.deechael.concentration.config.Config;
import net.deechael.concentration.config.ConfigProvider;

public class FabricConfigProvider implements ConfigProvider {

    @Override
    public Config ensureLoaded() {
        return ConcentrationConfigFabric.getInstance();
    }

}
