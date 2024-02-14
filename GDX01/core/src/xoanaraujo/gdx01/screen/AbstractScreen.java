package xoanaraujo.gdx01.screen;

import box2dLight.RayHandler;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.audio.AudioManager;
import xoanaraujo.gdx01.input.GameInputListener;
import xoanaraujo.gdx01.input.InputManager;

public abstract class AbstractScreen<T extends Table> implements Screen, GameInputListener {
    protected final Core context;
    protected final ExtendViewport viewport;
    protected final World world;
    protected final Stage stage;
    protected final T screenUI;
    protected final InputManager inputManager;
    protected final AudioManager audioManager;
    protected boolean isMusicLoaded;
    private final RayHandler rayHandler;

    public AbstractScreen(Core context) {
        this.context = context;
        viewport = context.getViewport();
        world = context.getWorld();
        stage = context.getStage();
        screenUI = getScreenUI(context);
        inputManager = context.getInputManager();
        audioManager = context.getAudioManager();
        rayHandler = context.getRayHandler();
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
        rayHandler.useCustomViewport(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
    }

    public Core getContext() {
        return context;
    }


}
