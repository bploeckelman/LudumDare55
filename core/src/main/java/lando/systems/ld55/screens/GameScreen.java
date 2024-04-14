package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.StyleManager;
import lando.systems.ld55.entities.StylePiece;
import lando.systems.ld55.particles.Particles;
import lando.systems.ld55.ui.GameScreenUI;

public class GameScreen extends BaseScreen{
    public GameBoard gameBoard;
    public Particles particles;
    public ActionManager actionManager;
    public GameScreenUI ui;
    private StyleManager styleManager = new StyleManager();

    public enum GameMode { None, Summon, Move }
    public GameMode currentGameMode = GameMode.Summon;

    public GameScreen() {
        gameBoard = new GameBoard(this, 22, 10);
        particles = new Particles(assets);
        actionManager = new ActionManager(this);
        ui = new GameScreenUI(this);
        Gdx.input.setInputProcessor(new InputMultiplexer(gameBoard));
        Main.game.audioManager.playMusic(AudioManager.Musics.mainMusic);
        setupStyle();
    }

    private void setupStyle() {
        float width = 400;
        float x = Config.Screen.window_width / 2f - width;
        float div = width / 5;
        float y = 75;
        styleManager.add(assets.candle, x += div, y);
        styleManager.add(assets.candle, x += div, y);
        styleManager.add(assets.candle, x += div, y);
        styleManager.add(assets.candle, x += div, y);
        styleManager.add(assets.candleEvil, x += div * 2, y);
        styleManager.add(assets.candleEvil, x += div, y);
        styleManager.add(assets.candleEvil, x += div, y);
        styleManager.add(assets.candleEvil, x += div, y);
    }

    @Override
    public void alwaysUpdate(float delta) {

    }

    public void update(float dt) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        gameBoard.update(dt);
        particles.update(dt);
        actionManager.update(dt);
        ui.update(dt);

        // useless click effect outside of board that can be removed if needed.
        var touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCamera.unproject(touchPos);
        if (Gdx.input.justTouched() && gameBoard.hoverTile == null) {
            particles.portal(touchPos.x, touchPos.y, 20f);
            Main.game.audioManager.playSound(AudioManager.Sounds.idle_click);
//            Main.game.audioManager.playSound(AudioManager.Sounds.error_sound);
        }

        if(Gdx.input.justTouched() && gameBoard.hoverTile != null) {
            Main.game.audioManager.playSound(AudioManager.Sounds.error_sound, .3f);
        }

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
            styleManager.render(batch);
            actionManager.render(batch);
        }
        batch.end();

        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        {
            ui.render(batch);

            var font = assets.fontAbandonedMed;
            var layout = assets.layout;
            layout.setText(font, "--- [Turn Order] ---", Color.WHITE, windowCamera.viewportWidth, Align.center, false);
            font.draw(batch, layout, 0, 50);

            font = assets.font;
            var phase = actionManager.getCurrentPhase();
            var turnLabel = "Turn: " + (actionManager.getTurnNumber() +1);
            var phaseLabel = "Ohh Ohh";
            switch(phase){
                case CollectActions: phaseLabel = "Planning"; break;
                case ResolveActions: phaseLabel = "Resolving Actions"; break;
                case Attack:         phaseLabel = "Attacking"; break;
            }
            phaseLabel = "Phase: " + phaseLabel;

            font.getData().setScale(.5f);
            {
                layout.setText(font, phaseLabel, Color.WHITE, windowCamera.viewportWidth, Align.center, false);
                font.draw(batch, layout, 0, windowCamera.viewportHeight - 20);

                layout.setText(font, turnLabel, Color.WHITE, windowCamera.viewportWidth, Align.center, false);
                font.draw(batch, layout, 0, windowCamera.viewportHeight - 40);
            }
            font.getData().setScale(1f);

            particles.draw(batch, Particles.Layer.FOREGROUND);
        }
        batch.end();
    }
}
