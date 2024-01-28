package xoanaraujo.gdx01.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import xoanaraujo.gdx01.Core;

public class LoadingScreen extends ScreenAdapter {
    private static final Color BACKGROUND = new Color(0.4f, 0.1f, 0.1f, 1f);

    public LoadingScreen(Core context) {
        super(context);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(BACKGROUND);
        if(Gdx.input.isKeyJustPressed(Input.Keys.E))
            context.switchScreen(ScreenType.GAME);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
