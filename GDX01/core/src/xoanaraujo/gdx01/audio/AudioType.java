package xoanaraujo.gdx01.audio;

public enum AudioType {
    MAIN("audio/main.wav", true, 0.3f),
    ZELDA("audio/zelda_music.wav", true, 0.3f);
    private final String path;
    private final boolean isMusic;
    private final float volume;

    AudioType(String path, boolean isMusic, float volume) {
        this.path = path;
        this.isMusic = isMusic;
        this.volume = volume;
    }

    public String getPath() {
        return path;
    }

    public boolean isMusic() {
        return isMusic;
    }

    public float getVolume() {
        return volume;
    }
}
