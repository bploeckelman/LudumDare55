package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.actions.MoveAction;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.GameTile;
import lando.systems.ld55.particles.Particles;

public class GameScreen extends BaseScreen{
    public GameBoard gameBoard;
    public Particles particles;
    public GamePiece gamePiece;
    public ActionManager actionManager;

    public GameScreen() {
        // TODO bring in size or level etc
        gameBoard = new GameBoard(this, 10);
        particles = new Particles(assets);
        gamePiece = new GamePiece(assets.cherry, assets.cherry);
        actionManager = new ActionManager();
        Gdx.input.setInputProcessor(new InputMultiplexer(gameBoard));
    }

    @Override
    public void alwaysUpdate(float delta) {

    }

    public void selectTile(GameTile tile) {
        gamePiece.selectTile(tile);
    }

    public void update(float dt) {
        gameBoard.update(dt);
        particles.update(dt);
        gamePiece.update(dt);
        actionManager.update(dt);

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
            MoveAction moveAction = new MoveAction(gameBoard, gamePiece, gameBoard.getTileAt(MathUtils.random(10), MathUtils.random(10)));
            actionManager.addAction(moveAction);
        }
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
        }

        gamePiece.render(batch);
        batch.end();
    }
}
