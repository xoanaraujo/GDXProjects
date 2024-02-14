package xoanaraujo.gdx01;

import box2dLight.RayHandler;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.ecs.components.AnimationComponent;
import xoanaraujo.gdx01.ecs.components.Box2DComponent;
import xoanaraujo.gdx01.ecs.components.GameObjectComponent;
import xoanaraujo.gdx01.input.GameInputListener;
import xoanaraujo.gdx01.input.GameKeys;
import xoanaraujo.gdx01.input.InputManager;
import xoanaraujo.gdx01.map.GameMap;
import xoanaraujo.gdx01.map.MapListener;
import xoanaraujo.gdx01.view.animation.AnimatiomType;

import java.util.EnumMap;

import static xoanaraujo.gdx01.util.GameConst.UNIT_SCALE;

public class GameRenderer implements Disposable, MapListener{

    private static final String TAG = GameRenderer.class.getSimpleName();
    private final Color backgroundColor;
    private final OrthographicCamera camera;
    private final ExtendViewport viewport;
    private final SpriteBatch batch;
    private final AssetManager assetManager;
    private final EnumMap<AnimatiomType, Animation<Sprite>> animationCache;
    private final ObjectMap<String, TextureRegion[][]> regionCache;
    private final ImmutableArray<Entity> animatedEntities;
    private final ImmutableArray<Entity> gameObjectEntities;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Array<TiledMapTileLayer> tileMapLayers;

    private final GLProfiler profiler;
    private final Box2DDebugRenderer debugRenderer;
    private final World world;
    private final RayHandler rayHandler;
    private IntMap<Animation<Sprite>> mapAnimations;

    public GameRenderer(final Core context) {
        backgroundColor = new Color(0.1f, 0.1f, 0.1f, 1f);
        assetManager = context.getAssetManager();
        viewport = context.getViewport();
        camera = context.getCamera();
        batch = context.getBatch();


        animationCache = new EnumMap<>(AnimatiomType.class);
        regionCache = new ObjectMap<>();

        gameObjectEntities = context.getEcsEngine().getEntitiesFor(Family.all(GameObjectComponent.class, AnimationComponent.class, Box2DComponent.class).get());
        animatedEntities = context.getEcsEngine().getEntitiesFor(Family.all(AnimationComponent.class, Box2DComponent.class).exclude(GameObjectComponent.class).get()); // Se actualiza solo con el engine

        mapRenderer = new OrthogonalTiledMapRenderer(null, UNIT_SCALE, batch);
        context.getMapManager().addMapListener(this);
        tileMapLayers = new Array<>();

        profiler = new GLProfiler(Gdx.graphics);
        profiler.disable();
        if (profiler.isEnabled()) {
            debugRenderer = new Box2DDebugRenderer();
            world = context.getWorld();
        } else {
            debugRenderer = null;
            world = null;
        }
        rayHandler = context.getRayHandler();
    }

    public void render(final float alpha) {
        ScreenUtils.clear(backgroundColor);

        viewport.apply(false);

        mapRenderer.setView(camera);
        batch.begin();
        // First render the map
        if (mapRenderer.getMap() != null) {
            AnimatedTiledMapTile.updateAnimationBaseTime();
            for (final TiledMapTileLayer layer : tileMapLayers) {
                mapRenderer.renderTileLayer(layer);
            }
        }
        // Render gameObjects
        for (final Entity entity : gameObjectEntities){
            renderGameObject(entity, alpha);
        }
        // Render animations (Player animations, efects, etc.)
        for (final Entity entity : animatedEntities) {
            renderEntity(entity, alpha);
        }
        batch.end();

        rayHandler.setCombinedMatrix(camera);
        rayHandler.updateAndRender();

        if (profiler.isEnabled()) {
            Gdx.app.debug(TAG, "Bindings: " + profiler.getTextureBindings());
            Gdx.app.debug(TAG, "Drawcalls: " + profiler.getDrawCalls());
            debugRenderer.render(world, camera.combined);
            profiler.reset();
        }

    }

