package net.deechael.concentration.mixin;

import net.deechael.concentration.Concentration;
import net.deechael.concentration.FullscreenMode;
import net.deechael.concentration.config.ConfigProvider;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

/**
 * Make vanilla fullscreen option follow Concentration function
 *
 * @author DeeChael
 */
@Mixin(VideoSettingsScreen.class)
public abstract class VideoSettingsScreenMixin extends OptionsSubScreen {

    public VideoSettingsScreenMixin(Screen parent, Options options, Component component) {
        super(parent, options, component);
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void inject$removed(CallbackInfo ci) {
        this.options.save(); // fix that the options won't save when exit options screen by pressing ESC
    }

    @Inject(method = "options", at = @At("HEAD"), cancellable = true)
    private static void inject$options(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        cir.setReturnValue(
                new OptionInstance[]{
                        options.graphicsMode(),
                        options.renderDistance(),
                        options.prioritizeChunkUpdates(),
                        options.simulationDistance(),
                        options.ambientOcclusion(),
                        options.framerateLimit(),
                        options.enableVsync(),
                        options.bobView(),
                        options.guiScale(),
                        options.attackIndicator(),
                        options.gamma(),
                        options.cloudStatus(),
                        concentration$FullscreenMode(options),
                        concentration$wrapperFullscreen(options),
                        options.particles(),
                        options.mipmapLevels(),
                        options.entityShadows(),
                        options.screenEffectScale(),
                        options.entityDistanceScaling(),
                        options.fovEffectScale(),
                        options.showAutosaveIndicator(),
                        options.glintSpeed(),
                        options.glintStrength(),
                        options.menuBackgroundBlurriness()
                }
        );
    }

    @Unique
    private static OptionInstance<FullscreenMode> concentration$FullscreenMode(Options options) {
        return new OptionInstance<FullscreenMode>(
                "concentration.option.fullscreen_mode",
                OptionInstance.noTooltip(),
                OptionInstance.forOptionEnum(),
                new OptionInstance.Enum(Arrays.asList(FullscreenMode.values()), HumanoidArm.CODEC),
                ConfigProvider.INSTANCE.ensureLoaded().getFullscreenMode(),
                fullscreenMode -> {
                    ConfigProvider.INSTANCE.ensureLoaded().setFullscreenMode(fullscreenMode);
                    ConfigProvider.INSTANCE.ensureLoaded().save(); // Because this option actually not saving in vanilla options, so it need save manually
                    if (options.fullscreen().get()) {
                        // If fullscreen turns on, re-toggle to changing the fullscreen mode instantly
                        Concentration.toggleFullScreenMode(options, true);
                    }
                });
    }

    @Unique
    private static OptionInstance<Boolean> concentration$wrapperFullscreen(Options options) {
        // Don't put a constant to the second parameter, or else whatever fullscreen you are, when you open video settings page, the value shown is always the same
        return OptionInstance.createBoolean("options.fullscreen", options.fullscreen().get(), (value) -> Concentration.toggleFullScreenMode(options, value));
    }

    @Shadow
    protected abstract void addOptions();

}
