package net.deechael.concentration.platform.services;

import net.deechael.concentration.config.ConcentrationConfig;

public interface IPlatformHelper {

    ConcentrationConfig getConfig();

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

}