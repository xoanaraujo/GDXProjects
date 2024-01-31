package xoanaraujo.gdx01.util;

public class GameConst {
    public static final int WORLD_WIDTH = 20, WORLD_HEIGHT = 20;

    private static final float PIXELS_PER_METER =16f;
    public static final float UNIT_SCALE = 1 / PIXELS_PER_METER;

    public static final int FPS = 60;
    public static final short BIT_GROUND = 1 << 0;
    public static final short BIT_PLAYER = 1 << 1;
}
