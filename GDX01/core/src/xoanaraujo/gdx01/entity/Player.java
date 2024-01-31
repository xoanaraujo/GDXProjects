package xoanaraujo.gdx01.entity;

import com.badlogic.gdx.physics.box2d.Body;
import xoanaraujo.gdx01.map.CollisionArea;

public class Player {
    private Body body;

    public Player(Body body) {
        this.body = body;
    }
}
