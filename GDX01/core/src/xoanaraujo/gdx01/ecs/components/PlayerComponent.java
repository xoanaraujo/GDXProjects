package xoanaraujo.gdx01.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class PlayerComponent implements Component, Pool.Poolable {
    public boolean hasWeapon;
    public float velocity;
    @Override
    public void reset() {
        hasWeapon = false;
        velocity = 0;
    }
}
