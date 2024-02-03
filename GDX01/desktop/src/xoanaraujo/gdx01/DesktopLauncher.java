package xoanaraujo.gdx01;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import xoanaraujo.gdx01.util.GameConst;

import static xoanaraujo.gdx01.util.GameConst.UNIT_SCALE;


// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    private static final float SCALE = 3f;

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("GDX01");
        config.setForegroundFPS(60);
        config.setWindowedMode(GameConst.WIDTH, (GameConst.HEIGHT));
        new Lwjgl3Application(new Core(), config);
    }
}
