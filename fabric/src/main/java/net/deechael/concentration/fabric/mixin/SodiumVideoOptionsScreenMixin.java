package net.deechael.concentration.fabric.mixin;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.deechael.concentration.Concentration;
import net.deechael.concentration.FullscreenMode;
import net.deechael.concentration.fabric.config.ConcentrationConfigFabric;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

/**
 * Hooks sodium options to make sure that changing fullscreen behaviour will use Concentration function instead of vanilla function
 *
 * @author DeeChael
 */
@Mixin(SodiumGameOptionPages.class)
public class SodiumVideoOptionsScreenMixin {

    @Shadow
    @Final
    private static MinecraftOptionsStorage vanillaOpts;

    @ModifyArg(method = "general", at = @At(value = "INVOKE", target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionPage;<init>(Lnet/minecraft/network/chat/Component;Lcom/google/common/collect/ImmutableList;)V"), index = 1)
    private static ImmutableList<OptionGroup> inject$general(ImmutableList<OptionGroup> groups) {
        List<OptionGroup> newGroups = new ArrayList<>();

        for (OptionGroup group : groups) {
            OptionGroup.Builder builder = OptionGroup.createBuilder();
            for (Option<?> option : group.getOptions()) {
                if (option.getName().getContents() instanceof TranslatableContents translatableContents) {
                    if (translatableContents.getKey().equals("options.fullscreen")) {
                        builder.add(
                                OptionImpl.createBuilder(FullscreenMode.class, vanillaOpts)
                                        .setName(Component.translatable("concentration.option.fullscreen_mode"))
                                        .setTooltip(Component.translatable("concentration.option.fullscreen_mode.tooltip"))
                                        .setControl((opt) -> new CyclingControl<>(opt, FullscreenMode.class, new Component[]{
                                                Component.translatable("concentration.option.fullscreen_mode.borderless"),
                                                Component.translatable("concentration.option.fullscreen_mode.native")
                                        }))
                                        .setBinding((options, value) -> {
                                                    ConcentrationConfigFabric.getInstance().fullscreen = value;
                                                    ConcentrationConfigFabric.getInstance().save();
                                                    if (options.fullscreen().get()) {
                                                        // If fullscreen turns on, re-toggle to changing the fullscreen mode instantly
                                                        Concentration.toggleFullScreenMode(options, true);
                                                    }
                                                },
                                                (options) -> ConcentrationConfigFabric.getInstance().fullscreen
                                        )
                                        .build()
                        ).add(
                                OptionImpl.createBuilder(boolean.class, vanillaOpts)
                                        .setName(Component.translatable("options.fullscreen"))
                                        .setTooltip(Component.translatable("sodium.options.fullscreen.tooltip"))
                                        .setControl(TickBoxControl::new)
                                        .setBinding(Concentration::toggleFullScreenMode, (options) -> options.fullscreen().get())
                                        .build()
                        );
                        continue;
                    }
                }
                builder.add(option);
            }
            newGroups.add(builder.build());
        }

        return ImmutableList.copyOf(newGroups);
    }

}
