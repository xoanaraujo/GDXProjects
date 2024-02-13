package xoanaraujo.gdx01.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.WorldContactListener;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.ecs.components.GameObjectComponent;
import xoanaraujo.gdx01.ecs.components.RemoveEntityComponent;
import xoanaraujo.gdx01.map.gameobject.GameObjectType;

public class PlayerCollisionSystem extends IteratingSystem implements WorldContactListener.PlayerCollisionListener {
    public PlayerCollisionSystem(final Core context) {
        super(Family.all(RemoveEntityComponent.class).get());
        context.getWorldContactListener().addListener(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        getEngine().removeEntity(entity);
    }

    @Override
    public void playerCollision(Entity player, Entity gameObject) {
        final GameObjectComponent gameObjectComponent = ECSEngine.gameObjectComponentMapper.get(gameObject);
        if (gameObjectComponent.type == GameObjectType.BOX){
            gameObject.add(getEngine().createComponent(RemoveEntityComponent.class));
        }
    }
}
