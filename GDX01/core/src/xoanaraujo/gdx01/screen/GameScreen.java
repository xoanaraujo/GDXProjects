package xoanaraujo.gdx01.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.audio.AudioType;
import xoanaraujo.gdx01.input.GameKeys;
import xoanaraujo.gdx01.input.InputManager;
import xoanaraujo.gdx01.map.*;
import xoanaraujo.gdx01.ui.GameUI;

import static xoanaraujo.gdx01.util.GameConst.*;

public class GameScreen extends AbstractScreen<GameUI> implements MapListener {
    private static final String TAG = GameScreen.class.getSimpleName();
    private static final Color BACKGROUND = new Color(0.1f, 0.1f, 0.1f, 1f);
    private final AssetManager assetManager;
    private final MapManager mapManager;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final OrthographicCamera camera;
    private final GLProfiler glProfiler;

    public GameScreen(Core context) {
        super(context);
        assetManager = context.getAssetManager();
        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, context.getBatch());
        camera = context.getCamera();
        glProfiler = new GLProfiler(Gdx.graphics);
        // glProfiler.enable();
        mapManager = new MapManager(context);
        mapManager.addMapListener(this);
        mapManager.setMap(MapType.MAP_1);

        context.getEcsEngine().createPlayer(mapManager.getCurrentMap().getStartPlayerLocation(), 0.4f);
    }

    @Override
    protected GameUI getScreenUI(Core context) {
        return new GameUI(context);
    }

    @Override
    public void show() {
        super.show();

    }

    @Override
    public void hide() {
        super.hide();
        audioManager.stopMusic();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(BACKGROUND);

        if (mapRenderer.getMap() != null){
            mapRenderer.setView(camera);
            mapRenderer.render();
        }
        debugRenderer.render(world, viewport.getCamera().combined);
        if (!isMusicLoaded && assetManager.isLoaded(AudioType.MAIN.getPath())) {
            isMusicLoaded = true;
            audioManager.playAudio(AudioType.MAIN);
        }

        viewport.apply(false);
        screenUI.updateFPS((int) (FPS / delta));
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

    @Override
    public void keyDown(InputManager manager, GameKeys gameKey) {

    }

    @Override
    public void keyUp(InputManager manager, GameKeys gameKey) {

    }

    @Override
    public void mapChange(GameMap currentMap) {
        mapRenderer.setMap(mapManager.getCurrentMap().getTiledMap());
    }
}
