package xoanaraujo.gdx01.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.screen.ScreenAbstract;

public abstract class InputAbstract<T extends ScreenAbstract> extends InputAdapter {
    protected final Core context;
    protected final T screen;

    public InputAbstract(T screen) {
        context = screen.getContext();
        this.screen = screen;
        Gdx.input.setInputProcessor(this);
    }
}
