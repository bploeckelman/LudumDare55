package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.Main;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.particles.Particles;
import lando.systems.ld55.ui.GameScreenUI;

public class GameScreen extends BaseScreen{
    public GameBoard gameBoard;
    public Particles particles;
    public ActionManager actionManager;
    public GameScreenUI ui;
    public enum GameMode { None, Summon, Move }
    private GameMode currentGameMode = GameMode.None;

    public GameScreen() {
        gameBoard = new GameBoard(this, 22, 10);
        particles = new Particles(assets);
        actionManager = new ActionManager();
        ui = new GameScreenUI(this);
        Gdx.input.setInputProcessor(new InputMultiplexer(gameBoard));
        Main.game.audioManager.playMusic(AudioManager.Musics.mainMusic);
    }

    public void setMode(GameMode mode) {
        currentGameMode = mode;
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
            particles.draw(batch, Particles.Layer.BACKGROUND);
            gameBoard.render(batch);
            ui.render(batch); // TODO(brian) - should technically be in windowCam, but then particles draw under it
            particles.draw(batch, Particles.Layer.FOREGROUND);
            actionManager.render(batch);
        }
        batch.end();

        batch.setProjectionMatrix(windowCamera.combined);
        batch.begin();
        {
            ActionManager.Phase actionPhase = actionManager.getCurrentPhase();
            String phaseString = "Ohh Ohh";
            switch(actionPhase){
                case CollectActions:
                    phaseString = "Planning";
                    break;
                case ResolveActions:
                    phaseString = "Resolving Actions";
                    break;
                case Attack:
                    phaseString = "Attacking";
                    break;
            }
            phaseString = "Phase: " + phaseString;
            String turnString = "Turn: " + (actionManager.getTurnNumber() +1);
            assets.font.getData().setScale(.5f);
            assets.font.draw(batch, phaseString, 0, windowCamera.viewportHeight - 20, windowCamera.viewportWidth, Align.center, true);
            assets.font.draw(batch, turnString, 0, windowCamera.viewportHeight - 40, windowCamera.viewportWidth, Align.center, true);
            assets.font.getData().setScale(1f);
        }
        batch.end();
    }
}
