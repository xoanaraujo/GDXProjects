package xoanaraujo.gdx01.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xoanaraujo.gdx01.Core;

public abstract class ScreenAdapter<T extends Table> implements Screen {
    protected final Core context;
    protected final FitViewport viewport;
    protected final World world;
    protected final Box2DDebugRenderer debugRenderer;
    protected final Stage stage;
    protected final T screenUI;

    public ScreenAdapter(Core context) {
        this.context = context;
        this.viewport = context.getViewport();
        this.world = context.getWorld();
        this.debugRenderer = context.getDebugRenderer();
        stage = context.getStage();
        screenUI = getScreenUI(context.getSkin());
    }

    protected abstract T getScreenUI(Skin skin);

    @Override
    public void show() {
        stage.addActor(screenUI);
    }

    @Override
    public void hide() {
        stage.getRoot().removeActor(screenUI);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);


    }

}
