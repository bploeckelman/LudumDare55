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

public class GameScreen extends BaseScreen{
    public GameBoard gameBoard;
    public Particles particles;
    public ActionManager actionManager;

    public GameScreen() {
        gameBoard = new GameBoard(this, 22, 10);
        particles = new Particles(assets);
        actionManager = new ActionManager();
        Gdx.input.setInputProcessor(new InputMultiplexer(gameBoard));
    }

    @Override
    public void alwaysUpdate(float delta) {

    }

    public void update(float dt) {
        gameBoard.update(dt);
        particles.update(dt);
        actionManager.update(dt);

        // useless click effect outside of board that can be removed if needed.
//        Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
//        worldCamera.unproject(touchPos);
//        if (Gdx.input.justTouched() && gameBoard.hoverTile == null) {
//            particles.tinySmoke(touchPos.x, touchPos.y);
//            Main.game.audioManager.playSound(AudioManager.Sounds.idle_click);
//        }

        // if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
        //     if (gamePiece.currentAction == null) {
        //         MoveAction moveAction = new MoveAction(gameBoard, gamePiece, gameBoard.getTileAt(MathUtils.random(9), MathUtils.random(9)));
        //         gamePiece.currentAction = moveAction;
        //         actionManager.addAction(moveAction);
        //     }
        // }
         if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
             actionManager.endTurn();
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
