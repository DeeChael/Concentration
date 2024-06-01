package net.deechael.concentration.fabric.integration.embeddium;

import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import org.embeddedt.embeddium.api.OptionGroupConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.StandardOptions;

import java.util.List;

public class ConcentrationOptions {

    public static final OptionStorage<?> STORAGE = new ConcentrationOptionsStorage();

    public static void init() {
        OptionGUIConstructionEvent.BUS.addListener(event -> {
            List<OptionPage> pages = event.getPages();
            pages.add(new ConcentrationOptionPage());
        });
        OptionGroupConstructionEvent.BUS.addListener(event -> {
            if (event.getId() != null && event.getId().toString().equals(StandardOptions.Group.WINDOW.toString())) {
                List<Option<?>> options = event.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    if (options.get(i).getId().toString().equals(StandardOptions.Option.FULLSCREEN.toString())) {
                        options.remove(i);
                        break;
                    }
                }
            }
        });
    }
}