    private void renderGameObject(Entity entity, float alpha) {
        final Box2DComponent box2DComponent = ECSEngine.box2DComponentMapper.get(entity);
        final GameObjectComponent gameObjectComponent = ECSEngine.gameObjectComponentMapper.get(entity);

        if (gameObjectComponent.animationIndex != -1) {
            final AnimationComponent animationComponent = ECSEngine.animationComponentMapper.get(entity);
            Gdx.app.debug(TAG, "" + animationComponent.time);
            final Animation<Sprite> animation = mapAnimations.get(gameObjectComponent.animationIndex);
            final Sprite keyFrame = animation.getKeyFrame(animationComponent.time);
            keyFrame.setOriginCenter();
            keyFrame.setBounds(
                    box2DComponent.renderPosition.x - box2DComponent.width / 2,
                    box2DComponent.renderPosition.y - box2DComponent.height / 2,
                    animationComponent.width,
                    animationComponent.height);
            keyFrame.setRotation(box2DComponent.body.getAngle() * MathUtils.radDeg);
            keyFrame.draw(batch);
        } else {
            Gdx.app.debug(TAG, "GameObject component doesnt have animation? Idk reed the code");
        }

        box2DComponent.renderPosition.lerp(box2DComponent.body.getPosition(), alpha);


    }

    private void renderEntity(Entity entity, float alpha) {
        final Box2DComponent box2DComponent = ECSEngine.box2DComponentMapper.get(entity);
        final AnimationComponent animationComponent = ECSEngine.animationComponentMapper.get(entity);

        if (animationComponent.type != null) {
            final Animation<Sprite> animation = getAnimation(animationComponent.type);
            final Sprite keyFrame = animation.getKeyFrame(animationComponent.time);
            keyFrame.setBounds(
                    box2DComponent.renderPosition.x - animationComponent.width * 0.55f,
                    box2DComponent.renderPosition.y - animationComponent.height * 0.3f,
                    animationComponent.width,
                    animationComponent.height);
            keyFrame.draw(batch);
        } else {
            Gdx.app.debug(TAG, "Animation component doesnt have a AnimationType");
        }

        box2DComponent.renderPosition.lerp(box2DComponent.body.getPosition(), alpha);


        // dummySprite.setBounds(
        //         box2DComponent.renderPosition.x - box2DComponent.width * 0.5f,
        //         box2DComponent.renderPosition.y - box2DComponent.height * 0.5f,
        //         box2DComponent.width,
        //         box2DComponent.height);
        // dummySprite.draw(batch);
    }

    private Animation<Sprite> getAnimation(AnimatiomType type) {
        Animation<Sprite> animation = animationCache.get(type);
        if (animation == null) {
            Gdx.app.debug(TAG, "Creating new animation of type " + type);

            final TextureAtlas.AtlasRegion atlasRegion = assetManager.get(type.getPath(), TextureAtlas.class).findRegion(type.getAtlasKey());
            TextureRegion[][] textureRegions = atlasRegion.split(48, 48);
            animation = new Animation<>(
                    type.getFrameTime(),
                    getKeyFrames(textureRegions[type.getRowIndex()]));
            animation.setPlayMode(Animation.PlayMode.LOOP);
            animationCache.put(type, animation);
        }
        return animation;
    }

    private Sprite[] getKeyFrames(TextureRegion[] textureRegions) {
        final Sprite[] keyFrames = new Sprite[textureRegions.length];
        for (int i = 0; i < keyFrames.length; i++) {
            TextureRegion region = textureRegions[i];
            Sprite sprite = new Sprite(region);
            sprite.setOriginCenter();
            keyFrames[i] = sprite;
        }
        return keyFrames;
    }

    @Override
    public void dispose() {
        if (debugRenderer != null) {
            debugRenderer.dispose();
        }
        mapRenderer.dispose();
    }

    @Override
    public void mapChange(GameMap currentMap) {
        mapRenderer.setMap(currentMap.getTiledMap());
        currentMap.getTiledMap().getLayers().getByType(TiledMapTileLayer.class, tileMapLayers);
        mapAnimations = currentMap.getMapAnimations();
    }
}
