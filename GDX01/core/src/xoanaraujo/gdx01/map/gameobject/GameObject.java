package xoanaraujo.gdx01.map.gameobject;

import com.badlogic.gdx.math.Vector2;

public class GameObject {
    private final GameObjectType type;
    private final Vector2 position;
    private final float width, height, rotationDegrees;
    private final int animationIndex;

    public GameObject(GameObjectType type, Vector2 position, float width, float height, float rotationDegrees, int animationIndex) {
        this.type = type;
        this.position = position;
        this.width = width;
        this.height = height;
        this.rotationDegrees = rotationDegrees;
        this.animationIndex = animationIndex;
    }

    public GameObjectType getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRotationDegrees() {
        return rotationDegrees;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }
}
