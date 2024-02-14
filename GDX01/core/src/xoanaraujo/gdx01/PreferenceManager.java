package xoanaraujo.gdx01;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import xoanaraujo.gdx01.ecs.ECSEngine;
import xoanaraujo.gdx01.ecs.components.Box2DComponent;

public class PreferenceManager {
    private final Preferences preferences;

    public PreferenceManager() {
        this.preferences = Gdx.app.getPreferences("gdx01Preferences");
    }

    public boolean containsKey(String key) {
        return preferences.contains(key);
    }

    public void setFloatValue(String keu, Float value) {
        preferences.putFloat(keu, value);
        preferences.flush();
    }

    public Float getFloatValue(String key) {
        return preferences.getFloat(key);
    }

    public void saveGameState(final Entity player) {
        final Box2DComponent box2DComponent = ECSEngine.box2DComponentMapper.get(player);
        preferences.putFloat("player_x", box2DComponent.body.getPosition().x);
        preferences.putFloat("player_y", box2DComponent.body.getPosition().y);
        preferences.flush();
    }

    public void loadGameState(final Entity player){
        float playerX = preferences.getFloat("player_x");
        float playerY = preferences.getFloat("player_y");

        final Box2DComponent box2DComponent = ECSEngine.box2DComponentMapper.get(player);

        box2DComponent.body.setTransform(playerX, playerY, box2DComponent.body.getAngle());
    }
}
