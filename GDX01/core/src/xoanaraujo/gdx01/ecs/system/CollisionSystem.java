package xoanaraujo.gdx01.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.IntMap;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.WorldContactListener;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.ecs.components.AnimationComponent;
import xoanaraujo.gdx01.ecs.components.GameObjectComponent;
import xoanaraujo.gdx01.ecs.components.RemoveEntityComponent;
import xoanaraujo.gdx01.map.GameMap;
import xoanaraujo.gdx01.map.gameobject.GameObjectType;

public class CollisionSystem extends IteratingSystem implements WorldContactListener.PlayerCollisionListener, CollisionSystemListener {
    private IntMap<Animation<Sprite>> mapAnimations;
    public CollisionSystem(final Core context) {
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
        } else if (gameObjectComponent.type == GameObjectType.CHEST){
            final AnimationComponent animationComponent = ECSEngine.animationComponentMapper.get(gameObject);
            Animation<Sprite> animation = mapAnimations.get(gameObjectComponent.animationIndex);
            animationComponent.paused = false;
        }
    }

    @Override
    public void updateMapObjects(GameMap currentGameMap) {
        mapAnimations = currentGameMap.getMapAnimations();
    }
}
