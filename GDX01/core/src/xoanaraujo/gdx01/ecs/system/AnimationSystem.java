package xoanaraujo.gdx01.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.ecs.components.AnimationComponent;

public class AnimationSystem extends IteratingSystem {
    public AnimationSystem() {
        super(Family.all(AnimationComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final AnimationComponent animationComponent = ECSEngine.animationComponentMapper.get(entity);
        if (animationComponent.type != null){
            animationComponent.time += deltaTime;
        }
    }
}
