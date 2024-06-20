package net.deechael.concentration.fabric;

import net.deechael.concentration.ConcentrationConstants;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Mixin plugin to make sure that sodium support only turned on when sodium installed
 * @author DeeChael
 */
public class ConcentrationFabricMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean vulkan = FabricLoader.getInstance().isModLoaded("vulkanmod");
        if (vulkan) {
            return checkVulkan(mixinClassName) || checkWindowMixin(mixinClassName)
                    || "net.deechael.concentration.mixin.accessor.WindowAccessor".equals(mixinClassName)
                    || "net.deechael.concentration.mixin.KeyboardHandlerMixin".equals(mixinClassName);
        } else {
            return !mixinClassName.contains("fabric") || checkSodium(mixinClassName);
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private boolean checkSodium(String mixinClassName) {
        return "net.deechael.concentration.mixin.fabric.SodiumVideoOptionsScreenMixin".equals(mixinClassName)
                && FabricLoader.getInstance().isModLoaded("sodium");
    }

    private boolean checkVulkan(String mixinClassName) {
        return "net.deechael.concentration.mixin.fabric.OptionsMixin".equals(mixinClassName);
    }

    private boolean checkWindowMixin(String mixinClassName) {
        return mixinClassName.startsWith("net.deechael.concentration.mixin.fabric.WindowMixin");
    }

}
