package xoanaraujo.gdx01;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xoanaraujo.gdx01.screen.ScreenType;
import xoanaraujo.gdx01.util.GameConst;

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
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private static final Float FIXED_TIME_STEP = 1f / FPS;
    private Float updateTime;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug(TAG, "FPS: " + Gdx.graphics.getFramesPerSecond());
        batch = new SpriteBatch();
        updateTime = 0f;
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
        stage = new Stage(new FitViewport(WIDTH, HEIGHT), batch);
        switchScreen(ScreenType.LOADING);
    }

    private void initializeSkin() {
        // Generate ttf bitmaps
        final ObjectMap<String, Object> resources = new ObjectMap<>();

        final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/font/alagard.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.minFilter = Texture.TextureFilter.Nearest;
        fontParameter.magFilter = Texture.TextureFilter.Nearest;
        final int[] sizesToCreate = {16, 20, 26, 32};
        for (int size : sizesToCreate) {
            fontParameter.size = size;
            resources.put("font_" + size, fontGenerator.generateFont(fontParameter));
            fontGenerator.generateFont(fontParameter);
        }
        fontGenerator.dispose();

        // load skin
        final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("ui/hud/hud.atlas", resources);
        assetManager.load("ui/hud/hud.json", Skin.class, skinParameter);
        assetManager.finishLoading();
        skin = assetManager.get("ui/hud/hud.json",Skin.class);

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
        stage.getViewport().apply();
        stage.act();
        stage.draw();

    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        debugRenderer.dispose();
        assetManager.dispose();
        stage.dispose();
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

    public Stage getStage() {
        return stage;
    }

    public Skin getSkin() {
        return skin;
    }
}
