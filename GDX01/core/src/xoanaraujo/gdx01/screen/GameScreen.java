package xoanaraujo.gdx01.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.entity.Player;
import xoanaraujo.gdx01.map.CollisionArea;
import xoanaraujo.gdx01.map.MyMap;
import xoanaraujo.gdx01.ui.GameUI;

import static xoanaraujo.gdx01.util.GameConst.*;

public class GameScreen extends ScreenAdapter<GameUI> {

    private static final String TAG = GameScreen.class.getSimpleName();
    private static final Color BACKGROUND = new Color(0.1f, 0.1f, 0.1f, 1f);
    private final BodyDef bodyDef;
    private final FixtureDef fixtureDef;
    private final AssetManager assetManager;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final GLProfiler glProfiler;
    private final MyMap map;
    private Player player;

    public GameScreen(Core context) {
        super(context);
        camera = context.getCamera();
        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.getBatch());
        assetManager = context.getAssetManager();
        glProfiler = new GLProfiler(Gdx.graphics);
        glProfiler.enable();
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        final TiledMap tiledMap = assetManager.get("map/map.tmx", TiledMap.class);
        mapRenderer.setMap(tiledMap);
        map = new MyMap(tiledMap);
        spawnCollisionsAreas();

        spawnPlayer();
    }

    @Override
    protected GameUI getScreenUI(Skin skin) {
        return new GameUI(skin);
    }

    private void spawnPlayer() {

        resetBodyAndFixtureDefinition();
        bodyDef.position.set(map.getStartPlayerLocation().x + 0.5f, map.getStartPlayerLocation().y + 0.5f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);
        body.setUserData("PLAYER");

        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_GROUND;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.4f);
        fixtureDef.shape = circleShape;

        body.createFixture(fixtureDef);
        circleShape.dispose();

        player = new Player(body);
    }

    private void spawnCollisionsAreas() {
        for (final CollisionArea collisionArea : map.getCollisionAreas()) {
            resetBodyAndFixtureDefinition();
            bodyDef.position.set(collisionArea.getX(), collisionArea.getY());
            bodyDef.gravityScale = 1;
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.fixedRotation = true;
            Body body = world.createBody(bodyDef);
            body.setUserData("WALLS");

            fixtureDef.filter.categoryBits = BIT_GROUND;
            fixtureDef.filter.maskBits = -1;
            final ChainShape chainShape = new ChainShape();
            chainShape.createChain(collisionArea.getVertices());
            fixtureDef.shape = chainShape;
            body.createFixture(fixtureDef);
            chainShape.dispose();
        }
    }

    private void resetBodyAndFixtureDefinition() {
        bodyDef.position.set(0, 0);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = false;

        fixtureDef.density = 0;
        fixtureDef.restitution = 0;
        fixtureDef.isSensor = false;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = 0x0001;
        fixtureDef.filter.maskBits = -1;
        fixtureDef.shape = null;
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(BACKGROUND);
        viewport.apply(true);
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        mapRenderer.dispose(); // GameScreen doesn't own the batch. This is not needed.
    }
    /*// Create a circle
        bodyDef.position.set(0f, 2f);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);
        body.setUserData("CIRCLE");

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_CIRCLE;
        fixtureDef.filter.maskBits = BIT_GROUND | BIT_BOX;
        CircleShape cShape = new CircleShape();
        cShape.setRadius(0.5f);
        fixtureDef.shape = cShape;
        body.createFixture(fixtureDef);
        cShape.dispose();

        // Create a box
        bodyDef.position.set(0.9f, -2.75f);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        body.setUserData("BOX");

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.5f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_BOX;
        fixtureDef.filter.maskBits = BIT_GROUND | BIT_CIRCLE;
        PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(0.5f, 0.5f);
        fixtureDef.shape = pShape;
        body.createFixture(fixtureDef);
        pShape.dispose();


        // Create platform
        bodyDef.position.set(0f,-3f);
        bodyDef.gravityScale = 1;
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);
        body.setUserData("PLATFORM");

        fixtureDef.isSensor = false;
        fixtureDef.restitution = 0.1f;
        fixtureDef.friction = 0.2f;
        fixtureDef.filter.categoryBits = BIT_GROUND;
        fixtureDef.filter.maskBits = -1;
        pShape = new PolygonShape();
        pShape.setAsBox(3.5f, 0.25f);
        fixtureDef.shape = pShape;
        body.createFixture(fixtureDef);
        pShape.dispose();*/
}
