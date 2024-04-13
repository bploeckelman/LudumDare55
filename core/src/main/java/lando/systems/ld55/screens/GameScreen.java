package lando.systems.ld55.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.entities.GameBoard;

public class GameScreen extends BaseScreen{
    public GameBoard gameBoard;

    public GameScreen() {
        // TODO bring in size or level etc
        gameBoard = new GameBoard(this, 10);
    }

    @Override
    public void alwaysUpdate(float delta) {

    }

    public void update(float dt) {
        gameBoard.update(dt);
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
            gameBoard.render(batch);
        }
        batch.end();
    }
}
