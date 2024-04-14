package lando.systems.ld54.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.particles.Particles;
import lando.systems.ld55.screens.BaseScreen;
import lando.systems.ld55.screens.GameScreen;
import lando.systems.ld55.utils.typinglabel.TypingLabel;

public class IntroScreen extends BaseScreen {

    Texture backgroundTexture;
    Texture parchmentTexture;
    BitmapFont font;
    Particles particles;
    String page1 =
        "{COLOR=black}The old king has fallen, his crown a relic of an era.\n" +
        "The board lies silent, a battlefield turned desolate.\n" +
        "{GRADIENT=gray;black}{JUMP}Pawns{ENDJUMP}{ENDGRADIENT} stand frozen, {GRADIENT=yellow;black}knights{ENDGRADIENT} remain unmounted, their destinies uncertain in this hushed kingdom.\n" +
        "{SICK}Yet, a new power stirs. Not a warrior to claim the throne, but a strategist, a seer...{ENDSICK}\n";
    String page2 =
        "{COLOR=black}Your eyes behold this forgotten realm.  Your touch awakens its slumbering pieces.\n" +
        "Though your hand wields no sword, your mind commands armies unseen.\n" +
        "Each move you orchestrate ripples across this checkered landscape, shaping history.\n" +
        "{GRADIENT=white;black}Bishops{ENDGRADIENT} whisper prayers, their paths influenced by your subtle guidance.\n";
    String page3 =
        "{COLOR=black}Knights charge with renewed purpose, their hooves a thunderclap directed by your will.\n" +
        "The pawns, once overlooked, become the linchpin of your grand design.\n" +
        "But beware, strategist, for every calculated move, an equal counterstroke awaits.\n" +
        "Victory and defeat hang in delicate balance, the echoes of your choices ringing through time.\n";
    String page4 =
        "{COLOR=black}This is no mere chessboard, but a tapestry of fate woven by your strategic brilliance.\n" +
        "So, grand observer, will your reign usher in an era of prosperity...\n" +
        "...or will the kingdom crumble beneath the weight of your decisions?\n" +
        "{RAINBOW}The time has come. The pieces await your command...{ENDRAINBOW}\n";
    int currentPage = 0;
    float elapsedTime = 0f;
    TypingLabel typingLabel;
    float transitionAlpha = 0f;

    public IntroScreen() {
        backgroundTexture = assets.introBackground;
        parchmentTexture = assets.parchment;
        font = Main.game.assets.fontTreasureMap;


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
                } else if (currentPage == 3) {
                    typingLabel.restart(page4);
                } else {
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
