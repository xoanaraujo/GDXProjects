package xoanaraujo.gdx01.view.animation;

public enum AnimatiomType {
    PLAYER_IDLE_DOWN("entity/player.atlas", "player", 0.2f, 0),
    PLAYER_IDLE_UP("entity/player.atlas", "player", 0.2f, 2),
    PLAYER_MOVE_DOWN("entity/player.atlas", "player", 0.1f, 3),
    PLAYER_MOVE_RIGHT("entity/player.atlas", "player", 0.1f, 4),
    PLAYER_MOVE_LEFT("entity/player.atlas", "player", 0.1f, 5),
    PLAYER_MOVE_UP("entity/player.atlas", "player", 0.1f, 6);

    private final String path, atlasKey;
    private final float frameTime;
    private final int rowIndex;

    AnimatiomType(String path, String atlasKey, float frameTime, int rowIndex) {
        this.path = path;
        this.atlasKey = atlasKey;
        this.frameTime = frameTime;
        this.rowIndex = rowIndex;
    }

    public String getPath() {
        return path;
    }

    public String getAtlasKey() {
        return atlasKey;
    }

    public float getFrameTime() {
        return frameTime;
    }

    public int getRowIndex() {
        return rowIndex;
    }
}
