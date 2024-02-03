package xoanaraujo.gdx01.input;

public interface GameInputListener {
    void keyDown(final InputManager manager, final GameKeys gameKey);
    void keyUp(final InputManager manager, final GameKeys gameKey);
}
