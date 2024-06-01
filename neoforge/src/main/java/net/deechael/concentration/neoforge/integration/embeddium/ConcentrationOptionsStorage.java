package net.deechael.concentration.neoforge.integration.embeddium;

import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import net.deechael.concentration.neoforge.NeoForgeConcentrationConfig;

public class ConcentrationOptionsStorage implements OptionStorage<Object> {

    @Override
    public Object getData() {
        return new Object();
    }

    @Override
    public void save() {
        NeoForgeConcentrationConfig.SPECS.save();
    }

}
