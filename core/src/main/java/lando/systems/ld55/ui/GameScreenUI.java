package lando.systems.ld55.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.entities.EnemyAI;
import lando.systems.ld55.screens.GameScreen;

public class GameScreenUI {

    public final Vector3 touchPos = new Vector3();

    public final GameScreen screen;
    public final ImageButton endTurnButton;
//    public final ImageButton summonButton;
//    public final ImageButton moveButton;
    public final NinePatch actionsPanel;
    public final Rectangle actionsPanelBounds;
    public final Array<ActionPoint> actionPoints;

    public static class ActionPoint {
        public Rectangle bounds;
        public boolean active;

        public ActionPoint(Rectangle bounds, boolean active) {
            this.bounds = bounds;
            this.active = active;
        }
    }

    public GameScreenUI(GameScreen screen) {
        this.screen = screen;
        endTurnButton = new ImageButton(10, 10, 200, 125,
            screen.assets.atlas.findRegion("icons/finish-turn-btn"),
            screen.assets.atlas.findRegion("icons/finish-turn-btn-hovered"),
            screen.assets.atlas.findRegion("icons/finish-turn-btn-pressed"),
            screen.assets.atlas.findRegion("icons/finish-turn-btn-disabled"));
        endTurnButton.onClick = () -> {
            EnemyAI.doTurn(screen.gameBoard);
            screen.actionManager.endTurn();
        };

        actionsPanel = TileOverlayAssets.panelWhite;
        actionsPanelBounds = new Rectangle(15, 154, 60, 111);
        actionPoints = new Array<>();
        refreshActionPoints();

//        var summonIcon = screen.assets.atlas.findRegion("icons/kenney-board-game/pawns");
//        summonButton = new ImageButton(20, 280 - 70, 50, 50, summonIcon, null, null, null);
//        summonButton.backgroundDefault = Assets.Patch.glass_dim.ninePatch;
//        summonButton.onClick = () -> screen.currentGameMode = GameScreen.GameMode.Summon;
//
//        var moveIcon = screen.assets.atlas.findRegion("icons/kenney-board-game/arrow_diagonal_cross");
//        moveButton = new ImageButton(20, 280 - 130, 50, 50, moveIcon, null, null, null);
//        moveButton.backgroundDefault = Assets.Patch.glass_dim.ninePatch;
//        moveButton.onClick = () -> screen.currentGameMode = GameScreen.GameMode.Move;
    }

    private void refreshActionPoints() {
        actionPoints.clear();
        var margin = 2f;
        var numSlots = ActionManager.ActionsPerTurn;
        var availablePanelHeight = actionsPanelBounds.height - margin * 2;
        var slotWidth = actionsPanelBounds.width - margin * 2;
        var slotHeight = (availablePanelHeight / numSlots) - margin;
        for (int i = 0; i < numSlots; i++) {
            var bounds = new Rectangle(
                actionsPanelBounds.x + margin,
                actionsPanelBounds.y + margin + i * (slotHeight + margin),
                slotWidth, slotHeight);
            var point = new ActionPoint(bounds, true);
            actionPoints.add(point);
        }
    }

    public void update(float dt) {
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        screen.worldCamera.unproject(touchPos);

        var pressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        var disabled = screen.actionManager.getCurrentPhase() != ActionManager.Phase.CollectActions;
        endTurnButton.update(dt, touchPos, pressed, disabled);

//        summonButton.active = screen.currentGameMode == GameScreen.GameMode.Summon;
//        moveButton.active = screen.currentGameMode == GameScreen.GameMode.Move;

//        summonButton.update(dt, touchPos, pressed, false);
//        moveButton.update(dt, touchPos, pressed, false);

        var actionsAvailable = screen.actionManager.playerActionsAvailable;
        for (int i = 0; i < actionPoints.size; i++) {
            var point = actionPoints.get(i);
            point.active = i < actionsAvailable;
        }
    }

    public void render(SpriteBatch batch) {
        endTurnButton.render(batch);
//        summonButton.render(batch);
//        moveButton.render(batch);

        var actionsAvailable = screen.actionManager.playerActionsAvailable > 0;
        batch.setColor(actionsAvailable ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        actionsPanel.draw(batch, actionsPanelBounds.x, actionsPanelBounds.y, actionsPanelBounds.width, actionsPanelBounds.height);

        for (var point : actionPoints) {
            // TODO(brian) - would be nice to change color through blue -> green -> yellow -> red as actions are used
            var color = point.active ? Color.LIME : Color.GRAY;
            if (!actionsAvailable) color = Color.DARK_GRAY;

            batch.setColor(color);
            actionsPanel.draw(batch, point.bounds.x, point.bounds.y, point.bounds.width, point.bounds.height);
        }
        batch.setColor(1, 1, 1, 1);
    }
}
