package net.deechael.concentration.neoforge.integration;

import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import net.deechael.concentration.ConcentrationConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import org.embeddedt.embeddium.api.OptionGroupConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.StandardOptions;

import java.util.List;

@EventBusSubscriber(modid = ConcentrationConstants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ConcentrationOptions {

    public static final OptionStorage<?> STORAGE = new ConcentrationOptionsStorage();

    @SubscribeEvent
    public static void event(OptionGUIConstructionEvent event) {
        List<OptionPage> pages = event.getPages();
        pages.add(new ConcentrationOptionPage());
    }

    @SubscribeEvent
    public static void event(OptionGroupConstructionEvent event) {
        if (event.getId() != null && event.getId().toString().equals(StandardOptions.Group.WINDOW.toString())) {
            List<Option<?>> options = event.getOptions();
            for (int i = 0; i < options.size(); i++) {
                if (options.get(i).getId().toString().equals(StandardOptions.Option.FULLSCREEN.toString())) {
                    options.remove(i);
                    break;
                }
            }
        }
    }

}
