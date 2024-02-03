package xoanaraujo.gdx01.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {
    private final GameKeys[] keyMapping;
    private final boolean[] keyState;
    private final Array<GameInputListener> listeners;

    public InputManager() {
        this.keyMapping = new GameKeys[256];
        for (GameKeys gameKey : GameKeys.values()) {
            for (int code : gameKey.keys) {
                keyMapping[code] = gameKey;
            }
        }

        keyState = new boolean[GameKeys.values().length];
        listeners = new Array<>();
    }

    public void addInputListener(final GameInputListener listener){
        listeners.add(listener);
    }

    public void removeInputListener(final GameInputListener listener){
        listeners.removeValue(listener, true);
    }

    @Override
    public boolean keyDown(int keycode) {
        final GameKeys gameKey = keyMapping[keycode];
        if (gameKey == null){
            return false;
        }
        notifyKeyDown(gameKey);
        return true;
    }

    public void notifyKeyDown(final GameKeys gameKey){
        keyState[gameKey.ordinal()] = true;
        for (GameInputListener listener : listeners) {
            listener.keyDown(this, gameKey);
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        final GameKeys gameKey = keyMapping[keycode];
        if (gameKey == null){
            return false;
        }
        notifyKeyUp(gameKey);
        return true;
    }

    public void notifyKeyUp(final GameKeys gameKey){
        keyState[gameKey.ordinal()] = false;
        for (GameInputListener listener : listeners) {
            listener.keyUp(this, gameKey);
        }
    }

    public boolean isKeyPressed(GameKeys gameKey){
        return  keyState[gameKey.ordinal()];
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
