package xoanaraujo.gdx01.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.ScreenUtils;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.audio.AudioType;
import xoanaraujo.gdx01.input.GameKeys;
import xoanaraujo.gdx01.input.InputManager;
import xoanaraujo.gdx01.map.MapType;
import xoanaraujo.gdx01.view.ui.LoadingUI;
import xoanaraujo.gdx01.view.animation.AnimatiomType;


public class LoadingScreen extends AbstractScreen<LoadingUI>{
    private static final String TAG = LoadingScreen.class.getSimpleName();
    private final AssetManager assetManager;
    private static final Color BACKGROUND = new Color(0.1f, 0.1f, 0.1f, 1f);

    public LoadingScreen(Core context) {
        super(context);
        assetManager = context.getAssetManager();

        for (MapType mapType : MapType.values()) {
            assetManager.load(mapType.getPath(), TiledMap.class);
        }
        for (AnimatiomType type : AnimatiomType.values()) {
            assetManager.load(type.getPath(), TextureAtlas.class);
        }
    }

    @Override
    protected LoadingUI getScreenUI(Core context) {
        return new LoadingUI(context);
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
        assetManager.update();
        screenUI.updateProgressBar(assetManager.getProgress());
        if(!isMusicLoaded && assetManager.isLoaded(AudioType.ZELDA.getPath())){
            isMusicLoaded = true;
            audioManager.playAudio(AudioType.ZELDA);
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
        Gdx.app.debug(TAG, "keyDown");

        if (assetManager.getProgress() >= 1){
            context.switchScreen(ScreenType.GAME);
        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys gameKey) {

    }
}
