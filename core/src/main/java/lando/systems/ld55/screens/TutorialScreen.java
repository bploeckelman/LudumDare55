package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.kotcrab.vis.ui.widget.VisWindow;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.assets.Assets;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.entities.StyleManager;
import lando.systems.ld55.particles.Particles;
import lando.systems.ld55.ui.TutorialScreenUI;
import lando.systems.ld55.ui.radial.RadialTooltip;
import lando.systems.ld55.utils.typinglabel.TypingLabel;

import javax.swing.text.Style;

public class TutorialScreen extends BaseScreen {
    StyleManager styleManager;
    Particles particles;
    TutorialScreenUI ui;
    boolean tutorialPage1Finished = false;
    boolean tutorialPage2Finished = false;
    boolean tutorialPage3Finished = false;
    boolean tutorialPage4Finished = false;
    boolean tutorialPage5Finished = false;
    boolean tutorialPage6Finished = false;
    boolean tutorialPage7Finished = false;
    boolean tutorialPage8Finished = false;
    boolean tutorialFinished = false;
    boolean showEnemies = false;
    float accum = 0f;

    public TutorialScreen() {
        particles = new Particles(assets);
        styleManager = new StyleManager();
        setupStyle();
        ui = new TutorialScreenUI(this);
    }

    @Override
    public void alwaysUpdate(float delta) {

    }

    @Override
    public void update(float dt) {
        accum += dt;
        particles.update(dt);
        styleManager.update(dt);
        ui.update(dt);
        if (Gdx.input.justTouched()) {
            Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            worldCamera.unproject(touchPos);
            particles.levelUpEffect(touchPos.x, touchPos.y);
            if (!tutorialPage1Finished) {
                Gdx.app.log("Tutorial", "Page 1 Finished");
                tutorialPage1Finished = true;
                styleManager.add(assets.thrones.get(0),     140, 350, true);
                styleManager.add(assets.king.get(0).get(0), 180, 350, true);
                particles.smoke(180, 350);
                styleManager.add(assets.thrones.get(1),     1140, 350, false);
                styleManager.add(assets.king.get(1).get(0), 1100, 350, false);
                particles.smoke(1100, 350);
            } else if (!tutorialPage2Finished) {
                Gdx.app.log("Tutorial", "Page 2 Finished");
                tutorialPage2Finished = true;
            } else if (!tutorialPage3Finished) {
                Gdx.app.log("Tutorial", "Page 3 Finished");
                tutorialPage3Finished = true;
                styleManager.add(assets.bishop.get(1).get(0), 1000, 150, false);
                particles.smoke(1000, 150);
                styleManager.add(assets.knight.get(1).get(0), 1000, 250, false);
                particles.smoke(1000, 250);
                styleManager.add(assets.rook.get(1).get(0), 1000, 350, false);
                particles.smoke(1000, 350);
                styleManager.add(assets.pawn.get(1).get(0), 1000, 450, false);
                particles.smoke(1000, 450);
                styleManager.add(assets.queen.get(1).get(0), 1000, 550, false);
                particles.smoke(1000, 550);
                showEnemies = true;
            } else if (!tutorialPage4Finished) {
                Gdx.app.log("Tutorial", "Page 4 Finished");
                tutorialPage4Finished = true;
                Main.game.audioManager.playSound(AudioManager.Sounds.horn_fanfare);
                styleManager.add(assets.bishop.get(0).get(0), 300, 150, true);
                particles.levelUpEffect(300, 150);
                styleManager.add(assets.knight.get(0).get(0), 300, 250, true);
                particles.levelUpEffect(300, 250);
                styleManager.add(assets.rook.get(0).get(0), 300, 350, true);
                particles.levelUpEffect(300, 350);
                styleManager.add(assets.pawn.get(0).get(0), 300, 450, true);
                particles.levelUpEffect(300, 450);
                styleManager.add(assets.queen.get(0).get(0), 300, 550, true);
                particles.levelUpEffect(300, 550);
            } else if (!tutorialPage5Finished) {
                Gdx.app.log("Tutorial", "Page 5 Finished");
                tutorialPage5Finished = true;
            } else if (!tutorialPage6Finished) {
                Gdx.app.log("Tutorial", "Page 6 Finished");
                tutorialPage6Finished = true;
            } else if (!tutorialPage7Finished) {
                Gdx.app.log("Tutorial", "Page 7 Finished");
                tutorialPage7Finished = true;
            } else if (!tutorialPage8Finished) {
                Gdx.app.log("Tutorial", "Page 8 Finished");
                tutorialPage8Finished = true;
                ui.endTurnButton.pulse = true;
            }
            else {
                launchGame();
            }
        }
    }

