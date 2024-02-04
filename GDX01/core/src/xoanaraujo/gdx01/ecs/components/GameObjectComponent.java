package xoanaraujo.gdx01.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Pool;
import xoanaraujo.gdx01.map.GameObjectType;

public class GameObjectComponent implements Component, Pool.Poolable {
    public GameObjectType type;
    public int animationIndex;
    @Override
    public void reset() {
        type = null;
        animationIndex = 0;
    }
}
