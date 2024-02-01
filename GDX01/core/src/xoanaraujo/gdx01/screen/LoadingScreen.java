package xoanaraujo.gdx01.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.ui.LoadingUI;

public class LoadingScreen extends ScreenAdapter<LoadingUI> {
    private final AssetManager assetManager;
    private static final Color BACKGROUND = new Color(0.1f, 0.1f, 0.1f, 1f);

    public LoadingScreen(Core context) {
        super(context);
        assetManager = context.getAssetManager();
        assetManager.load("map/map.tmx", TiledMap.class);
    }

    @Override
    protected LoadingUI getScreenUI(Skin skin) {
        return new LoadingUI(skin);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(BACKGROUND);

        if(assetManager.update() && Gdx.input.isTouched()){
            context.switchScreen(ScreenType.GAME);
        }
        screenUI.updateProgressBar(assetManager.getProgress());
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
}
