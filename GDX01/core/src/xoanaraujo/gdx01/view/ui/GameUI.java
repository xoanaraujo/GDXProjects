package xoanaraujo.gdx01.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;

import xoanaraujo.gdx01.Core;

public class GameUI extends AbstractUI {
    private final TextButton txtButtonFPS;
    private StringBuilder fpsBuilder;
    public GameUI(Core context) {
        super(context);
        txtButtonFPS = new TextButton("[bright_black]0", getSkin(), "big");
        fpsBuilder = txtButtonFPS.getLabel().getText();
        setFillParent(true); // Fills the hole screen with this GameUI Table subclass
        top();
        right();

        txtButtonFPS.getLabel().setWrap(true);
        txtButtonFPS.getLabel().setAlignment(Align.right);
        add(txtButtonFPS).pad(10).padRight(30).size(200f, 50f);
    }

    public void updateFPS(int fps){
        fpsBuilder.setLength(0);
        txtButtonFPS.setText("[debug]" + Gdx.graphics.getFramesPerSecond());
    }
}
