package xoanaraujo.gdx01.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.ecs.components.Box2DComponent;
import xoanaraujo.gdx01.ecs.components.PlayerComponent;
import xoanaraujo.gdx01.ecs.system.PlayerCameraSystem;
import xoanaraujo.gdx01.ecs.system.PlayerMovementSystem;

import static xoanaraujo.gdx01.util.GameConst.*;

public class ECSEngine extends PooledEngine {
    public static final ComponentMapper<PlayerComponent> playerComponentMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<Box2DComponent> box2DComponentMapper = ComponentMapper.getFor(Box2DComponent.class);
    private final World world;


    public ECSEngine(Core context) {
        super();
        world = context.getWorld();
        addSystem(new PlayerMovementSystem(context));
        addSystem(new PlayerCameraSystem(context));
    }



    public void createPlayer(final Vector2 spawnLocation, final float radius) {
        final Entity player = this.createEntity();

        // Add components
        final PlayerComponent playerComponent = createComponent(PlayerComponent.class);
        playerComponent.velocity = 3f;
        player.add(playerComponent);

        resetBodyAndFixtureDefinition();
        final Box2DComponent box2DComponent = createComponent(Box2DComponent.class);
        BODY_DEF.position.set(spawnLocation.x + 0.5f, spawnLocation.y + 0.5f);
        BODY_DEF.fixedRotation = true;
        BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        box2DComponent.body = world.createBody(BODY_DEF);
        box2DComponent.body.setUserData("PLAYER");
        box2DComponent.width = radius * 2;
        box2DComponent.height = radius * 2;

        FIXTURE_DEF.friction = 0f;
        FIXTURE_DEF.filter.categoryBits = BIT_PLAYER;
        FIXTURE_DEF.filter.maskBits = BIT_GROUND;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        FIXTURE_DEF.shape = circleShape;

        box2DComponent.body.createFixture(FIXTURE_DEF);
        circleShape.dispose();

        player.add(box2DComponent);
        addEntity(player);
    }
}
