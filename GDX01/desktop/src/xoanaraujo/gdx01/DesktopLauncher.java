package xoanaraujo.gdx01;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import static xoanaraujo.gdx01.util.GameConst.HEIGHT;
import static xoanaraujo.gdx01.util.GameConst.WIDTH;


// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("GDX01");
        config.setForegroundFPS(60);
        config.setWindowedMode(WIDTH, (HEIGHT));
        new Lwjgl3Application(new Core(), config);
    }
}
