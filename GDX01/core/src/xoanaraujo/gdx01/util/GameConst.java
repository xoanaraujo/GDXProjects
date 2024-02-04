package xoanaraujo.gdx01.util;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameConst {
    public static final int WORLD_WIDTH = 16, WORLD_HEIGHT = 9;
    public static final int WIDTH = WORLD_WIDTH * 70, HEIGHT = WORLD_HEIGHT * 70;
    public static final float UNIT_SCALE = 1 / 16f;
    public static final int FPS = 60;
    public static final short BIT_GROUND = 1 << 0;
    public static final short BIT_PLAYER = 1 << 1;
    public static final short BIT_GAME_OBJECT = 1 << 2;

    public static final FixtureDef FIXTURE_DEF = new FixtureDef();
    public static final BodyDef BODY_DEF = new BodyDef();

    public static void resetBodyAndFixtureDefinition() {
        BODY_DEF.position.set(0, 0);
        BODY_DEF.gravityScale = 1;
        BODY_DEF.type = BodyDef.BodyType.StaticBody;
        BODY_DEF.fixedRotation = false;

        FIXTURE_DEF.density = 0;
        FIXTURE_DEF.restitution = 0;
        FIXTURE_DEF.isSensor = false;
        FIXTURE_DEF.friction = 0.2f;
        FIXTURE_DEF.filter.categoryBits = 0x0001;
        FIXTURE_DEF.filter.maskBits = -1;
        FIXTURE_DEF.shape = null;
    }
}
