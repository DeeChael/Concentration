package net.deechael.concentration.fabric;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Mixin plugin to make sure that sodium support only turned on when sodium installed
 *
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
        if (FabricLoader.getInstance().isModLoaded("vulkanmod")) {
            return mixinClassName.equals("net.deechael.concentration.fabric.mixin.VulkanWindowMixin") ||
                    mixinClassName.equals("net.deechael.concentration.fabric.mixin.OptionsMixin") ||
                    mixinClassName.equals("net.deechael.concentration.fabric.mixin.GLFWMixin");
        } else {
            return checkSodium(mixinClassName) || mixinClassName.equals("net.deechael.concentration.fabric.mixin.WindowMixin");
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

    private static boolean checkSodium(String mixinClassName) {
        return "net.deechael.concentration.fabric.mixin.SodiumVideoOptionsScreenMixin".equals(mixinClassName)
                && FabricLoader.getInstance().isModLoaded("sodium");
    }

}
