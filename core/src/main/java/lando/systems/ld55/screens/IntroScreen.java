package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.particles.Particles;
import lando.systems.ld55.utils.typinglabel.TypingLabel;

public class IntroScreen extends BaseScreen {

    Texture backgroundTexture;
    Texture parchmentTexture;
    BitmapFont font;
    Particles particles;
    String page1 =
        "{COLOR=black}" +
            "\nA{GRADIENT=black;gray} King{ENDGRADIENT} is the embodiment of{GRADIENT=black;gold} power{ENDGRADIENT}.\n\n" +
        "But every monarch knows they are a captive,\n" +
            "with their every need relying on the {GRADIENT=navy;purple} fealty {ENDGRADIENT}of those who serve them.\n\n ";
    String page2 =
        "{COLOR=black}An {GRADIENT=red;black}evil wizard{ENDGRADIENT} has unleashed an army of {GRADIENT=red;brown}monsters {ENDGRADIENT}on the castle.\n\n" +
            "The warriors of the King's " +
            "court must now advance and conquer their menace in a {GRADIENT=brown;red}rousing, turn-based combat simulator{ENDGRADIENT} " +
            "vaguely similar to{GRADIENT=green;black} chess{ENDGRADIENT}.\n\n" +
            "(The similarities turned out to be pretty superficial in the end, but that's where the idea started)";
    String page3 =
            "\n{COLOR=black}In times of peril, the King is only as safe as the court of their {GRADIENT=yellow;black}summoning{ENDGRADIENT}..." +
                "\n\nGuide your warriors to victory or end up \n {GRADIENT=red;brown}Board To Death! {ENDGRADIENT} " ;
//    String page4 =
//        "{COLOR=black}This is no mere chessboard, but a tapestry of fate woven by your strategic brilliance.\n" +
//        "So, grand observer, will your reign usher in an era of prosperity...\n" +
//        "...or will the kingdom crumble beneath the weight of your decisions?\n" +
//        "{RAINBOW}The time has come. The pieces await your command...{ENDRAINBOW}\n";
    int currentPage = 0;
    float elapsedTime = 0f;
    TypingLabel typingLabel;
    float transitionAlpha = 0f;

    public IntroScreen() {
        backgroundTexture = assets.introBackground;
        parchmentTexture = assets.parchment;
        font = Main.game.assets.fontTreasureMap;

        Main.game.audioManager.playMusic(AudioManager.Musics.introMusic);

        particles = new Particles(Main.game.assets);

        typingLabel = new TypingLabel(font, page1, worldCamera.viewportWidth * .2f, worldCamera.viewportHeight * .8f);
        typingLabel.setWidth(Config.Screen.window_width * .7f);
        typingLabel.setFontScale(1.2f);
    }

    @Override
    public void alwaysUpdate(float delta) {

    }

    @Override
    public void update(float dt) {
        elapsedTime += dt;
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            worldCamera.unproject(touchPos);
            particles.levelUpEffect(touchPos.x, touchPos.y);

            if (transitionAlpha < 1f) {
                transitionAlpha = 1f;
            } else if (!typingLabel.hasEnded()) {
                typingLabel.skipToTheEnd();
            } else {
                currentPage++;
                if (currentPage == 1) {
                    typingLabel.restart(page2);
                } else if (currentPage == 2) {
                    typingLabel.restart(page3);
                }
//                else if (currentPage == 3) {
//                    typingLabel.restart(page4);
//                }
                else {
                    launchGame();
                }
            }
        }
        particles.update(dt);
        if (transitionAlpha < 1f) {
            transitionAlpha = MathUtils.clamp(transitionAlpha + dt, 0f, 1f);
        } else {
            typingLabel.update(dt);
        }
    }

    @Override
    public void renderFrameBuffers(SpriteBatch batch) {

    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(.0f, .0f, .1f, 1f);

        batch.enableBlending();
        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, transitionAlpha);
        batch.draw(backgroundTexture, 0, 0, windowCamera.viewportWidth, windowCamera.viewportHeight);

        // Center parchment calculation (adjust offsets if needed)
        batch.draw(parchmentTexture, windowCamera.viewportWidth * .1f, windowCamera.viewportHeight * .1f, windowCamera.viewportWidth * .9f, windowCamera.viewportHeight * .9f * transitionAlpha);

        typingLabel.render(batch);

        particles.draw(batch, Particles.Layer.FOREGROUND);
        batch.end();
    }

    void launchGame() {
        if (!exitingScreen){
            exitingScreen = true;
            game.setScreen(new GameScreen(), assets.doomShader, 1f);
        }
    }
}
