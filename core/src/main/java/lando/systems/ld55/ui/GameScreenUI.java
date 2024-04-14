package lando.systems.ld55.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.assets.Assets;
import lando.systems.ld55.screens.GameScreen;

public class GameScreenUI {

    public final GameScreen screen;
    public final ImageButton endTurnButton;
    public final ImageButton summonButton;
    public final ImageButton moveButton;
    public final Vector3 touchPos = new Vector3();

    public GameScreenUI(GameScreen screen) {
        this.screen = screen;
        endTurnButton = new ImageButton(15, 15, 200, 125,
            screen.assets.atlas.findRegion("icons/finish-turn-btn"),
            screen.assets.atlas.findRegion("icons/finish-turn-btn-hovered"),
            screen.assets.atlas.findRegion("icons/finish-turn-btn-pressed"),
            screen.assets.atlas.findRegion("icons/finish-turn-btn-disabled"));
        endTurnButton.onClick = () -> screen.actionManager.endTurn();

        var summonIcon = screen.assets.atlas.findRegion("icons/kenney-board-game/pawns");
        summonButton = new ImageButton(20, 280 - 70, 50, 50, summonIcon, null, null, null);
        summonButton.backgroundDefault = Assets.Patch.glass_dim.ninePatch;
        summonButton.onClick = () -> screen.currentGameMode = GameScreen.GameMode.Summon;

        var moveIcon = screen.assets.atlas.findRegion("icons/kenney-board-game/arrow_diagonal_cross");
        moveButton = new ImageButton(20, 280 - 130, 50, 50, moveIcon, null, null, null);
        moveButton.backgroundDefault = Assets.Patch.glass_dim.ninePatch;
        moveButton.onClick = () -> screen.currentGameMode = GameScreen.GameMode.Move;
    }

    public void update(float dt) {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        screen.worldCamera.unproject(touchPos);

        var pressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        var disabled = screen.actionManager.getCurrentPhase() != ActionManager.Phase.CollectActions;
        endTurnButton.update(dt, touchPos, pressed, disabled);

        summonButton.active = screen.currentGameMode == GameScreen.GameMode.Summon;
        moveButton.active = screen.currentGameMode == GameScreen.GameMode.Move;

        summonButton.update(dt, touchPos, pressed, false);
        moveButton.update(dt, touchPos, pressed, false);
    }

    public void render(SpriteBatch batch) {
        endTurnButton.render(batch);
        summonButton.render(batch);
        moveButton.render(batch);
    }
}
