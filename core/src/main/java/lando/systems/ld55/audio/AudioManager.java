package lando.systems.ld55.audio;

import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import lando.systems.ld55.assets.Assets;

public class AudioManager implements Disposable {

    public MutableFloat soundVolume;
    public MutableFloat musicVolume;

    public static boolean isMusicMuted;
    public static boolean isSoundMuted;

    // none should not have a sound
    public enum Sounds {
        none
        , coin
        , idle_click
        , pew
        , click
        , select
        , lockIn
        , metalTap
        , error_sound
        , error_buzz
        , level_up
        , horn_fanfare
        , cucaracha_fanfare
        , victory_fanfare
        , funeral_dirge
        , enemy_spawn
        , round_ring
        , hop
        , combat_hit
        , spawn_evil_start // tentacles appear
        , spawn_evil_end // game piece completely rendered
        , spawn_evil_move_start // movement sound from spawn location
        , spawn_good_start // fanfare trumpets
        , spawn_good_end
        , spawn_good_move_start

    }

    public enum Musics {
        none
        , introMusic
        , mainMusic
        , outroMusic
    }

    public ObjectMap<Sounds, SoundContainer> sounds = new ObjectMap<>();
    public ObjectMap<Musics, Music> musics = new ObjectMap<>();

    public Music currentMusic;
    public Musics eCurrentMusic;
    public Music oldCurrentMusic;

    private final Assets assets;
    private final TweenManager tween;

    public AudioManager(Assets assets, TweenManager tween) {
        this.assets = assets;
        this.tween = tween;

        putSound(Sounds.coin, assets.coin);
        putSound(Sounds.idle_click, assets.idleClick);
        putSound(Sounds.pew, assets.pew);
        putSound(Sounds.click, assets.click);
        putSound(Sounds.lockIn, assets.lockIn);
        putSound(Sounds.select, assets.select);
        putSound(Sounds.metalTap, assets.metalTap);
        putSound(Sounds.level_up, assets.levelUp);
//        putSound(Sounds.error, assets.errorThud);
        putSound(Sounds.error_sound, assets.errorThud);
        putSound(Sounds.error_buzz, assets.errorBuzz);
        putSound(Sounds.horn_fanfare, assets.hornFanfare);
        putSound(Sounds.spawn_good_start, assets.hornFanfare);
        putSound(Sounds.victory_fanfare, assets.victoryFanfare);
        putSound(Sounds.funeral_dirge, assets.funeralDirge);
        putSound(Sounds.spawn_evil_start, assets.squelch1);
        putSound(Sounds.spawn_evil_start, assets.squelch2);
        putSound(Sounds.spawn_evil_start, assets.squelch3);
        putSound(Sounds.spawn_evil_start, assets.squelch4);
        putSound(Sounds.cucaracha_fanfare, assets.cucarachaFanfare);
        putSound(Sounds.enemy_spawn, assets.enemySpawn);
        putSound(Sounds.spawn_evil_start, assets.enemySpawn);
        putSound(Sounds.round_ring, assets.roundRing);
        putSound(Sounds.hop, assets.hop);
        putSound(Sounds.combat_hit, assets.hit1);
        putSound(Sounds.combat_hit, assets.hit2);
        putSound(Sounds.combat_hit, assets.hit3);
        putSound(Sounds.combat_hit, assets.hit4);
        putSound(Sounds.combat_hit, assets.hit5);
        putSound(Sounds.combat_hit, assets.hit6);

        musics.put(Musics.introMusic, assets.introMusic);
        musics.put(Musics.mainMusic, assets.mainMusic);
        musics.put(Musics.outroMusic, assets.outroMusic);

        musicVolume = new MutableFloat(0.4f);
        soundVolume = new MutableFloat(0.6f);

        isMusicMuted = false;
        isSoundMuted = false;

    }

