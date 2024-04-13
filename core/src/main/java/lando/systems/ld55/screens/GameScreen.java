package lando.systems.ld55.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.particles.Particles;

public class GameScreen extends BaseScreen{
    public GameBoard gameBoard;
    public Particles particles;

    public GameScreen() {
        // TODO bring in size or level etc
        gameBoard = new GameBoard(this, 10);
        particles = new Particles(assets);
    }

    @Override
    public void alwaysUpdate(float delta) {

    }

    public void update(float dt) {
        gameBoard.update(dt);
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
            particles.draw(batch, Particles.Layer.BACKGROUND);
            gameBoard.render(batch);
            particles.draw(batch, Particles.Layer.FOREGROUND);
        }
        batch.end();
    }
}
