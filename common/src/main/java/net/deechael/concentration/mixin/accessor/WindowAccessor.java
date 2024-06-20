package net.deechael.concentration.mixin.accessor;

import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.Window;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor make setDirty method available
 * @author DeeChael
 */
@Mixin(Window.class)
public interface WindowAccessor {

    @Accessor
    ScreenManager getScreenManager();

    @Accessor
    void setDirty(boolean value);

    @Accessor("x")
    void setX(int x);

    @Accessor("y")
    void setY(int y);

    @Accessor("width")
    void setWidth(int width);

    @Accessor("height")
    void setHeight(int height);

}