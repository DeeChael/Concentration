package net.deechael.concentration.fabric;

import com.mojang.blaze3d.platform.Window;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import net.deechael.concentration.AttachMode;
import net.deechael.concentration.FullScreenMode;
import net.deechael.concentration.config.ConcentrationConfig;
import net.deechael.concentration.mixin.accessors.MainWindowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

@Config(name = "concentration-client")
public class FabricConcentrationConfig implements ConfigData, ConcentrationConfig {

    public FullScreenMode fullScreenMode = FullScreenMode.WINDOWED;
    public AttachMode attachMode = AttachMode.ATTACH;

    @Override
    public void ensureLoaded() {
    }

    @Override
    public AttachMode getAttachMode() {
        return attachMode;
    }

    @Override
    public FullScreenMode getFullScreenMode() {
        return fullScreenMode;
    }

    @Override
    public void setFullScreenMode(Options options, FullScreenMode fullScreenMode) {
        this.fullScreenMode = fullScreenMode;
        this.save();

        options.fullscreen().set(fullScreenMode != FullScreenMode.WINDOWED);

        Minecraft client = Minecraft.getInstance();
        Window window = client.getWindow();

        if (window.isFullscreen() != options.fullscreen().get()) {
            window.toggleFullScreen();
            options.fullscreen().set(window.isFullscreen());
        }

        if (options.fullscreen().get()) {
            ((MainWindowAccessor) (Object) window).setDirty(true);
            window.changeFullscreenVideoMode();
        }
    }

    public void save() {
        ConfigHolder<FabricConcentrationConfig> holder = AutoConfig.getConfigHolder(FabricConcentrationConfig.class);
        holder.setConfig(this);
        holder.save();
    }

}
