package net.deechael.concentration.neoforge;

import net.deechael.concentration.Concentration;
import net.deechael.concentration.ConcentrationConstants;
import net.deechael.concentration.config.ConcentrationConfigScreen;
import net.deechael.concentration.neoforge.compat.EmbeddiumCompat;
import net.deechael.concentration.neoforge.config.ConcentrationConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;

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

        if (FMLEnvironment.dist.isClient()) {
            ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> new IConfigScreenFactory() {
                @Override
                public @NotNull Screen createScreen(@NotNull Minecraft minecraft, @NotNull Screen parent) {
                    return new ConcentrationConfigScreen(Component.literal(ConcentrationConstants.MOD_NAME), parent) {

                        @Override
                        public void save() {
                            ConcentrationConfig.SPECS.save();
                            Concentration.toggleFullScreenMode(minecraft.options, minecraft.options.fullscreen().get());
                        }

                        @Override
                        public void addElements() {
                            addOption(OptionInstance.createBoolean("concentration.config.customization.enabled",
                                    ConcentrationConfig.CUSTOMIZED.get(),
                                    ConcentrationConfig.CUSTOMIZED::set));
                            addOption(OptionInstance.createBoolean("concentration.config.customization.related",
                                    ConcentrationConfig.RELATED.get(),
                                    ConcentrationConfig.RELATED::set));

                            addIntField(Component.translatable("concentration.config.customization.x"),
                                    ConcentrationConfig.X,
                                    ConcentrationConfig.X::set);
                            addIntField(Component.translatable("concentration.config.customization.y"),
                                    ConcentrationConfig.Y,
                                    ConcentrationConfig.Y::set);
                            addIntField(Component.translatable("concentration.config.customization.width"),
                                    ConcentrationConfig.WIDTH,
                                    ConcentrationConfig.WIDTH::set);
                            addIntField(Component.translatable("concentration.config.customization.height"),
                                    ConcentrationConfig.HEIGHT,
                                    ConcentrationConfig.HEIGHT::set);
                        }
                    };
                }
            });
        }
    }

}