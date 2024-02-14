package xoanaraujo.gdx01;

import box2dLight.Light;
import box2dLight.RayHandler;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xoanaraujo.gdx01.audio.AudioManager;
import xoanaraujo.gdx01.audio.AudioType;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.input.InputManager;
import xoanaraujo.gdx01.map.MapManager;
import xoanaraujo.gdx01.screen.ScreenType;

import java.util.EnumMap;

import static xoanaraujo.gdx01.util.GameConst.*;

public class Core extends Game {
    private static final String TAG = Core.class.getSimpleName();
    private EnumMap<ScreenType, Screen> screens;
    private ExtendViewport viewport;
    private OrthographicCamera camera;
    private ECSEngine ecsEngine;
    private GameRenderer gameRenderer;
    private World world;
    private WorldContactListener worldContactListener;
    private RayHandler rayHandler;
    private InputManager inputManager;
    private AssetManager assetManager;
    private MapManager mapManager;
    private AudioManager audioManager;
    private PreferenceManager preferenceManager;
    private Stage stage;
    private Skin skin;
    private I18NBundle i18NBundle;
    private SpriteBatch batch;
    private static final Float FIXED_TIME_STEP = 1f / FPS;
    private Float updateTime;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        batch = new SpriteBatch();

        // Screen stuff
        screens = new EnumMap<>(ScreenType.class);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);


        // Physics with Box2D
        Box2D.init();
        updateTime = 0f;
        world = new World(new Vector2(0, 0f), true);
        worldContactListener = new WorldContactListener();
        world.setContactListener(worldContactListener);

        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0, 0, 0, 0.4f);
        Light.setGlobalContactFilter(BIT_PLAYER, (short) 1, BIT_GROUND);
        // AssetManager
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(assetManager.getFileHandleResolver()));
        initializeSkin();
        stage = new Stage(new ExtendViewport(WIDTH, HEIGHT), batch);

        // Audio Manager
        audioManager = new AudioManager(this);
        for (final AudioType audioType : AudioType.values()) {
            if (audioType.isMusic()) {
                assetManager.load(audioType.getPath(), Music.class);
            } else {
                assetManager.load(audioType.getPath(), Sound.class);
            }
        }

        // Input
        inputManager = new InputManager();
        Gdx.input.setInputProcessor(new InputMultiplexer(inputManager, stage));

        // ecs engine
        ecsEngine = new ECSEngine(this);

        // Map Manager
        mapManager = new MapManager(this);

        // Preference manager
        preferenceManager = new PreferenceManager();

        // GameRenderer
        gameRenderer = new GameRenderer(this);

        switchScreen(ScreenType.LOADING);
    }

    private void initializeSkin() {
        // Setup colors to be used in our font
        Colors.put("debug", new Color(1f, 0f, 0f, 3));

        // Generate ttf bitmaps
        final ObjectMap<String, Object> resources = new ObjectMap<>();

        final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/font.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.minFilter = Texture.TextureFilter.Linear;
        fontParameter.magFilter = Texture.TextureFilter.Linear;
        final int[] sizesToCreate = {16, 20, 26, 32};
        for (int size : sizesToCreate) {
            fontParameter.size = size;
            BitmapFont bitmapFont = fontGenerator.generateFont(fontParameter);
            bitmapFont.getData().markupEnabled = true;

            resources.put("font_" + size, bitmapFont);
            fontGenerator.generateFont(fontParameter);
        }
        fontGenerator.dispose();

        // load skin
        final SkinLoader.SkinParameter skinParameter = new SkinLoader.SkinParameter("ui/hud/hud.atlas", resources);
        assetManager.load("ui/hud/hud.json", Skin.class, skinParameter);
        assetManager.load("ui/strings", I18NBundle.class);
        assetManager.finishLoading();
        skin = assetManager.get("ui/hud/hud.json", Skin.class);
        i18NBundle = assetManager.get("ui/strings", I18NBundle.class);

    }

    @Override
    public void render() {
        super.render();
        final float deltaTime = Math.min(0.25f, Gdx.graphics.getDeltaTime());
        ecsEngine.update(deltaTime);

        updateTime += deltaTime;
        while (updateTime >= FIXED_TIME_STEP) {
            world.step(FIXED_TIME_STEP, 6, 2);
            updateTime -= FIXED_TIME_STEP;
        }

        gameRenderer.render(updateTime / FIXED_TIME_STEP);
        stage.getViewport().apply();
        stage.act();
        stage.draw();

    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        assetManager.dispose();
        stage.dispose();
        batch.dispose();
        gameRenderer.dispose();
        rayHandler.dispose();
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
                throw new GdxRuntimeException("[Screen " + screenType + " could not be created]\n" + e);
            }
        } else {
            Gdx.app.debug(TAG, "Switching to " + screenType + " screen");
            setScreen(screen);
        }

    }

    public EnumMap<ScreenType, Screen> getScreens() {
        return screens;
    }

    public ExtendViewport getViewport() {
        return viewport;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public World getWorld() {
        return world;
    }


    public AssetManager getAssetManager() {
        return assetManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public AudioManager getAudioManager() {
        return audioManager;
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

    public I18NBundle getI18NBundle() {
        return i18NBundle;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public ECSEngine getEcsEngine() {
        return ecsEngine;
    }

    public WorldContactListener getWorldContactListener() {
        return worldContactListener;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    public PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }
}
