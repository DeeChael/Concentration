package net.deechael.concentration.fabric.compat;

import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import net.deechael.concentration.Concentration;
import net.minecraft.network.chat.Component;
import org.embeddedt.embeddium.api.OptionGroupConstructionEvent;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import org.embeddedt.embeddium.client.gui.options.StandardOptions;

/**
 * Make Embedddium fullscreen option follow Concentration function
 * @author DeeChael
 */
public class EmbeddiumCompat {

    public static void init() {
        OptionGroupConstructionEvent.BUS.addListener(event -> {
            if (event.getId() != null && event.getId().toString().equals(StandardOptions.Group.WINDOW.toString())) {
                var options = event.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    if (options.get(i).getId().toString().equals(StandardOptions.Option.FULLSCREEN.toString())) {
                        options.set(
                                i,
                                OptionImpl.createBuilder(Boolean.TYPE, new MinecraftOptionsStorage())
                                        .setId(StandardOptions.Option.FULLSCREEN)
                                        .setName(Component.translatable("options.fullscreen"))
                                        .setTooltip(Component.translatable("sodium.options.fullscreen.tooltip"))
                                        .setControl(TickBoxControl::new)
                                        .setBinding(Concentration::toggleFullScreenMode, (opts) -> opts.fullscreen().get()).build()
                        );
                        break;
                    }
                }
            }
        });
    }

}
