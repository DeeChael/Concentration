package net.deechael.concentration.neoforge;

import net.deechael.concentration.Concentration;
import net.deechael.concentration.ConcentrationConstants;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(ConcentrationConstants.MOD_ID)
public class ConcentrationNeoForge {

    public ConcentrationNeoForge(IEventBus eventBus) {
        Concentration.init();
    }

}