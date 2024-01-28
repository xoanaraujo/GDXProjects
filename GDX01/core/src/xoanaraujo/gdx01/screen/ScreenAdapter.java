package xoanaraujo.gdx01.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xoanaraujo.gdx01.Core;

public abstract class ScreenAdapter implements Screen {
    protected final Core context;
    protected final FitViewport viewport;
    protected final World world;
    protected final Box2DDebugRenderer debugRenderer;

    public ScreenAdapter(Core context) {
        this.context = context;
        this.viewport = context.getViewport();
        this.world = context.getWorld();
        this.debugRenderer = context.getDebugRenderer();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

}
