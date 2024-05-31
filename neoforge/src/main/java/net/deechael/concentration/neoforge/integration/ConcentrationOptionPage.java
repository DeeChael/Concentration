package net.deechael.concentration.neoforge.integration;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import net.deechael.concentration.AttachMode;
import net.deechael.concentration.ConcentrationConstants;
import net.deechael.concentration.FullScreenMode;
import net.deechael.concentration.neoforge.NeoForgeConcentrationConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;

public class ConcentrationOptionPage extends OptionPage {

    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(new ResourceLocation(ConcentrationConstants.MOD_ID, "general"));

    public ConcentrationOptionPage() {
        super(ID, Component.translatable("concentration.options.general"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        groups.add(OptionGroup.createBuilder()
                .add(
                        OptionImpl.createBuilder(FullScreenMode.class, ConcentrationOptions.STORAGE)
                                .setId(new ResourceLocation(ConcentrationConstants.MOD_ID, "fullscreen"))
                                .setName(Component.translatable("concentration.options.general.fullscreen.title"))
                                .setTooltip(Component.translatable("concentration.options.general.fullscreen.desc"))
                                .setControl((opt) -> new CyclingControl<>(opt, FullScreenMode.class, new Component[]{
                                        Component.translatable("concentration.options.fullscreen.windowed"),
                                        Component.translatable("concentration.options.fullscreen.borderless"),
                                        Component.translatable("options.fullscreen")
                                }))
                                .setBinding((options, value) -> NeoForgeConcentrationConfig.INSTANCE.setFullScreenMode(Minecraft.getInstance().options, value),
                                        (options) -> NeoForgeConcentrationConfig.FULL_SCREEN_MODE.get())
                                .build()
                )
                .add(
                        OptionImpl.createBuilder(AttachMode.class, ConcentrationOptions.STORAGE)
                                .setId(new ResourceLocation(ConcentrationConstants.MOD_ID, "attach_mode"))
                                .setName(Component.translatable("concentration.options.general.attach_mode.title"))
                                .setTooltip(Component.translatable("concentration.options.general.attach_mode.desc"))
                                .setControl(option -> new CyclingControl<>(option, AttachMode.class, new Component[]{
                                        Component.translatable("concentration.options.attach_mode.attach"),
                                        Component.translatable("concentration.options.attach_mode.replace")
                                }))
                                .setBinding((options, value) -> NeoForgeConcentrationConfig.ATTACH_MODE.set(value),
                                        (options) -> NeoForgeConcentrationConfig.ATTACH_MODE.get())
                                .build()
                )
                .build()
        );

        return ImmutableList.copyOf(groups);
    }

}
