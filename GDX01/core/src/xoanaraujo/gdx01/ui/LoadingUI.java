package xoanaraujo.gdx01.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;

import xoanaraujo.gdx01.Core;
import xoanaraujo.gdx01.input.GameKeys;
import xoanaraujo.gdx01.input.InputManager;

public class LoadingUI extends AbstractUI {
    private static final String TAG = LoadingUI.class.getSimpleName();
    private final ProgressBar progressBar;
    private boolean loadingOk;
    private final TextButton loadingTextButton;
    private final StringBuilder loadingStrBuilder;

    public LoadingUI(Core context) {
        super(context);
        setFillParent(true);
        loadingOk = false;
        loadingTextButton = new TextButton(i18NBundle.format("loading"), getSkin(), "huge");
        loadingStrBuilder = loadingTextButton.getLabel().getText();

        progressBar = new ProgressBar(0, 1, 0.01f, false, getSkin(), "default");

        progressBar.setAnimateDuration(1f);
        loadingTextButton.getLabel().setWrap(true);
        loadingTextButton.getLabel().setAlignment(Align.bottom);
        add(loadingTextButton).expand().fill().bottom().row();
        add(progressBar).expandX().fillX().pad(20f);
        bottom();
        center();
        loadingTextButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                context.getInputManager().notifyKeyDown(GameKeys.SELECT);
                return true;
            }
        });
        // setDebug(true, true); //TODO Remove when not debugging
    }

    public void updateProgressBar(final float progress){
        progressBar.setValue(progress);

        if (progress == 1 && progressBar.getVisualValue() == 1){
            if (!loadingOk){ // TODO Pensar en otro momento si tiene sentido comprobarlo en cada frame
                loadingOk = true;
                loadingTextButton.getLabel().setText(i18NBundle.format("anyKey"));
                loadingTextButton.setColor(1, 1, 1, 0);
                loadingTextButton.addAction(Actions.forever(Actions.sequence(Actions.alpha(1, 1.5f), Actions.alpha(0, 1.5f))));
            }
        } else {
            loadingStrBuilder.setLength(0);
            loadingStrBuilder.append(i18NBundle.format("loading"));
            loadingStrBuilder.append("(");
            loadingStrBuilder.append(progress * 100).append("%");
            loadingStrBuilder.append(")");
            loadingTextButton.getLabel().invalidateHierarchy();
        }
    }
}
