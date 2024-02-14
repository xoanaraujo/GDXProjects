package xoanaraujo.gdx01.ecs.components;

import box2dLight.Light;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

public class Box2DComponent implements Component, Pool.Poolable {
    public Body body;
    public Light light;
    public float
            width,
            height,
            lightDistance,
            lightFluctuation,
            lightFluctuationDistance,
            lightFluctuationTime,
            lightFluctuationSpeed;
    public Vector2 renderPosition = new Vector2();
    @Override
    public void reset() {
        if (light != null){
            light.remove();
            light.dispose();
            light = null;
        }
        if (body != null){
            body.getWorld().destroyBody(body);
            body = null;
        }
        lightFluctuation =
        lightDistance =
        lightFluctuationDistance =
        lightFluctuationSpeed =
        lightFluctuationTime=
        width =
        height = 0;
        renderPosition.set(0, 0);
    }
}
