package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.particles.Particles;
import lando.systems.ld55.ui.TitleScreenUI;
import lando.systems.ld55.utils.events.EventType;
import lando.systems.ld55.utils.events.Events;

public class TitleScreen extends BaseScreen {

    float accum = 0;
    boolean drawUI = true;
    TitleScreenUI titleScreenUI;
    Particles particles;
    Events.EventListener transitionToGameScreen = (type, data) -> transitionToGameScreen();
    Events.EventListener transitionToCreditsScreen = (type, data) -> transitionToCreditsScreen();
    Events.EventListener meaninglessClickEffect = (type, data) -> meaninglessClickEffect((float)data[0], (float)data[1]);

    public TitleScreen() {
        Gdx.input.setInputProcessor(uiStage);
        Main.game.audioManager.playMusic(AudioManager.Musics.introMusic);
        particles = new Particles(assets);
        var buttonWidth = 250f;
        var buttonHeight = 60f;
        titleScreenUI = new TitleScreenUI(
            (worldCamera.viewportWidth - (3 * buttonWidth)) / 2f, 20,
            buttonWidth, buttonHeight, assets.font, TitleScreenUI.ButtonOrientation.HORIZONTAL);
        subscribeEvents();
    }

    @Override
    public void initializeUI() {
        super.initializeUI();
        //TitleScreenUI titleScreenUI = new TitleScreenUI(this);
        //uiStage.addActor(titleScreenUI);
    }

    @Override
    public void alwaysUpdate(float delta) {
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        accum+=dt;

        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCamera.unproject(touchPos);
        titleScreenUI.update(touchPos.x, touchPos.y);
        particles.update(dt);
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
            float width = worldCamera.viewportWidth;
            float height = worldCamera.viewportHeight;
            batch.draw(assets.title, 0, 0, width, height);
//            batch.draw(gdx, (width - gdx.getWidth()) / 2f, (height - gdx.getHeight()) / 2f);

            particles.draw(batch, Particles.Layer.BACKGROUND);

//            var font = assets.fontAbandoned;
//            var layout = new GlyphLayout();
//            layout.setText(font, "Coming Soon - \nClock Gobblers", Color.WHITE, width, Align.center, true);
            //font.draw(batch, layout, 0, height / 3f);

            particles.draw(batch, Particles.Layer.FOREGROUND);
            if (drawUI) {
                titleScreenUI.draw(batch);
            }
        }
        batch.end();
    }

    private void transitionToGameScreen() {
        if (!exitingScreen ) {
            exitingScreen = true;
            var nextScreen = Config.Debug.show_intro_screen
                ? new IntroScreen()
                : new GameScreen();
            game.setScreen(nextScreen);
            Main.game.audioManager.playSound(AudioManager.Sounds.idle_click);
            unsubscribeEvents();
        }
    }

    private void transitionToCreditsScreen() {
        Gdx.app.log("TitleScreen", "transitionToCreditsScreen");
        if (!exitingScreen ) {
            Main.game.audioManager.playSound(AudioManager.Sounds.level_up);
            exitingScreen = true;
            game.setScreen(new CreditScreen());
            unsubscribeEvents();
        }
    }

    private void meaninglessClickEffect(float x, float y) {
        particles.tinySmoke(x, y);
        Main.game.audioManager.playSound(AudioManager.Sounds.idle_click);
    }

    private void subscribeEvents() {
        Events.get().subscribe(EventType.TRANSITION_TO_GAME, transitionToGameScreen);
        Events.get().subscribe(EventType.TRANSITION_TO_CREDITS, transitionToCreditsScreen);
        Events.get().subscribe(EventType.MEANINGLESS_CLICK, meaninglessClickEffect);
    }

    private void unsubscribeEvents() {
        Events.get().unsubscribe(EventType.TRANSITION_TO_GAME, transitionToGameScreen);
        Events.get().unsubscribe(EventType.TRANSITION_TO_CREDITS, transitionToCreditsScreen);
        Events.get().unsubscribe(EventType.MEANINGLESS_CLICK, meaninglessClickEffect);
    }
}
