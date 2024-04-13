package lando.systems.ld55.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;

public class TitleScreen extends BaseScreen {

    float accum = 0;
    boolean drawUI = true;

    public TitleScreen() {
        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    public void initializeUI() {
        super.initializeUI();
        //TitleScreenUI titleScreenUI = new TitleScreenUI(this);
        //uiStage.addActor(titleScreenUI);
    }

    @Override
    public void alwaysUpdate(float delta) {
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        accum+=dt;

        if (Gdx.input.justTouched() && !exitingScreen ) {
            exitingScreen = true;
            game.setScreen(new GameScreen());
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
            Texture gdx = assets.gdx;
            float width = worldCamera.viewportWidth;
            float height = worldCamera.viewportHeight;
            batch.draw(gdx, (width - gdx.getWidth()) / 2f, (height - gdx.getHeight()) / 2f);

            var font = assets.fontAbandoned;
            var layout = new GlyphLayout();
            layout.setText(font, "Coming Soon - \nClock Gobblers", Color.WHITE, width, Align.center, true);
            font.draw(batch, layout, 0, height / 3f);
        }
        batch.end();

        if (drawUI) {
            uiStage.draw();
        }
    }

    private float calculateRotation(Vector2 shipStartingPos, Vector2 shipDestinationPos) {
        var rotation = Math.atan2(shipDestinationPos.y - shipStartingPos.y, shipDestinationPos.x - shipStartingPos.x);
        return (float) Math.toDegrees(rotation);
    }
}
