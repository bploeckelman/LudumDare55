package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld54.screens.IntroScreen;
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

    public TitleScreen() {
        Gdx.input.setInputProcessor(uiStage);
        Main.game.audioManager.playMusic(AudioManager.Musics.introMusic);
        particles = new Particles(assets);
        titleScreenUI = new TitleScreenUI(worldCamera.viewportWidth - 500f, 200, 300f, 75f, assets.fontAbandoned, TitleScreenUI.ButtonOrientation.VERTICAL);
        Events.get().subscribe(EventType.TRANSITION_TO_GAME, (type, data) -> transitionToGameScreen());
        Events.get().subscribe(EventType.TRANSITION_TO_CREDITS, (type, data) -> transitionToCreditsScreen());
        Events.get().subscribe(EventType.MEANINGLESS_CLICK, (type, data) -> meaninglessClickEffect((float)data[0], (float)data[1]));
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
            Texture gdx = assets.gdx;
            float width = worldCamera.viewportWidth;
            float height = worldCamera.viewportHeight;
            batch.draw(gdx, (width - gdx.getWidth()) / 2f, (height - gdx.getHeight()) / 2f);

            particles.draw(batch, Particles.Layer.BACKGROUND);

            var font = assets.fontAbandoned;
            var layout = new GlyphLayout();
            layout.setText(font, "Coming Soon - \nClock Gobblers", Color.WHITE, width, Align.center, true);
            //font.draw(batch, layout, 0, height / 3f);

            particles.draw(batch, Particles.Layer.FOREGROUND);
            if (drawUI) {
                titleScreenUI.draw(batch);
            }
        }
        batch.end();


    }

    private void transitionToGameScreen() {
        if (Gdx.input.justTouched() && !exitingScreen ) {
            exitingScreen = true;
            game.setScreen(new IntroScreen());
        }
    }

    private void transitionToCreditsScreen() {
        Main.game.audioManager.playSound(AudioManager.Sounds.level_up);
//        if (Gdx.input.justTouched() && !exitingScreen ) {
//            exitingScreen = true;
//            game.setScreen(new GameScreen());
//        }
    }

    private void meaninglessClickEffect(float x, float y) {
        particles.tinySmoke(x, y);
        Main.game.audioManager.playSound(AudioManager.Sounds.idle_click);
    }
}
