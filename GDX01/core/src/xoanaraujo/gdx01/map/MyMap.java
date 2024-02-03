package xoanaraujo.gdx01.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static xoanaraujo.gdx01.util.GameConst.UNIT_SCALE;

public class MyMap {
    private static final String TAG = MyMap.class.getSimpleName();
    private TiledMap tiledMap;
    private Array<CollisionArea> collisionAreas;
    private Vector2 startPlayerLocation;

    public MyMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        this.collisionAreas = new Array<>();
        this.startPlayerLocation = new Vector2();
        parsePLayerLocation();
        parseCollisionLayer();
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
        MapLayer startPlayerLocationLayer =tiledMap.getLayers().get("startPlayerLocation");
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
}
