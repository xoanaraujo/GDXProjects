package xoanaraujo.gdx01.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import jdk.incubator.vector.ByteVector;
import xoanaraujo.gdx01.map.CollisionArea;

public class Player {
    private Body body;
    private Vector2 direction;
    private boolean directionChanged;

    public Player(Body body) {
        this.body = body;
        direction = new Vector2(0, 0);
        directionChanged = false;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void move(){
        if(directionChanged){
            direction.nor();
            body.applyLinearImpulse(
                    (direction.x * 4 - body.getLinearVelocity().x) * body.getMass(),
                    (direction.y * 4 - body.getLinearVelocity().y) * body.getMass(),
                    body.getWorldCenter().x, body.getWorldCenter().y,true
            );
        }
    }

    public boolean isDirectionChanged() {
        return directionChanged;
    }

    public void setDirectionChanged(boolean directionChanged) {
        this.directionChanged = directionChanged;
    }
}
