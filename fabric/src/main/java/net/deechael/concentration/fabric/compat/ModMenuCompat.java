package net.deechael.concentration.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.deechael.concentration.Concentration;
import net.deechael.concentration.ConcentrationConstants;
import net.deechael.concentration.config.ConcentrationConfigScreen;
import net.deechael.concentration.fabric.config.ConcentrationConfigFabric;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.Component;

public class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConcentrationConfigFabric configHandler = ConcentrationConfigFabric.getInstance();

            return new ConcentrationConfigScreen(Component.literal(ConcentrationConstants.MOD_NAME), parent) {
                @Override
                public void save() {
                    configHandler.save();
                    Concentration.toggleFullScreenMode(Minecraft.getInstance().options, Minecraft.getInstance().options.fullscreen().get());
                }

                @Override
                public void addElements() {
                    addOption(OptionInstance.createBoolean("concentration.config.customization.enabled",
                            configHandler.customized,
                            value -> configHandler.customized = value));
                    addOption(OptionInstance.createBoolean("concentration.config.customization.related",
                            configHandler.related,
                            value -> configHandler.related = value));

                    addIntField(Component.translatable("concentration.config.customization.x"),
                            () -> configHandler.x,
                            value -> configHandler.x = value);
                    addIntField(Component.translatable("concentration.config.customization.y"),
                            () -> configHandler.y,
                            value -> configHandler.y = value);
                    addIntField(Component.translatable("concentration.config.customization.width"),
                            () -> configHandler.width,
                            value -> configHandler.width = value > 0 ? value : 1);
                    addIntField(Component.translatable("concentration.config.customization.height"),
                            () -> configHandler.height,
                            value -> configHandler.height = value > 0 ? value : 1);
                }
            };
        };
    }

}
