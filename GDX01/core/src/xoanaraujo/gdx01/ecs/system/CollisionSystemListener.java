package xoanaraujo.gdx01.ecs.system;

import xoanaraujo.gdx01.map.GameMap;

public interface CollisionSystemListener {
    void updateMapObjects(GameMap currentGameMap);
}
