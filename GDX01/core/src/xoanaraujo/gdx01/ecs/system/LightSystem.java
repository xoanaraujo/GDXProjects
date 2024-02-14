package xoanaraujo.gdx01.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.ecs.components.Box2DComponent;

public class LightSystem extends IteratingSystem {
    public LightSystem() {
        super(Family.all(Box2DComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Box2DComponent box2DComponent = ECSEngine.box2DComponentMapper.get(entity);

        if (box2DComponent != null && box2DComponent.lightFluctuationDistance > 0) {
            box2DComponent.lightFluctuationTime += box2DComponent.lightFluctuationSpeed * deltaTime;
            if (box2DComponent.lightFluctuationTime > MathUtils.PI2) {
                box2DComponent.lightFluctuationTime = 0;
            }
            box2DComponent.light.setDistance(box2DComponent.lightDistance + MathUtils.sin(box2DComponent.lightFluctuationTime) * box2DComponent.lightFluctuationDistance);
        }
    }
}
