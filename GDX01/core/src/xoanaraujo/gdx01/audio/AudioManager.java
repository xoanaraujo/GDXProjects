package xoanaraujo.gdx01.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import xoanaraujo.gdx01.Core;

public class AudioManager {
    private final AssetManager assetManager;
    private AudioType currentMusicType;
    private Music currentMusic;

    public AudioManager(Core context) {
        this.assetManager = context.getAssetManager();
        currentMusic = null;
        currentMusicType = null;
    }

    public void playAudio(final AudioType audioType) {
        if (audioType.isMusic()) {
            if (currentMusicType == audioType) {
                // The music is already being played
                return;
            }else if (currentMusic != null) {
                currentMusic.stop();
            }
            currentMusicType = audioType;
            currentMusic = assetManager.get(audioType.getPath(), Music.class);
            currentMusic.setLooping(true);
            currentMusic.setVolume(audioType.getVolume());
            currentMusic.play();
        } else {
            assetManager.get(audioType.getPath(), Sound.class).play(audioType.getVolume());
        }
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
            currentMusicType = null;
        }
    }
}