    public void update(float dt) {
        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume.floatValue());
            currentMusic.play();
        }

        if (oldCurrentMusic != null) {
            oldCurrentMusic.setVolume(musicVolume.floatValue());
        }
    }

    @Override
    public void dispose() {
        Sounds[] allSounds = Sounds.values();
        for (Sounds sound : allSounds) {
            if (sounds.get(sound) != null) {
                sounds.get(sound).dispose();
            }
        }
        Musics[] allMusics = Musics.values();
        for (Musics music : allMusics) {
            if (musics.get(music) != null) {
                musics.get(music).dispose();
            }
        }
        currentMusic = null;
    }

    public void putSound(Sounds soundType, Sound sound) {
        SoundContainer soundCont = sounds.get(soundType);
        if (soundCont == null) {
            soundCont = new SoundContainer();
        }

        soundCont.addSound(sound);
        sounds.put(soundType, soundCont);
    }

    public long playSound(Sounds soundOption) {
        if (isSoundMuted || soundOption == Sounds.none) return -1;
        return playSound(soundOption, soundVolume.floatValue());
    }

    public long playSound(Sounds soundOption, float volume) {
        volume = volume * soundVolume.floatValue();
        if (isSoundMuted || soundOption == Sounds.none) return -1;

        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont == null) {
            // Gdx.app.log("NoSound", "No sound found for " + soundOption.toString());
            return 0;
        }

        Sound s = soundCont.getSound();
        return (s != null) ? s.play(volume) : 0;
    }

    public void stopSound(Sounds soundOption, long soundId) {
        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont == null) {
            // Gdx.app.log("NoSound", "No sound found for " + soundOption.toString());
            return;
        }

        Sound s = soundCont.getSound();
        s.stop(soundId);
    }

    public long playSound(Sounds soundOption, float volume, float pitch, float pan) {
        volume = volume * soundVolume.floatValue();
        if (isSoundMuted || soundOption == Sounds.none) return -1;

        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont == null) {
            // Gdx.app.log("NoSound", "No sound found for " + soundOption.toString());
            return 0;
        }

        Sound s = soundCont.getSound();
        return (s != null) ? s.play(volume, pitch, pan) : 0;
    }

    public long loopSound(Sounds soundOption, float volume) {
        volume = volume * soundVolume.floatValue();
        if (isSoundMuted || soundOption == Sounds.none) return -1;

        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont == null) {
            // Gdx.app.log("NoSound", "No sound found for " + soundOption.toString());
            return 0;
        }

        Sound s = soundCont.getSound();
        return (s != null) ? s.loop(volume) : 0;
    }

    public long playDirectionalSoundFromVector(Sounds soundOption, Vector2 vector, float viewportWidth) {
        if (isSoundMuted || soundOption == Sounds.none) return -1;

        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont == null) {
            // Gdx.app.log("NoSound", "No sound found for " + soundOption.toString());
            return 0;
        }

        Sound s = soundCont.getSound();
        float midWidth = viewportWidth / 2f;
        float pan = -1 * (midWidth - vector.x) / midWidth;
//        Gdx.app.log("pan: ", String.valueOf(pan));

        return (s != null) ? s.play(soundVolume.floatValue(), 1f, pan) : 0;
    }



    public void stopSound(Sounds soundOption) {
        SoundContainer soundCont = sounds.get(soundOption);
        if (soundCont != null) {
            soundCont.stopSound();
        }
    }

    public void stopAllSounds() {
        for (SoundContainer soundCont : sounds.values()) {
            if (soundCont != null) {
                soundCont.stopSound();
            }
        }
    }

    public Music playMusic(Musics musicOptions) {
        return playMusic(musicOptions, true);
    }

    public Music playMusic(Musics musicOptions, boolean playImmediately) {
        return playMusic(musicOptions, playImmediately, true);
    }

    public Music playMusic(Musics musicOptions, boolean playImmediately, boolean looping) {
        if (playImmediately) {
            if (currentMusic != null && currentMusic.isPlaying()) {
                currentMusic.stop();
            }
            // fade in out streams
            currentMusic = startMusic(musicOptions, looping);
        } else {
            if (currentMusic == null || !currentMusic.isPlaying()) {
                currentMusic = startMusic(musicOptions, looping);
            } else {
                currentMusic.setLooping(false);
                currentMusic.setOnCompletionListener(music -> {
                    currentMusic = startMusic(musicOptions, looping);
                });
            }
        }
        return currentMusic;
    }

    private Music startMusic(Musics musicOptions, boolean looping) {
        Music music = musics.get(musicOptions);
        if (music != null) {
            music.setVolume(musicVolume.floatValue());
            music.setLooping(looping);
            music.play();
        }
        return music;
    }

    public void fadeMusic(Musics musicOption) {
        if (eCurrentMusic == musicOption) return;

    }

    public void stopMusic() {
        for (Music music : musics.values()) {
            if (music != null) music.stop();
        }
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    public void setMusicVolume(float level) {
        if (isMusicMuted)
            musicVolume.setValue(0f);
        else
            musicVolume.setValue(level);
    }
    public void setSoundVolume(float level) {
        if (isSoundMuted)
            soundVolume.setValue(0f);
        else
            soundVolume.setValue(level);
    }

}

class SoundContainer {
    public Array<Sound> sounds;
    public Sound currentSound;

    public SoundContainer() {
        sounds = new Array<Sound>();
    }

    public void addSound(Sound s) {
        if (!sounds.contains(s, false)) {
            sounds.add(s);
        }
    }

    public Sound getSound() {
        if (sounds.size > 0) {
            int randIndex = MathUtils.random(0, sounds.size - 1);
            Sound s = sounds.get(randIndex);
            currentSound = s;
            return s;
        } else {
            // Gdx.app.log("No sounds found!");
            return null;
        }
    }

    public void stopSound() {
        if (currentSound != null) {
            currentSound.stop();
        }
    }

    public void dispose() {
        if (currentSound != null) {
            currentSound.dispose();
        }
    }
}
