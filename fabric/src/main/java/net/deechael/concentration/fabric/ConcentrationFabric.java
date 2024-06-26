package net.deechael.concentration.fabric;

import net.deechael.concentration.Concentration;
import net.fabricmc.api.ClientModInitializer;

/**
 * Main class for fabric version of Concentration
 *
 * @author DeeChael
 */
public class ConcentrationFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Concentration.init();
    }

}
