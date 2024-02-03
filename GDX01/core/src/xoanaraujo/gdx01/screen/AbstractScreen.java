package xoanaraujo.gdx01.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.audio.AudioManager;
import xoanaraujo.gdx01.input.GameInputListener;
import xoanaraujo.gdx01.input.InputManager;

public abstract class AbstractScreen<T extends Table> implements Screen, GameInputListener {
    protected final Core context;
    protected final FitViewport viewport;
    protected final World world;
    protected final Box2DDebugRenderer debugRenderer;
    protected final Stage stage;
    protected final T screenUI;
    protected final InputManager inputManager;
    protected final AudioManager audioManager;
    protected boolean isMusicLoaded;

    public AbstractScreen(Core context) {
        this.context = context;
        viewport = context.getViewport();
        world = context.getWorld();
        debugRenderer = context.getDebugRenderer();
        stage = context.getStage();
        screenUI = getScreenUI(context);
        inputManager = context.getInputManager();
        audioManager = context.getAudioManager();
        isMusicLoaded = false;
    }

    protected abstract T getScreenUI(Core context);

    @Override
    public void show() {
        inputManager.addInputListener(this);
        stage.addActor(screenUI);
    }

    @Override
    public void hide() {
        inputManager.removeInputListener(this);
        stage.getRoot().removeActor(screenUI);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    public Core getContext() {
        return context;
    }


}
