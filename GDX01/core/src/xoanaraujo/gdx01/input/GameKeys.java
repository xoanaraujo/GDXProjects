package xoanaraujo.gdx01.input;

import com.badlogic.gdx.Input;

public enum GameKeys {
    UP(Input.Keys.W, Input.Keys.UP),
    DOWN(Input.Keys.S, Input.Keys.DOWN),
    LEFT(Input.Keys.A, Input.Keys.LEFT),
    RIGHT(Input.Keys.D, Input.Keys.RIGHT),
    SELECT(Input.Keys.SPACE, Input.Keys.ENTER);

    final int[] keys;

    GameKeys(int... key) {
        this.keys = key;
    }
}
