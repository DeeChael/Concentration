package net.deechael.concentration.mixin;

import net.deechael.concentration.Concentration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Make vanilla fullscreen option follow Concentration function
 * @author DeeChael
 */
@Mixin(VideoSettingsScreen.class)
public class VideoSettingsScreenMixin {

    @Inject(method = "options", at = @At("HEAD"), cancellable = true)
    private static void inject$options(Options options, CallbackInfoReturnable<OptionInstance<?>[]> cir) {
        cir.setReturnValue(
                new OptionInstance[] {
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
                        concentration$wrapperFullscreen(),
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
    private static OptionInstance<Boolean> concentration$wrapperFullscreen() {
        return OptionInstance.createBoolean("options.fullscreen", Minecraft.getInstance().options.fullscreen().get(), (value) -> {
            Concentration.toggleFullScreenMode(Minecraft.getInstance().options, value);
        });
    }

}
