package xoanaraujo.gdx01.map.gameobject;

import com.badlogic.gdx.graphics.g2d.Animation;

public enum GameObjectType {
    CHEST(Animation.PlayMode.NORMAL), BOX(Animation.PlayMode.NORMAL);

    private final Animation.PlayMode playMode;

    GameObjectType(Animation.PlayMode playMode) {
        this.playMode = playMode;
    }

    public Animation.PlayMode getPlayMode() {
        return playMode;
    }
}
