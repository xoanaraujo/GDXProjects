package xoanaraujo.gdx01.map;

public enum MapType {
    MAP_1("map/map.tmx");

    private final String path;

    MapType(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
