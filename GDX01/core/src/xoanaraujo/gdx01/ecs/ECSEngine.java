package xoanaraujo.gdx01.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.ecs.components.AnimationComponent;
import xoanaraujo.gdx01.ecs.components.GameObjectComponent;
import xoanaraujo.gdx01.ecs.system.*;
import xoanaraujo.gdx01.ecs.components.Box2DComponent;
import xoanaraujo.gdx01.ecs.components.PlayerComponent;
import xoanaraujo.gdx01.map.gameobject.GameObject;
import xoanaraujo.gdx01.util.GameConst;
import xoanaraujo.gdx01.view.animation.AnimatiomType;

import static xoanaraujo.gdx01.util.GameConst.*;

public class ECSEngine extends PooledEngine {
    public static final ComponentMapper<PlayerComponent> playerComponentMapper = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<Box2DComponent> box2DComponentMapper = ComponentMapper.getFor(Box2DComponent.class);
    public static final ComponentMapper<AnimationComponent> animationComponentMapper = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<GameObjectComponent> gameObjectComponentMapper = ComponentMapper.getFor(GameObjectComponent.class);
    private final World world;
    private final Vector2 localPosition, posBeforeRotation, posAfterRotation;

    public ECSEngine(Core context) {
        super();
        world = context.getWorld();
        localPosition = new Vector2();
        posBeforeRotation = new Vector2();
        posAfterRotation = new Vector2();
        addSystem(new PlayerMovementSystem(context));
        addSystem(new PlayerCameraSystem(context));
        addSystem(new AnimationSystem());
        addSystem(new PlayerAnimationSystem());

        addSystem(new PlayerCollisionSystem(context));
    }



    public void createPlayer(final Vector2 spawnLocation, final float radius) {
        final Entity playerEntity = this.createEntity();

        // Add components
        final PlayerComponent playerComponent = createComponent(PlayerComponent.class);
        playerComponent.velocity = 3f;
        playerEntity.add(playerComponent);

        resetBodyAndFixtureDefinition();
        final Box2DComponent box2DComponent = createComponent(Box2DComponent.class);
        BODY_DEF.position.set(spawnLocation.x + 0.5f, spawnLocation.y + 0.5f);
        BODY_DEF.fixedRotation = true;
        BODY_DEF.type = BodyDef.BodyType.DynamicBody;
        box2DComponent.body = world.createBody(BODY_DEF);
        box2DComponent.body.setUserData(playerEntity);
        box2DComponent.width = 1;
        box2DComponent.height = 1;
        box2DComponent.renderPosition.set(box2DComponent.body.getPosition());

        FIXTURE_DEF.friction = 0f;
        FIXTURE_DEF.filter.categoryBits = BIT_PLAYER;
        FIXTURE_DEF.filter.maskBits = BIT_GROUND | BIT_GAME_OBJECT;

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(radius);
        FIXTURE_DEF.shape = circleShape;

        box2DComponent.body.createFixture(FIXTURE_DEF);
        circleShape.dispose();

        playerEntity.add(box2DComponent);

        // Animation component
        final AnimationComponent animationComponent = createComponent(AnimationComponent.class);
        animationComponent.type = AnimatiomType.PLAYER_IDLE_DOWN;
        animationComponent.width = animationComponent.height = 48 * UNIT_SCALE;
        playerEntity.add(animationComponent);

        addEntity(playerEntity);
    }

    public void createGameObject(final GameObject gameObject) {
        final Entity gameObjectEntity = createEntity();

        // GameObject component
        final GameObjectComponent gameObjectComponent = createComponent(GameObjectComponent.class);
        gameObjectComponent.animationIndex = gameObject.getAnimationIndex();
        gameObjectComponent.type = gameObject.getType();
        gameObjectEntity.add(gameObjectComponent);

        // Animation component
        final AnimationComponent animationComponent = createComponent(AnimationComponent.class);
        animationComponent.type = null;
        animationComponent.width = gameObject.getWidth();
        animationComponent.height = gameObject.getHeight();
        gameObjectEntity.add(animationComponent);

        // Box2D component
        GameConst.resetBodyAndFixtureDefinition();
        final float halfW = gameObject.getWidth() * 0.5f;
        final float halfH = gameObject.getWidth() * 0.5f;
        final float angleRadians = -gameObject.getRotationDegrees() * MathUtils.degreesToRadians;
        final Box2DComponent  box2DComponent = createComponent(Box2DComponent.class);
        BODY_DEF.type = BodyDef.BodyType.StaticBody;
        BODY_DEF.position.set(gameObject.getPosition().x + halfW, gameObject.getPosition().y + halfH);
        box2DComponent.body = world.createBody(BODY_DEF);
        box2DComponent.body.setUserData(gameObjectEntity);
        box2DComponent.width = gameObject.getWidth();
        box2DComponent.height = gameObject.getHeight();

        // Save position before rotation. Tiled is rotating around the bottom left corner instead of the center of a Tile
        localPosition.set(-halfW, -halfH);
        posBeforeRotation.set(box2DComponent.body.getWorldPoint(localPosition));
        // Rotate body
        box2DComponent.body.setTransform(box2DComponent.body.getPosition(), angleRadians);
        // Get position after rotation
        posAfterRotation.set(box2DComponent.body.getWorldPoint(localPosition));
        // Adjust position to original value before rotation
        box2DComponent.body.setTransform(box2DComponent.body.getPosition().add(posBeforeRotation).sub(posAfterRotation), angleRadians);
        box2DComponent.renderPosition.set(box2DComponent.body.getPosition().x - animationComponent.width * 0.5f, box2DComponent.body.getPosition().y / box2DComponent.height * 0.5f);

        FIXTURE_DEF.filter.categoryBits = BIT_GAME_OBJECT;
        FIXTURE_DEF.filter.maskBits  = BIT_PLAYER;

        final PolygonShape pShape = new PolygonShape();
        pShape.setAsBox(halfW, halfH);
        FIXTURE_DEF.shape = pShape;
        box2DComponent.body.createFixture(FIXTURE_DEF);
        pShape.dispose();

        gameObjectEntity.add(box2DComponent);
        addEntity(gameObjectEntity);
    }
}
