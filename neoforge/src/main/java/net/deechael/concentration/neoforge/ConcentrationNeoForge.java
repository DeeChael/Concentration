package net.deechael.concentration.neoforge;

import net.deechael.concentration.Concentration;
import net.deechael.concentration.ConcentrationConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = ConcentrationConstants.MOD_ID, dist = Dist.CLIENT)
public class ConcentrationNeoForge {

    public ConcentrationNeoForge(IEventBus eventBus) {
        Concentration.init();
    }

}