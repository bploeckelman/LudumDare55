package lando.systems.ld55.screens;

import aurelienribon.tweenengine.*;
import aurelienribon.tweenengine.equations.Bounce;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.primitives.MutableFloat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.particles.Particles;
import lando.systems.ld55.ui.SettingsUI;
import lando.systems.ld55.ui.TitleScreenUI;
import lando.systems.ld55.utils.Time;
import lando.systems.ld55.utils.accessors.Vector2Accessor;
import lando.systems.ld55.utils.events.EventType;
import lando.systems.ld55.utils.events.Events;

public class TitleScreen extends BaseScreen {

    float accum = 0;
    float fanfareTimer = -100;
    boolean drawUI = false;
    TitleScreenUI titleScreenUI;
    SettingsUI settingsUI;
    Particles particles;
    Events.EventListener transitionToGameScreen = (type, data) -> transitionToGameScreen();
    Events.EventListener transitionToCreditsScreen = (type, data) -> transitionToCreditsScreen();
    Events.EventListener meaninglessClickEffect = (type, data) -> meaninglessClickEffect((float)data[0], (float)data[1]);
    Events.EventListener showSettings = (type, data) -> showSettings();

    private Vector2 boardPosition = new Vector2(80, 700);
    private Vector2 kingPosition = new Vector2(0, -400);
    private Vector2 deathPosition = new Vector2(Config.Screen.window_width - 171f, -400);
    private MutableFloat archerAlpha = new MutableFloat(0);
    private Vector2 archerPosition = new Vector2(325, 224);
    private MutableFloat pawnEvilAlpha = new MutableFloat(0);
    private Vector2 pawnEvilPosition = new Vector2(720, 134);
    private MutableFloat pawnGoodAlpha = new MutableFloat(0);
    private Vector2 pawnGoodPosition = new Vector2(480, 304);
    private MutableFloat knightAlpha = new MutableFloat(0);
    private Vector2 knightPosition = new Vector2(650, 294);
    private MutableFloat bishopGoodAlpha = new MutableFloat(0);
    private Vector2 bishopGoodPosition = new Vector2(450, 104);
    private MutableFloat bishopEvilAlpha = new MutableFloat(0);
    private Vector2 bishopEvilPosition = new Vector2(835, 234);

    private Vector2 titlePosition = new Vector2(70, 800);
    private Vector2 subTitlePosition = new Vector2(70, 270);
    private MutableFloat subTitleAlpha = new MutableFloat(0);

    public TitleScreen() {
        Gdx.input.setInputProcessor(uiStage);
        Main.game.audioManager.playMusic(AudioManager.Musics.introMusic);
        particles = new Particles(assets);
        var buttonWidth = 250f;
        var buttonHeight = 60f;
        titleScreenUI = new TitleScreenUI(
            (worldCamera.viewportWidth - (3 * buttonWidth)) / 2f, 20,
            buttonWidth, buttonHeight, assets.font, TitleScreenUI.ButtonOrientation.HORIZONTAL);
        settingsUI = new SettingsUI(skin, worldCamera);
        uiStage.addActor(settingsUI);
        subscribeEvents();

        Timeline.createSequence()
            .delay(.1f)
            .push(Tween.to(boardPosition, Vector2Accessor.Y, 1.5f)
                .target(-140).ease(Bounce.OUT))
            .beginParallel()
                .push(Tween.to(kingPosition, Vector2Accessor.Y, .5f)
                    .target(0))
                .push(Tween.to(deathPosition, Vector2Accessor.Y, .5f)
                    .target(0))
            .end()
            .beginParallel()
                .push(Tween.to(archerAlpha, 1, .5f)
                    .target(1).delay(MathUtils.random(.25f, 1f)))
                .push(Tween.to(knightAlpha, 1, .5f)
                    .target(1).delay(MathUtils.random(.25f, 1f)))
                .push(Tween.to(bishopEvilAlpha, 1, .5f)
                    .target(1).delay(MathUtils.random(.25f, 1f)))
                .push(Tween.to(bishopGoodAlpha, 1, .5f)
                    .target(1).delay(MathUtils.random(.25f, 1f)))
                .push(Tween.to(pawnEvilAlpha, 1, .5f)
                    .target(1).delay(MathUtils.random(.25f, 1f)))
                .push(Tween.to(pawnGoodAlpha, 1, .5f)
                    .target(1).delay(MathUtils.random(.25f, 1f)))
            .end()
            .pushPause(.5f)
            .push(Tween.to(titlePosition, Vector2Accessor.Y, .9f)
                .target(550).ease(Elastic.OUT))
            .pushPause(.3f)
            .push(Tween.to(subTitleAlpha, 1, .5f)
                .target(1))
            .push(Tween.call((type, source) -> {
                drawUI = true;
                fanfareTimer = 2f;
            }))
            .start(Main.game.tween);

    }

    @Override
    public void initializeUI() {
        super.initializeUI();
        //uiStage.addActor(titleScreenUI);
    }

