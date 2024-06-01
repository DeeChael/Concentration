package net.deechael.concentration.neoforge;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mojang.blaze3d.platform.Window;
import net.deechael.concentration.AttachMode;
import net.deechael.concentration.ConcentrationConstants;
import net.deechael.concentration.FullscreenMode;
import net.deechael.concentration.config.ConcentrationConfig;
import net.deechael.concentration.mixin.accessor.WindowAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;

public class NeoForgeConcentrationConfig implements ConcentrationConfig {

    public final static NeoForgeConcentrationConfig INSTANCE = new NeoForgeConcentrationConfig();

    public static final ModConfigSpec SPECS;
    public static final ModConfigSpec.EnumValue<FullscreenMode> FULL_SCREEN_MODE;
    public static final ModConfigSpec.EnumValue<AttachMode> ATTACH_MODE;

    private static boolean loaded = false;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("concentration");

        builder.push("general");

        FULL_SCREEN_MODE = builder.comment("Full screen mode to use", "WINDOWED for window mode", "FULLSCREEN for vanilla full screen mode", "BORDERLESS for borderless screen mode")
                .defineEnum("fullscreen", FullscreenMode.WINDOWED);

        ATTACH_MODE = builder
                .comment("The action when use the fullscreen key short.")
                .defineEnum("attach", AttachMode.ATTACH);

        builder.pop();

        builder.pop();

        SPECS = builder.build();
    }


    @Override
    public void ensureLoaded() {
        if (loaded)
            return;

        ConcentrationConstants.LOGGER.info("Loading Concentration Config");

        Path path = FMLPaths.CONFIGDIR.get().resolve("concentration-client.toml");
        CommentedFileConfig config = CommentedFileConfig.builder(path)
                .sync()
                .autosave()
                .writingMode(WritingMode.REPLACE)
                .build();
        config.load();
        SPECS.setConfig(config);

        loaded = true;
    }

    @Override
    public AttachMode getAttachMode() {
        return ATTACH_MODE.get();
    }

    @Override
    public FullscreenMode getFullScreenMode() {
        return FULL_SCREEN_MODE.get();
    }

    @Override
    public void setFullScreenMode(Options options, FullscreenMode fullScreenMode) {
        FULL_SCREEN_MODE.set(fullScreenMode);
        options.fullscreen().set(fullScreenMode != FullscreenMode.WINDOWED);

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

    private NeoForgeConcentrationConfig() {
    }

}
