package net.deechael.concentration.neoforge;

import net.deechael.concentration.Concentration;
import net.deechael.concentration.ConcentrationConstants;
import net.deechael.concentration.neoforge.compat.EmbeddiumCompat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;

/**
 * Mod entrance for NeoForge of Concentration
 * @author DeeChael
 */
@Mod(value = ConcentrationConstants.MOD_ID, dist = Dist.CLIENT)
public class ConcentrationNeoForge {

    public ConcentrationNeoForge(IEventBus eventBus) {
        Concentration.init();

        if (ModList.get().isLoaded("embeddium")) {
            EmbeddiumCompat.init();
        }
    }

}