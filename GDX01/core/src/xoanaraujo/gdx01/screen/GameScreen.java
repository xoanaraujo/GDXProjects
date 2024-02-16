package xoanaraujo.gdx01.screen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.PreferenceManager;
import xoanaraujo.gdx01.audio.AudioType;
import xoanaraujo.gdx01.input.GameKeys;
import xoanaraujo.gdx01.input.InputManager;
import xoanaraujo.gdx01.map.*;
import xoanaraujo.gdx01.view.ui.GameUI;

import static xoanaraujo.gdx01.util.GameConst.*;

public class GameScreen extends AbstractScreen<GameUI> implements MapListener{
    private static final String TAG = GameScreen.class.getSimpleName();
    private final MapManager mapManager;
    private final AssetManager assetManager;
    private final PreferenceManager preferenceManager;
    private final Entity player;
    public GameScreen(Core context) {
        super(context);
        assetManager = context.getAssetManager();
        mapManager = context.getMapManager();
        mapManager.setMap(MapType.MAP_1);
        context.getInputManager().addInputListener(this);
        preferenceManager = context.getPreferenceManager();
        context.setPlayer( context.getEcsEngine().createPlayer(mapManager.getCurrentMap().getStartPlayerLocation(), 0.33f));
        player = context.getPlayer();
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
    }

    @Override
    public void render(float delta) {
        screenUI.updateFPS((int) (FPS / delta));
        if (!isMusicLoaded && assetManager.isLoaded(AudioType.CHRONO.getPath())) {
            isMusicLoaded = true;
            audioManager.playAudio(AudioType.CHRONO);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void keyDown(InputManager manager, GameKeys gameKey) {
        if(gameKey == GameKeys.SELECT){
            preferenceManager.loadGameState(player);
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys gameKey) {

    }

    @Override
    public void mapChange(GameMap currentMap) {

    }
}
