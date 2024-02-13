package xoanaraujo.gdx01;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static xoanaraujo.gdx01.util.GameConst.BIT_GAME_OBJECT;
import static xoanaraujo.gdx01.util.GameConst.BIT_PLAYER;

public class WorldContactListener implements ContactListener {
    private Array<PlayerCollisionListener> listeners;
    private static final String TAG = WorldContactListener.class.getSimpleName();

    public WorldContactListener() {
        listeners = new Array<>();
    }

    @Override
    public void beginContact(Contact contact) {
        final Entity player;
        final Entity gameObject;
        final Body bodyA = contact.getFixtureA().getBody();
        final Body bodyB = contact.getFixtureB().getBody();
        final short categoryA = contact.getFixtureA().getFilterData().categoryBits;
        final short categoryB = contact.getFixtureB().getFilterData().categoryBits;

        if ((categoryA & BIT_PLAYER) == BIT_PLAYER){
            player = (Entity) bodyA.getUserData();
        } else if ((categoryB & BIT_PLAYER) == BIT_PLAYER){
            player = (Entity) bodyB.getUserData();
        } else
            return;

        if ((categoryA & BIT_GAME_OBJECT) == BIT_GAME_OBJECT){
            gameObject = (Entity) bodyA.getUserData();
        } else if ((categoryB & BIT_GAME_OBJECT) == BIT_GAME_OBJECT){
            gameObject = (Entity) bodyB.getUserData();
        } else
            return;
        Gdx.app.debug(TAG, "PLAYER COLLIDE WITH GAMEOBJECT");
        for (PlayerCollisionListener listener : listeners) {
            listener.playerCollision(player, gameObject);
        }
    }

    @Override
    public void endContact(Contact contact) {
        // No logic needed here (For now)
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public void addListener (PlayerCollisionListener listener){
        listeners.add(listener);
    }
    public interface PlayerCollisionListener {
        void playerCollision(final Entity player, final Entity gameObject);
    }

}
