package xoanaraujo.gdx01.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.ecs.components.Box2DComponent;
import xoanaraujo.gdx01.ecs.components.PlayerComponent;
import xoanaraujo.gdx01.ecs.components.RemoveEntityComponent;

public class PlayerCameraSystem extends IteratingSystem {
    private final OrthographicCamera camera;

    public PlayerCameraSystem(Core context) {
        super(Family.all(PlayerComponent.class).get());
        camera = context.getCamera();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final Box2DComponent box2DComponent = ECSEngine.box2DComponentMapper.get(entity);
        camera.position.set(box2DComponent.renderPosition, 0);
        camera.update();
    }
}
