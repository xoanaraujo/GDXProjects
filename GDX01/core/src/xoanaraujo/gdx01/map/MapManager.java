package xoanaraujo.gdx01.map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.ecs.system.CollisionSystem;
import xoanaraujo.gdx01.ecs.system.CollisionSystemListener;
import xoanaraujo.gdx01.map.gameobject.GameObject;
import xoanaraujo.gdx01.util.GameConst;

import java.util.EnumMap;

import static xoanaraujo.gdx01.util.GameConst.*;

public class MapManager {
    private static final String TAG = MapManager.class.getSimpleName();
    private final World world;
    private final ECSEngine ecsEngine;
    private final CollisionSystemListener collisionSystemListener;
    private final Array<Entity> gameObjectsToRemove;
    private final Array<Body> bodies;
    private final AssetManager assetManager;
    private final MapType currentMapType;
    private GameMap currentMap;
    private final EnumMap<MapType, GameMap> mapCache;
    private Array<MapListener> listeners;

    public MapManager(Core context) {
        world = context.getWorld();
        ecsEngine = context.getEcsEngine();
        collisionSystemListener =ecsEngine.getSystem(CollisionSystem.class);
        gameObjectsToRemove = new Array<>();
        assetManager = context.getAssetManager();
        bodies = new Array<>();
        mapCache = new EnumMap<>(MapType.class);
        listeners = new Array<>();
        currentMapType = null;
        currentMap = null;
    }

    public void addMapListener(MapListener listener) {
        listeners.add(listener);
    }

    public void setMap(MapType mapType) {
        if (currentMapType == mapType)
            return;
        if (currentMap != null){
            world.getBodies(bodies);
            destroyCollisions();
            destroyGameObjects();
        }
        Gdx.app.debug(TAG, "Changing to map " + mapType);
        currentMap = mapCache.get(mapType);
        if (currentMap == null){
            Gdx.app.debug(TAG, "Creating a new map of type " + mapType);
            final TiledMap tiledMap = assetManager.get(mapType.getPath(), TiledMap.class);
            currentMap = new GameMap(tiledMap);
            mapCache.put(mapType, currentMap);
        }

        spawnCollisionAreas();
        spawnGameObjects();

        for (final MapListener listener : listeners) {
            listener.mapChange(currentMap);
        }
        collisionSystemListener.updateMapObjects(currentMap);
    }

    private void spawnGameObjects() {
        for (final GameObject gameObject : currentMap.getGameObjects()) {
            ecsEngine.createGameObject(gameObject);
        }
    }

    private void destroyGameObjects() { // Double array needed. Every time you delete an entity all the others move 1 step forward to reallocate
        for (Entity entity : ecsEngine.getEntities()) {
            if (ECSEngine.gameObjectComponentMapper.get(entity) != null){
                gameObjectsToRemove.add(entity);
            }
        }
        for (final Entity entity : gameObjectsToRemove) {
            ecsEngine.removeEntity(entity);
        }
        gameObjectsToRemove.clear();
    }

    private void destroyCollisions(){
        for (final Body body : bodies){
            if (body.getUserData().equals("GROUND"))
                world.destroyBody(body);
        }
    }

    private void spawnCollisionAreas(){
        GameConst.resetBodyAndFixtureDefinition();
        for (final CollisionArea collisionArea : currentMap.getCollisionAreas()) {
            BODY_DEF.position.set(collisionArea.getX(), collisionArea.getY());
            BODY_DEF.fixedRotation = true;
            final Body body = world.createBody(BODY_DEF);
            body.setUserData("GROUND");

            FIXTURE_DEF.filter.categoryBits = BIT_GROUND;
            FIXTURE_DEF.filter.maskBits = -1;
            final ChainShape chainShape = new ChainShape();
            chainShape.createChain(collisionArea.getVertices());
            FIXTURE_DEF.shape = chainShape;
            body.createFixture(FIXTURE_DEF);
            chainShape.dispose();
        }
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }
}
