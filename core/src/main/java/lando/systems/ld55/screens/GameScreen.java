package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.Stats;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.StyleManager;
import lando.systems.ld55.entities.TileOverlayInfo;
import lando.systems.ld55.particles.Particles;
import lando.systems.ld55.ui.GameScreenUI;
import lando.systems.ld55.ui.SettingsUI;

public class GameScreen extends BaseScreen{
    public GameBoard gameBoard;
    public static Particles particles;
    public ActionManager actionManager;
    public GameScreenUI ui;
    public SettingsUI settingsUI;
    public final StyleManager styleManager = new StyleManager();
    private float gameOverTime = 5;
    private boolean gameHasEnded;



    public GameScreen() {
        Stats.reset();
        actionManager = new ActionManager(this);
        gameBoard = new GameBoard(this, 10, 8);
        particles = new Particles(assets);
        ui = new GameScreenUI(this);
        settingsUI = new SettingsUI(skin, windowCamera);
        uiStage.addActor(settingsUI);
        gameHasEnded = false;
        Gdx.input.setInputProcessor(new InputMultiplexer(uiStage, gameBoard));
        Main.game.audioManager.playMusic(AudioManager.Musics.mainMusic);
        setupStyle();
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

        styleManager.add(assets.thrones.get(0),     140, 350, true);
        styleManager.add(assets.king.get(0).get(0), 180, 350, true);
        styleManager.add(assets.thrones.get(1),     1140, 350, false);
        styleManager.add(assets.king.get(1).get(0), 1100, 350, false);
    }

    @Override
    public void alwaysUpdate(float delta) {

    }

    public void update(float dt) {

        particles.update(dt);
        if (settingsUI.isSettingShown) {
            uiStage.act(dt);
            return;
        }

        if (gameOver && !exitingScreen) {
            gameOverTime -= dt;
            if (gameOverTime < 0) {
                exitingScreen = true;
                game.setScreen(new CreditScreen());
                return;
            }
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        gameBoard.update(dt);
        actionManager.update(dt);
        ui.update(dt);

        // useless click effect outside of board that can be removed if needed.
        var touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCamera.unproject(touchPos);
        if (Gdx.input.justTouched() && gameBoard.hoverTile == null) {
            particles.levelUpEffect(touchPos.x, touchPos.y);
//            Main.game.audioManager.playSound(AudioManager.Sounds.pew);
//            Main.game.audioManager.playSound(AudioManager.Sounds.error_buzz);
        }

//        if(Gdx.input.justTouched() && gameBoard.hoverTile != null) {
//            if(gameBoard.hoverTile.summonable) {
//                Main.game.audioManager.playSound(AudioManager.Sounds.click, .3f);
//            }
//            else {
//                Main.game.audioManager.playSound(AudioManager.Sounds.error_buzz, .3f);
//            }
//
//        }

        styleManager.update(dt);
    }

    @Override
    public void renderFrameBuffers(SpriteBatch batch) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
        batch.setProjectionMatrix(worldCamera.combined);
        gameBoard.renderFrameBuffers(batch);
    }

    @Override
    public void render(SpriteBatch batch) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.setProjectionMatrix(worldCamera.combined);
        batch.begin();
        {
            particles.draw(batch, Particles.Layer.BACKGROUND);
            gameBoard.render(batch);
            //styleManager.render(batch); // NOTE(brian) - moved into gameBoard.render in order to control draw order
            actionManager.render(batch);
        }
        batch.end();

        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        {
            ui.render(batch);

            var font = assets.fontAbandonedMed;
            var layout = assets.layout;
//            layout.setText(font, "--- [Turn Order] ---", Color.WHITE, windowCamera.viewportWidth, Align.center, false);
//            font.draw(batch, layout, 0, 50);

            font = assets.font;
            var phase = actionManager.getCurrentPhase();
            var phaseLabel = "Ohh Ohh";
            switch(phase){
                case CollectActions: phaseLabel = "Planning"; break;
                case ResolveActions: phaseLabel = "Resolving Actions"; break;
                case Attack:         phaseLabel = "Attacking"; break;
            }
            phaseLabel = phaseLabel + " Phase";
            var actionsPlural = actionManager.playerActionsAvailable == 1 ? "" : "s";
            var actionsRemainingLabel = "Action" + actionsPlural + " Remaining: " + actionManager.playerActionsAvailable;
            var turnLabel = "Turn: " + (actionManager.getTurnNumber() +1);

            font.getData().setScale(0.5f);
            {
                layout.setText(font, actionsRemainingLabel, Color.WHITE, windowCamera.viewportWidth, Align.center, false);
                font.draw(batch, layout, 0, windowCamera.viewportHeight - 25);

//                layout.setText(font, phaseLabel, Color.WHITE, windowCamera.viewportWidth, Align.center, false);
//                font.draw(batch, layout, 0, windowCamera.viewportHeight - 40);

                layout.setText(font, turnLabel, Color.WHITE, windowCamera.viewportWidth, Align.center, false);
                font.draw(batch, layout, 0, windowCamera.viewportHeight - 55);
//                font.draw(batch, layout, 0, windowCamera.viewportHeight - 60);
            }
            font.getData().setScale(1f);

            if (gameBoard.radialMenu != null) {
                gameBoard.radialMenu.render(batch);
            }

            if (gameOver) {
                if(win) {
                    if(!gameHasEnded) {
                        Main.game.audioManager.stopMusic();
                        Main.game.audioManager.playSound(AudioManager.Sounds.victory_fanfare);
                        gameHasEnded = true;
                    }


                    layout.setText(font, "You win!" , Color.WHITE, windowCamera.viewportWidth, Align.center, false);
                }
                else {
                    if(!gameHasEnded) {
                        Main.game.audioManager.stopMusic();
                        Main.game.audioManager.playSound(AudioManager.Sounds.funeral_dirge);
                        gameHasEnded = true;
                    }

                    layout.setText(font, "You Died..." , Color.RED, windowCamera.viewportWidth, Align.center, false);
                }

                font.draw(batch, layout, 0, windowCamera.viewportHeight / 2);
            }

            particles.draw(batch, Particles.Layer.FOREGROUND);
        }
        batch.end();

        if (settingsUI.isSettingShown) {
            uiStage.draw();
        }
    }

    public boolean gameOver = false;
    private boolean win = false;
    public void gameOver(boolean win) {
        if (gameOver) return;
        gameOver = true;
        this.win = win;
        particles.gameOver(win);
    }

    public void showAttack(GamePiece attacker, GamePiece defender) {
        var attackActionOverlay = gameBoard.attackActionOverlay;
        attackActionOverlay.clear();
        attackActionOverlay.add(new TileOverlayInfo(attacker.currentTile, 0)
            .addLayer("attacker", 1f, Color.RED, 0.5f, TileOverlayAssets.panelWhite, null, null));

        attackActionOverlay.add(new TileOverlayInfo(defender.currentTile, 0)
            .addLayer("defender", 1f, Color.WHITE, 0.7f, TileOverlayAssets.panelWhite, null, null));
    }

    public void clearAttack() {
        gameBoard.attackActionOverlay.clear();
    }
}
