package net.deechael.concentration.neoforge.compat;

import net.deechael.concentration.Concentration;
import net.deechael.concentration.ConcentrationConstants;
import net.deechael.concentration.FullscreenMode;
import net.deechael.concentration.neoforge.config.ConcentrationConfigNeoForge;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.api.OptionGroupConstructionEvent;
import org.embeddedt.embeddium.api.options.control.CyclingControl;
import org.embeddedt.embeddium.api.options.control.TickBoxControl;
import org.embeddedt.embeddium.api.options.storage.MinecraftOptionsStorage;
import org.embeddedt.embeddium.api.options.structure.OptionImpl;
import org.embeddedt.embeddium.api.options.structure.StandardOptions;

/**
 * Make Embedddium fullscreen option follow Concentration function
 *
 * @author DeeChael
 */
public class EmbeddiumCompat {

    public static void init() {
        OptionGroupConstructionEvent.BUS.addListener(event -> {
            if (event.getId() != null && event.getId().toString().equals(StandardOptions.Group.WINDOW.toString())) {
                var options = event.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    if (options.get(i).getId().toString().equals(StandardOptions.Option.FULLSCREEN.toString())) {
                        options.add(i, OptionImpl.createBuilder(FullscreenMode.class, MinecraftOptionsStorage.INSTANCE)
                                .setId(ResourceLocation.fromNamespaceAndPath(ConcentrationConstants.MOD_ID, "fullscreen_mode"))
                                .setName(Component.translatable("concentration.option.fullscreen_mode"))
                                .setTooltip(Component.translatable("concentration.option.fullscreen_mode.tooltip"))
                                .setControl((opt) -> new CyclingControl<>(opt, FullscreenMode.class, new Component[]{
                                        Component.translatable("concentration.option.fullscreen_mode.borderless"),
                                        Component.translatable("concentration.option.fullscreen_mode.native")
                                }))
                                .setBinding((vanillaOpts, value) -> {
                                            ConcentrationConfigNeoForge.FULLSCREEN.set(value);
                                            ConcentrationConfigNeoForge.SPECS.save();
                                            if (vanillaOpts.fullscreen().get()) {
                                                // If fullscreen turns on, re-toggle to changing the fullscreen mode instantly
                                                Concentration.toggleFullScreenMode(vanillaOpts, true);
                                            }
                                        },
                                        (vanillaOpts) -> ConcentrationConfigNeoForge.FULLSCREEN.get()
                                )
                                .build());
                        options.set(
                                i + 1,
                                OptionImpl.createBuilder(Boolean.TYPE, MinecraftOptionsStorage.INSTANCE)
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
