package xoanaraujo.gdx01;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xoanaraujo.gdx01.screen.ScreenType;

import java.util.EnumMap;

import static xoanaraujo.gdx01.util.GameConst.*;

public class Core extends Game {
    private static final String TAG = Core.class.getSimpleName();
    private EnumMap<ScreenType, Screen> screens;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private World world;
    private WorldContactAdapter worldContactAdapter;
    private Box2DDebugRenderer debugRenderer;
    private AssetManager assetManager;
    private SpriteBatch batch;
    private static final Float FIXED_TIME_STEP = 1f / FPS;
    private Float updateTime;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug(TAG, "FPS: " + Gdx.graphics.getFramesPerSecond());
        batch = new SpriteBatch();
        Box2D.init();

        screens = new EnumMap<>(ScreenType.class);
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        debugRenderer = new Box2DDebugRenderer();

        world = new World(new Vector2(0, -10f), true);
        worldContactAdapter = new WorldContactAdapter();
        world.setContactListener(worldContactAdapter);

        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
        initializeSkin();
        updateTime = 0f;

        switchScreen(ScreenType.LOADING);
    }

    private void initializeSkin() {
        // Generate ttf bitmaps
        // load skin
    }

    @Override
    public void render() {
        super.render();
		updateTime += Gdx.app.getGraphics().getDeltaTime();
        while (updateTime >= FIXED_TIME_STEP){
            world.step(FIXED_TIME_STEP, 6, 2);
            debugRenderer.render(world, viewport.getCamera().combined);
			updateTime -= FIXED_TIME_STEP;
        }

        //  final float alpha = updateTime / FIXED_TIME_STEP;
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        debugRenderer.dispose();
        assetManager.dispose();
        batch.dispose();
    }

    /**
     * @param screenType Enum that contains a reference to a Screen interface implementation
     */
    public void switchScreen(final ScreenType screenType) {
        final Screen screen = screens.get(screenType);
        if (screen == null) { // Screen is not created yet
            try {
                Gdx.app.debug(TAG, "Creating a new " + screenType + " screen");
                final Screen newScreen = (Screen) ClassReflection.getConstructor(screenType.getScreenClass(), Core.class).newInstance(this);
                screens.put(screenType, newScreen);
                setScreen(newScreen);
            } catch (ReflectionException e) {
                throw new GdxRuntimeException("Screen " + screenType + " could not be created" + e);
            }
        } else {
            Gdx.app.debug(TAG, "Switching to " + screenType + " screen");
            setScreen(screen);
        }

    }

    public EnumMap<ScreenType, Screen> getScreens() {
        return screens;
    }

    public FitViewport getViewport() {
        return viewport;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public World getWorld() {
        return world;
    }

    public Box2DDebugRenderer getDebugRenderer() {
        return debugRenderer;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