    @Override
    public void alwaysUpdate(float delta) {
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        accum+=dt;
        fanfareTimer += dt;

        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCamera.unproject(touchPos);
        titleScreenUI.update(touchPos.x, touchPos.y);
        settingsUI.act(dt);
        particles.update(dt);


        if (fanfareTimer > 2.4f) {
            particles.fanfareConfetti(70f, 470f);
            particles.fanfareConfetti(100f, 500f);
            particles.fanfareConfetti(130f, 530f);
            particles.bloodFountain(960f, 520f);
            particles.bloodFountain(1100f, 470f);
            particles.bloodFountain(1050f, 400f);
            particles.spawnArrow(450f, 285f, 890f, 280f);
            Time.do_after_delay(0.5f, (param) -> particles.bloodBurst(890f, 280f));
            particles.spawnFireball(580f, 235f, 720f, 350f);
            Time.do_after_delay(0.6f, (param) -> particles.bloodBurst(720f, 350f));
            particles.spawnMagic(920f, 350f, 540f, 320f);
            Time.do_after_delay(0.4f, (param) -> particles.bloodBurst(540f, 320f));
            fanfareTimer = 0f;
        }
    }

    @Override
    public void renderFrameBuffers(SpriteBatch batch) {

    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            batch.setColor(Color.WHITE);
            float width = worldCamera.viewportWidth;
            float height = worldCamera.viewportHeight;
            batch.draw(assets.titleBackground, 0, 0, width, height);

            batch.draw(assets.titleBoard, boardPosition.x, boardPosition.y);
            batch.draw(assets.titleKing, kingPosition.x, kingPosition.y);
            batch.draw(assets.titleDeath, deathPosition.x, deathPosition.y);

            batch.setColor(1, 1, 1, archerAlpha.floatValue());
            batch.draw(assets.titleArcherGood, archerPosition.x, archerPosition.y);

            batch.setColor(1, 1, 1, bishopEvilAlpha.floatValue());
            batch.draw(assets.titleBishopEvil, bishopEvilPosition.x, bishopEvilPosition.y);

            batch.setColor(1, 1, 1, bishopGoodAlpha.floatValue());
            batch.draw(assets.titleBishopGood, bishopGoodPosition.x, bishopGoodPosition.y);

            batch.setColor(1, 1, 1, knightAlpha.floatValue());
            batch.draw(assets.titleKnightEvil, knightPosition.x, knightPosition.y);

            batch.setColor(1, 1, 1, pawnEvilAlpha.floatValue());
            batch.draw(assets.titlePawnEvil, pawnEvilPosition.x, pawnEvilPosition.y);

            batch.setColor(1, 1, 1, pawnGoodAlpha.floatValue());
            batch.draw(assets.titlePawnGood, pawnGoodPosition.x, pawnGoodPosition.y);

            batch.setColor(Color.WHITE);
            batch.draw(assets.titleTextTitle, titlePosition.x, titlePosition.y);

            batch.setColor(1f, 1f, 1f, subTitleAlpha.floatValue());
            batch.draw(assets.titleTextSubTitle, subTitlePosition.x, subTitlePosition.y);

            batch.setColor(Color.WHITE);
            batch.draw(assets.titleVignette, 0, 0, width, height);

            particles.draw(batch, Particles.Layer.BACKGROUND);

            particles.draw(batch, Particles.Layer.FOREGROUND);
            if (drawUI) {
                titleScreenUI.draw(batch);
            }
        }
        batch.end();

        if (drawUI) {
            uiStage.act();
            uiStage.draw();
        }
    }

    private void transitionToGameScreen() {
        if (settingsUI.isSettingShown) { return; }
        if (!drawUI) return;
        if (!exitingScreen ) {
            exitingScreen = true;
            var nextScreen = Config.Debug.show_intro_screen
                ? new IntroScreen()
                : new GameScreen();
            game.setScreen(nextScreen, assets.doorwayShader, 2f);
            Main.game.audioManager.playSound(AudioManager.Sounds.click);
            unsubscribeEvents();
        }
    }

    private void transitionToCreditsScreen() {
        if (settingsUI.isSettingShown) { return; }
        if (!drawUI) return;
        if (!exitingScreen ) {
            Main.game.audioManager.playSound(AudioManager.Sounds.click);
            exitingScreen = true;
            game.setScreen(new CreditScreen());
            unsubscribeEvents();
        }
    }

    private void showSettings() {
        settingsUI.showSettings();
    }

    private void meaninglessClickEffect(float x, float y) {
        if (settingsUI.isSettingShown) { return; }
        particles.levelUpEffect(x, y);
        Main.game.audioManager.playSound(AudioManager.Sounds.hop);
    }

    private void subscribeEvents() {
        Events.get().subscribe(EventType.TRANSITION_TO_GAME, transitionToGameScreen);
        Events.get().subscribe(EventType.TRANSITION_TO_CREDITS, transitionToCreditsScreen);
        Events.get().subscribe(EventType.MEANINGLESS_CLICK, meaninglessClickEffect);
        Events.get().subscribe(EventType.SHOW_SETTINGS, showSettings);
    }

    private void unsubscribeEvents() {
        Events.get().unsubscribe(EventType.TRANSITION_TO_GAME, transitionToGameScreen);
        Events.get().unsubscribe(EventType.TRANSITION_TO_CREDITS, transitionToCreditsScreen);
        Events.get().unsubscribe(EventType.MEANINGLESS_CLICK, meaninglessClickEffect);
        Events.get().unsubscribe(EventType.SHOW_SETTINGS, showSettings);
    }
}
