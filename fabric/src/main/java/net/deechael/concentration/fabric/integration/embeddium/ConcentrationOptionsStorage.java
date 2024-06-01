package net.deechael.concentration.fabric.integration.embeddium;

import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import net.deechael.concentration.fabric.ConcentrationFabric;

public class ConcentrationOptionsStorage implements OptionStorage<Object> {

    @Override
    public Object getData() {
        return new Object();
    }

    @Override
    public void save() {
        ConcentrationFabric.CONFIG.save();
    }

}