    @Override
    public void renderFrameBuffers(SpriteBatch batch) {

    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(.0f, .0f, .1f, 1f);
        batch.begin();
        batch.draw(assets.levelLayout, 0, 0, Config.Screen.window_width, Config.Screen.window_height);
        styleManager.render(batch);
        ui.render(batch);
        assets.layout.setText(assets.fontAbandoned, "Tutorial", Color.WHITE, Config.Screen.window_width, Align.center, false);
        assets.fontAbandoned.draw(batch, assets.layout, 0f, Config.Screen.window_height - 30);
        if (!tutorialPage1Finished) {
            renderPage1();
        } else if (!tutorialPage2Finished) {
            renderPage2();
        } else if (!tutorialPage3Finished) {
            renderPage3();
        } else if (!tutorialPage4Finished) {
            renderPage4();
        } else if (!tutorialPage5Finished) {
            renderPage5();
        } else if (!tutorialPage6Finished) {
            renderPage6();
        } else if (!tutorialPage7Finished) {
            renderPage7();
        } else if (!tutorialPage8Finished) {
            renderPage8();
            tutorialFinished = true;
        } else {
            renderPage9();
        }
        particles.draw(batch, Particles.Layer.FOREGROUND);
        batch.end();
    }

    void launchGame() {
        if (!exitingScreen){
            exitingScreen = true;
            game.setScreen(new GameScreen(), assets.doomShader, 1f);
        }
    }

    private void renderPage1() {
        assets.layout.setText(assets.font, "Welcome to the Board to Death.", Color.WHITE,
            Config.Screen.window_width, Align.center, false);
        var panelWidth = assets.layout.width + 100f;
        var panelHeight = assets.layout.height + 50f;
        Assets.NinePatches.glass.draw(batch, Config.Screen.window_width / 2f - panelWidth / 2f, Config.Screen.window_height / 2f - panelHeight / 2f, panelWidth, panelHeight);
        assets.font.draw(batch, assets.layout, 0, Config.Screen.window_height / 2f + 12f);
    }

    private void renderPage2() {
        assets.layout.setText(assets.font, "This is you.\nThe good King", Color.YELLOW,
            Config.Screen.window_width / 2f, Align.center, false);
        var panelWidth = assets.layout.width + 100f;
        var panelHeight = assets.layout.height + 50f;
        Assets.NinePatches.glass.draw(batch, Config.Screen.window_width / 4f - panelWidth / 2f, Config.Screen.window_height - 260f, panelWidth, panelHeight);
        assets.font.draw(batch, assets.layout, 0, Config.Screen.window_height - 150f);

        assets.layout.setText(assets.font, "This is them.\nThe bad Wizard.", Color.RED,
            Config.Screen.window_width / 2f, Align.center, false);
        panelWidth = assets.layout.width + 100f;
        panelHeight = assets.layout.height + 50f;
        Assets.NinePatches.glass.draw(batch, Config.Screen.window_width * (3f / 4f) - panelWidth / 2f, Config.Screen.window_height - 260f, panelWidth, panelHeight);
        assets.font.draw(batch, assets.layout, Config.Screen.window_width / 2f, Config.Screen.window_height - 150f);
    }

    private void renderPage3() {
        assets.layout.setText(assets.font, "Your goal is to reach the enemy side,\nbefore they reach you.\nBut you can't do it yourself.\nAfter all, you are the king.", Color.WHITE,
            Config.Screen.window_width, Align.center, false);
        var panelWidth = assets.layout.width + 100f;
        var panelHeight = assets.layout.height + 50f;
        Assets.NinePatches.glass.draw(batch, Config.Screen.window_width / 2f - panelWidth / 2f, Config.Screen.window_height / 2f - panelHeight + 20f, panelWidth, panelHeight + 120f);
        Assets.NinePatches.glass_dim.draw(batch, Config.Screen.window_width / 2f - 50f, Config.Screen.window_height / 2f + 25f, 100f, 100f);
        batch.setColor(Color.YELLOW);
        batch.draw(TileOverlayAssets.laurel, Config.Screen.window_width / 2f - 50f, Config.Screen.window_height / 2f + 25f, 100f, 100f);
        batch.setColor(Color.WHITE);
        batch.draw(TileOverlayAssets.crown, Config.Screen.window_width / 2f - 50f, Config.Screen.window_height / 2f + 25f, 100f, 100f);
        assets.font.draw(batch, assets.layout, 0, Config.Screen.window_height / 2f);
    }

