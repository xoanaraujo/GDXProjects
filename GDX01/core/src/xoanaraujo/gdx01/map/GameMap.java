package xoanaraujo.gdx01.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

import static xoanaraujo.gdx01.util.GameConst.UNIT_SCALE;

public class GameMap {
    private static final String TAG = GameMap.class.getSimpleName();
    private TiledMap tiledMap;
    private Array<CollisionArea> collisionAreas;
    private Array<GameObject> gameObjects;
    private IntMap<Animation<Sprite>> mapAnimations;
    private Vector2 startPlayerLocation;

    public GameMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        this.collisionAreas = new Array<>();
        this.startPlayerLocation = new Vector2();
        gameObjects = new Array<>();
        mapAnimations = new IntMap<>();

        parsePLayerLocation();
        parseCollisionLayer();
        // parseGameObjectLayer();
    }

    private void parseGameObjectLayer() {
        final MapLayer mapLayer = tiledMap.getLayers().get("gameObjects");
        if (mapLayer == null){
            Gdx.app.debug(TAG, "There is no gameObjects layer");
            return;
        }
        final MapObjects objects = mapLayer.getObjects();
        for (MapObject object : objects) {
            if (object instanceof TiledMapTileMapObject){
                final TiledMapTileMapObject tiledMapObj = (TiledMapTileMapObject) object;
                final MapProperties tiledMapObjProperties = tiledMapObj.getProperties();
                final MapProperties tileProperties = tiledMapObj.getTile().getProperties();
                final GameObjectType objectType;
                if (tiledMapObjProperties.containsKey("type")){
                    objectType = GameObjectType.valueOf(tiledMapObjProperties.get("type", String.class));
                } else if (tileProperties.containsKey("type")) {
                    objectType = GameObjectType.valueOf(tileProperties.get("type", String.class));
                } else {
                    Gdx.app.debug(TAG, "There is no gameObjectType for " + tiledMapObjProperties.get("id", Integer.class));
                    continue;
                }


                final int animationIndex = tiledMapObj.getTile().getId();
                if (!createAnimation(animationIndex, tiledMapObj.getTile())){
                    Gdx.app.debug(TAG, "Could not create animation for tile "+ tiledMapObjProperties.get("id", Integer.class));
                    continue;
                }

                final float width = tiledMapObjProperties.get("width", Float.class) * UNIT_SCALE;
                final float height = tiledMapObjProperties.get("height", Float.class) * UNIT_SCALE;
                gameObjects.add(new GameObject(
                        objectType,
                        new Vector2(tiledMapObj.getX() * UNIT_SCALE, tiledMapObj.getY() * UNIT_SCALE),
                        width,
                        height, tiledMapObj.getRotation(), animationIndex));
            }else{
                Gdx.app.debug(TAG, "GameObject " + object + " is not supported");
            }
        }

    }

    private boolean createAnimation(int animationIndex, TiledMapTile tile) {
        Animation<Sprite> animation = mapAnimations.get(animationIndex);
        if (animation == null){
            Gdx.app.debug(TAG, "Creating a new map animation for tile " + tile.getId());
            if (tile instanceof AnimatedTiledMapTile){
                final AnimatedTiledMapTile aniTile = (AnimatedTiledMapTile) tile;
                final Sprite[] keyFrames = new Sprite[aniTile.getFrameTiles().length];
                int i = 0;
                for (final StaticTiledMapTile staticTile : aniTile.getFrameTiles()) {
                    keyFrames[i++] = new Sprite(staticTile.getTextureRegion());
                }
                animation = new Animation<>(aniTile.getAnimationIntervals()[0] * 0.001f); // Libgdx just saves 1 frame-time  per animation, so we only check the first value. Also, the time is in milliseconds, so we convert it to secs.
                animation.setPlayMode(Animation.PlayMode.LOOP);
            } else if (tile instanceof StaticTiledMapTile) {
                animation = new Animation<>(0, new Sprite(tile.getTextureRegion()));
                mapAnimations.put(animationIndex, animation);
            } else {
                Gdx.app.debug(TAG, "Tile of type " + tile + "is not supported for map animations");
                return false;
            }
        }

        return true;
    }

    private void parseCollisionLayer() {
        final MapLayer collisionLayer = tiledMap.getLayers().get("collision");
        final MapObjects mapObjects = collisionLayer.getObjects();
        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof RectangleMapObject) {
                final RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
                final Rectangle rectangle = rectangleMapObject.getRectangle();
                final float[] vertices = new float[10];
                // left-bot
                vertices[0] = 0;
                vertices[1] = 0;
                // left-top
                vertices[2] = 0;
                vertices[3] = rectangle.height;
                // right-top
                vertices[4] = rectangle.width;
                vertices[5] = rectangle.height;
                // right-bot
                vertices[6] = rectangle.width;
                vertices[7] = 0;
                // left-bot
                vertices[8] = 0;
                vertices[9] = 0;

                collisionAreas.add(new CollisionArea(rectangle.x, rectangle.y, vertices));

            } else if (mapObject instanceof PolylineMapObject) {
                final PolylineMapObject polylineMapObject = (PolylineMapObject) mapObject;
                final Polyline polyline = polylineMapObject.getPolyline();
                collisionAreas.add(new CollisionArea(polyline.getX(), polyline.getY(), polyline.getVertices()));
            } else if (mapObject instanceof PolygonMapObject) {
                final PolygonMapObject polygonMapObject = (PolygonMapObject) mapObject;
                final Polygon polygon = polygonMapObject.getPolygon();
                collisionAreas.add(new CollisionArea(polygon.getX(), polygon.getY(), polygon.getVertices()));
            } else {
                Gdx.app.debug(TAG, "MapObject of type " + mapObject.getClass() + " not allowed");
                
            }
        }

    }

    private void parsePLayerLocation(){
        MapLayer startPlayerLocationLayer =tiledMap.getLayers().get("spawnLocation");
        final MapObjects mapObjects = startPlayerLocationLayer.getObjects();
        for (MapObject mapObject : mapObjects) {
            if (mapObject instanceof RectangleMapObject) {
                final RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
                final Rectangle rectangle = rectangleMapObject.getRectangle();

                startPlayerLocation.set(rectangle.getX() * UNIT_SCALE, rectangle.getY() * UNIT_SCALE);
            }
        }
    }


    public Array<CollisionArea> getCollisionAreas() {
        return collisionAreas;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Vector2 getStartPlayerLocation() {
        return startPlayerLocation;
    }

    public Array<GameObject> getGameObjects() {
        return gameObjects;
    }

    public IntMap<Animation<Sprite>> getMapAnimations() {
        return mapAnimations;
    }
}
