package xoanaraujo.gdx01.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class LoadingUI extends Table {
    private static final String TAG = LoadingUI.class.getSimpleName();
    private final ProgressBar progressBar;
    private final TextButton loadingTextButton;
    private boolean loadingOk, pressOk;

    public LoadingUI(Skin skin) {
        super(skin);
        setFillParent(true);
        loadingOk = false;
        pressOk = false;
        progressBar = new ProgressBar(0, 1, 0.01f, false, skin, "default");
        progressBar.setAnimateDuration(0.1f);
        loadingTextButton = new TextButton("Loading", skin, "huge");
        loadingTextButton.getLabel().setWrap(true);

        add(loadingTextButton).expandX().fillX().bottom().row();
        add(progressBar).expandX().fillX().bottom().pad(20f);
        bottom();
        //setDebug(true, true); //TODO Remove when not debugging
    }

    public void updateProgressBar(final float progress){
        progressBar.setValue(progress);

        if (progress == 1 && progressBar.getVisualValue() == 1){
            if (!loadingOk){ // TODO Pensar en otro momento si tiene sentido comprobarlo en cada frame
                loadingOk = true;
                loadingTextButton.getLabel().setText("Press to continue...");
                loadingTextButton.setColor(1, 1, 1, 0);
                loadingTextButton.addAction(Actions.forever(Actions.sequence(Actions.alpha(1, 1.5f), Actions.alpha(0, 1.5f))));
            }
        }
    }
}
