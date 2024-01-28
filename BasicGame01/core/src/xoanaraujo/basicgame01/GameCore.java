package xoanaraujo.basicgame01;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameCore extends Game {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private static final Integer WIDTH = Gdx.graphics.getWidth(), HEIGHT = Gdx.graphics.getWidth();

    @Override
    public void create() {
        camera.setToOrtho(false, WIDTH, HEIGHT);
        setScreen(new GameScreen());
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);
    }

    @Override
    public void dispose() {
    }
}
