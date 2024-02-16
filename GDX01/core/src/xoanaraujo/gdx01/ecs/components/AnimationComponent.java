package xoanaraujo.gdx01.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import xoanaraujo.gdx01.view.animation.AnimatiomType;

public class AnimationComponent implements Component, Pool.Poolable {

    public AnimatiomType type;
    public float time;
    public float width;
    public float height;
    public boolean paused;
    @Override
    public void reset() {
        type = null;
        width = height = time = 0;
        paused = false;
    }
}
