package xoanaraujo.gdx01.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.screen.LoadingScreen;
import xoanaraujo.gdx01.screen.ScreenAbstract;
import xoanaraujo.gdx01.screen.ScreenType;

public class LoadingInput extends InputAdapter {
    private Core context;
    private LoadingScreen loadingScreen;
    public LoadingInput(LoadingScreen loadingScreen) {
        this.context = loadingScreen.getContext();
        this.loadingScreen = loadingScreen;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean keyDown(int keycode) {
        checkLoading();
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        checkLoading();
        return true;
    }

    private void checkLoading(){
        if (context.getAssetManager().isFinished()){
            context.switchScreen(ScreenType.GAME);
        }
    }
}
