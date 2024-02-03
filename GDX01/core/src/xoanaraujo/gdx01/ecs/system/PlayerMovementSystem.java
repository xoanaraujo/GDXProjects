package xoanaraujo.gdx01.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.ecs.components.Box2DComponent;
import xoanaraujo.gdx01.ecs.components.PlayerComponent;
import xoanaraujo.gdx01.input.GameInputListener;
import xoanaraujo.gdx01.input.GameKeys;
import xoanaraujo.gdx01.input.InputManager;

public class PlayerMovementSystem extends IteratingSystem implements GameInputListener {
    private Vector2 direction;

    public PlayerMovementSystem(final Core context) {
        super(Family.all(PlayerComponent.class, Box2DComponent.class).get());
        context.getInputManager().addInputListener(this);
        direction = new Vector2();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        final PlayerComponent playerComponent = ECSEngine.playerComponentMapper.get(entity);
        final Box2DComponent box2DComponent = ECSEngine.box2DComponentMapper.get(entity);

        direction.nor();
        box2DComponent.body.applyLinearImpulse(
                (direction.x * playerComponent.velocity - box2DComponent.body.getLinearVelocity().x) * box2DComponent.body.getMass(),
                (direction.y * playerComponent.velocity - box2DComponent.body.getLinearVelocity().y) * box2DComponent.body.getMass(),
                box2DComponent.body.getWorldCenter().x, box2DComponent.body.getWorldCenter().y,true
        );
    }

    @Override
    public void keyDown(InputManager manager, GameKeys gameKey) {
        switch (gameKey) {
            case UP: {
                direction.y = 1;
            }
            break;
            case DOWN: {
                direction.y = -1;
            }
            break;
            case RIGHT: {
                direction.x = 1;
            }
            break;
            case LEFT: {
                direction.x = -1;
            }
            break;
            case SELECT: {
                System.out.println("Illo no esta aun implementado el select hehe");
            }
            break;

        }
    }

    @Override
    public void keyUp(InputManager manager, GameKeys gameKey) {
        switch (gameKey) {
            case UP: {
                direction.y = manager.isKeyPressed(GameKeys.DOWN) ? -1 : 0;
            }
            break;
            case DOWN: {
                direction.y = manager.isKeyPressed(GameKeys.UP) ? 1 : 0;
            }
            break;
            case RIGHT: {
                direction.x = manager.isKeyPressed(GameKeys.LEFT) ? -1 : 0;
            }
            break;
            case LEFT: {
                direction.x = manager.isKeyPressed(GameKeys.RIGHT) ? 1 : 0;
            }
            break;
            case SELECT: {
                System.out.println("Illo no esta aun implementado el select hehe");
            }
            break;

        }
    }
}
