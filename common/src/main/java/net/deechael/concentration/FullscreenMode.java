package net.deechael.concentration;

public enum FullscreenMode {

    // Vanilla windowed mode
    WINDOWED,
    // Mod provided borderless window to simulate fullscreen visual
    BORDERLESS,
    // Vanilla fullscreen mode
    FULLSCREEN;

    public static FullscreenMode nextOf(FullscreenMode current) {
        return switch (current) {
            case WINDOWED -> BORDERLESS;
            case BORDERLESS -> FULLSCREEN;
            case FULLSCREEN -> WINDOWED;
        };
    }

    public static FullscreenMode nextBorderless(FullscreenMode current) {
        return switch (current) {
            case FULLSCREEN, BORDERLESS -> WINDOWED;
            case WINDOWED -> BORDERLESS;
        };
    }

    public boolean isBorderless() {
        return this == BORDERLESS;
    }

}
