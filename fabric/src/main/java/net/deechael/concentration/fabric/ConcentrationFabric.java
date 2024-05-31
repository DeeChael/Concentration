package net.deechael.concentration.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.deechael.concentration.fabric.integration.ConcentrationOptions;
import net.fabricmc.api.ClientModInitializer;

public class ConcentrationFabric implements ClientModInitializer {

    public static FabricConcentrationConfig CONFIG;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(FabricConcentrationConfig.class, Toml4jConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(FabricConcentrationConfig.class).getConfig();

        ConcentrationOptions.init();
    }

}
