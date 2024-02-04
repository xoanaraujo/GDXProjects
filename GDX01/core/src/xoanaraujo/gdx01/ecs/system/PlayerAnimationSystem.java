package xoanaraujo.gdx01.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.ecs.components.AnimationComponent;
import xoanaraujo.gdx01.ecs.components.Box2DComponent;
import xoanaraujo.gdx01.ecs.components.PlayerComponent;
import xoanaraujo.gdx01.view.animation.AnimatiomType;

public class PlayerAnimationSystem extends IteratingSystem {

    private static final String TAG = PlayerAnimationSystem.class.getSimpleName();

    public PlayerAnimationSystem() {
        super(Family.all(AnimationComponent.class, PlayerComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final Box2DComponent box2DComponent = ECSEngine.box2DComponentMapper.get(entity);
        final AnimationComponent animationComponent = ECSEngine.animationComponentMapper.get(entity);
        if (box2DComponent.body.getLinearVelocity().equals(Vector2.Zero)){
            if (animationComponent.type == AnimatiomType.PLAYER_MOVE_UP){
                animationComponent.type = AnimatiomType.PLAYER_IDLE_UP;
            }else if (animationComponent.type != AnimatiomType.PLAYER_IDLE_UP){
                animationComponent.type = AnimatiomType.PLAYER_IDLE_DOWN;
            }
        } else if (box2DComponent.body.getLinearVelocity().x > 0) {
            animationComponent.type = AnimatiomType.PLAYER_MOVE_RIGHT;
        } else if (box2DComponent.body.getLinearVelocity().x < 0) {
            animationComponent.type = AnimatiomType.PLAYER_MOVE_LEFT;
        } else if (box2DComponent.body.getLinearVelocity().y > 0) {
            animationComponent.type = AnimatiomType.PLAYER_MOVE_UP;
        } else if (box2DComponent.body.getLinearVelocity().y < 0) {
            animationComponent.type = AnimatiomType.PLAYER_MOVE_DOWN;
        }
    }
}
