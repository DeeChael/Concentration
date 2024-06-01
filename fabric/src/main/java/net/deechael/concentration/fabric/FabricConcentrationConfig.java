package net.deechael.concentration.fabric;

import com.mojang.blaze3d.platform.Window;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import net.deechael.concentration.AttachMode;
import net.deechael.concentration.FullscreenMode;
import net.deechael.concentration.config.ConcentrationConfig;
import net.deechael.concentration.mixin.accessor.WindowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

@Config(name = "concentration-client")
public class FabricConcentrationConfig implements ConfigData, ConcentrationConfig {

    public FullscreenMode fullScreenMode = FullscreenMode.WINDOWED;
    public AttachMode attachMode = AttachMode.ATTACH;

    @Override
    public void ensureLoaded() {
        // because the config is loaded in the main class, so we don't have to ensure the config is loaded right here
    }

    @Override
    public AttachMode getAttachMode() {
        return attachMode;
    }

    @Override
    public FullscreenMode getFullScreenMode() {
        return fullScreenMode;
    }

    @Override
    public void setFullScreenMode(Options options, FullscreenMode fullScreenMode) {
        this.fullScreenMode = fullScreenMode;
        this.save(); // because cloth config and neoforge mod config spec are different in read and write,
                     // neoforge mod config spec and automatically read and write,
                     // but cloth config can't, so I have to save the config manually

        options.fullscreen().set(fullScreenMode != FullscreenMode.WINDOWED); // because vanilla fullscreen is a boolean,
                                                                             // but with this mod we have to fullscreen mode

        Window window = Minecraft.getInstance().getWindow();

        if (window.isFullscreen() != options.fullscreen().get()) {
            window.toggleFullScreen();
            options.fullscreen().set(window.isFullscreen());
        }

        if (options.fullscreen().get()) {
            ((WindowAccessor) (Object) window).setDirty(true);
            window.changeFullscreenVideoMode();
        }
    }

    public void save() {
        ConfigHolder<FabricConcentrationConfig> holder = AutoConfig.getConfigHolder(FabricConcentrationConfig.class);
        holder.setConfig(this);
        holder.save();
    }

}
