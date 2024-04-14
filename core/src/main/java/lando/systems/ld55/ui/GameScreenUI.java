package lando.systems.ld55.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.screens.GameScreen;

public class GameScreenUI {

    public final GameScreen screen;
    public final ImageButton endTurnButton;
    public final Vector3 touchPos = new Vector3();

    public GameScreenUI(GameScreen screen) {
        this.screen = screen;
        endTurnButton = new ImageButton(15, 15, 125, 125,
            screen.assets.atlas.findRegion("icons/end-turn-btn"),
            screen.assets.atlas.findRegion("icons/end-turn-btn-hover"),
            screen.assets.atlas.findRegion("icons/end-turn-btn-press"),
            screen.assets.atlas.findRegion("icons/end-turn-btn-disabled"));
        endTurnButton.onClick = () -> screen.actionManager.endTurn();
    }

    public void update(float dt) {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        screen.worldCamera.unproject(touchPos);

        var pressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        var disabled = screen.actionManager.getCurrentPhase() != ActionManager.Phase.CollectActions;
        endTurnButton.update(dt, touchPos, pressed, disabled);
    }

    public void render(SpriteBatch batch) {
        endTurnButton.render(batch);
    }
}
