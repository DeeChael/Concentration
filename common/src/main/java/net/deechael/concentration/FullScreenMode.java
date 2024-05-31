package net.deechael.concentration;

public enum FullScreenMode {

    WINDOWED, BORDERLESS, FULLSCREEN;

    public static FullScreenMode nextOf(FullScreenMode current) {
        return switch (current) {
            case WINDOWED -> BORDERLESS;
            case BORDERLESS -> FULLSCREEN;
            case FULLSCREEN -> WINDOWED;
        };
    }

    public static FullScreenMode nextBorderless(FullScreenMode current) {
        return switch (current) {
            case FULLSCREEN, BORDERLESS -> WINDOWED;
            case WINDOWED -> BORDERLESS;
        };
    }

    public boolean isBorderless() {
        return this == BORDERLESS;
    }

}