    private void renderPage4() {
        assets.layout.setText(assets.font, "Oh no, the wizard summoned the monster!", Color.WHITE,
            Config.Screen.window_width, Align.center, false);
        var panelWidth = assets.layout.width + 100f;
        var panelHeight = assets.layout.height + 50f;
        Assets.NinePatches.glass.draw(batch, Config.Screen.window_width / 2f - panelWidth / 2f, Config.Screen.window_height / 2f - panelHeight + 20f, panelWidth, panelHeight);
        assets.font.draw(batch, assets.layout, 0, Config.Screen.window_height / 2f);
    }

    private void renderPage5() {
        assets.layout.setText(assets.font, "Summon the Monarchs!", Color.WHITE,
            Config.Screen.window_width, Align.center, false);
        var panelWidth = assets.layout.width + 100f;
        var panelHeight = assets.layout.height + 50f;
        Assets.NinePatches.glass.draw(batch, Config.Screen.window_width / 2f - panelWidth / 2f, Config.Screen.window_height / 2f - panelHeight + 20f, panelWidth, panelHeight);
        assets.font.draw(batch, assets.layout, 0, Config.Screen.window_height / 2f);
    }

    private void renderPage6() {
        assets.layout.setText(assets.font, "Every action consumes action point.\nMovement will consume 1.\nSummoning cost differs per unit.", Color.WHITE,
            Config.Screen.window_width, Align.center, false);
        var panelWidth = assets.layout.width + 100f;
        var panelHeight = assets.layout.height + 50f;
        Assets.NinePatches.glass.draw(batch, Config.Screen.window_width / 2f - panelWidth / 2f, Config.Screen.window_height / 2f - panelHeight + 20f, panelWidth, panelHeight);
        assets.font.draw(batch, assets.layout, 0, Config.Screen.window_height / 2f);
    }

    private void renderPage7() {
        assets.layout.setText(assets.font, "Action Points");
        Assets.NinePatches.glass.draw(batch, 100f, 160f, 1150f, 130f);
        assets.font.draw(batch, assets.layout, 120f, 260f);

        assets.layout.setText(assets.font, "You get more action points the more you play!", Color.WHITE, 1200f, Align.left, true);
        assets.font.draw(batch, assets.layout, 120f, 220f);
    }

    private void renderPage8() {
        assets.layout.setText(assets.font, "Click on the tile with this icon to Summon!", Color.WHITE,
            Config.Screen.window_width, Align.center, false);
        var panelWidth = assets.layout.width + 100f;
        var panelHeight = assets.layout.height + 50f;
        Assets.NinePatches.glass.draw(batch, Config.Screen.window_width / 2f - panelWidth / 2f, Config.Screen.window_height / 2f - panelHeight + 20f, panelWidth, panelHeight + 120f);
        Assets.NinePatches.glass_blue.draw(batch, Config.Screen.window_width / 2f - 50f, Config.Screen.window_height / 2f + 25f, 100f, 100f);
        batch.draw(TileOverlayAssets.pawnPlus, Config.Screen.window_width / 2f - 50f, Config.Screen.window_height / 2f + 25f, 100f, 100f);
        assets.font.draw(batch, assets.layout, 0, Config.Screen.window_height / 2f);
    }

    private void renderPage9() {
        Assets.NinePatches.glass.draw(batch, 190f, 40f, 1010f, 130f);
        assets.layout.setText(assets.font, "End turn to summon and move. The button will flash if there is no more actions!", Color.WHITE, 1000f, Align.left, true);
        assets.font.draw(batch, assets.layout, 210f, 150f);
    }
    private void setupStyle() {
        float y = 75;
        styleManager.add(assets.candle, 28, 542, 0);
        styleManager.add(assets.candle, 58, 572, 0);
        styleManager.add(assets.candle, 88, 602, 0);
        styleManager.add(assets.candleEvil, 904, y, 0);
        styleManager.add(assets.candleEvil, 972, y, 0);
        styleManager.add(assets.candleEvil, 1040, y, 0);

        var asset = MathUtils.random(100) < 20 ? assets.babe2 : assets.organGrinder;
        styleManager.add(asset, 73, Config.Screen.window_height - 130, true);
    }
}
