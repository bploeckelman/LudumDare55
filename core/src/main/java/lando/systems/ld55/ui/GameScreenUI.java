package lando.systems.ld55.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.actions.MoveAction;
import lando.systems.ld55.assets.Assets;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.entities.EnemyAI;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.screens.GameScreen;

public class GameScreenUI {

    public final Vector3 touchPos = new Vector3();

    public final GameScreen screen;
    public final ImageButton endTurnButton;
    public final ImageButton settingsButton;
    public final Rectangle settingsButtonPanelBound;

    public final NinePatch profilePanel;
    public final Rectangle profilePanelBounds;
    public HealthBar profileHealthBar;

    public final NinePatch actionsPanel;
    public final Rectangle actionsPanelBounds;
    public final Array<ActionPoint> actionPoints;

    public static class ActionPoint {
        public enum State {Empty, Temp, Full}
        public Rectangle bounds;
        public State state;

        public ActionPoint(Rectangle bounds, boolean active) {
            this.bounds = bounds;
            this.state = State.Full;
        }
    }

    public GameScreenUI(GameScreen screen) {
        this.screen = screen;
        endTurnButton = new ImageButton(10, 10, 200, 125,
            screen.assets.atlas.findRegion("icons/finish-turn-btn"),
            screen.assets.atlas.findRegion("icons/finish-turn-btn-hovered"),
            screen.assets.atlas.findRegion("icons/finish-turn-btn-pressed"),
            screen.assets.atlas.findRegion("icons/finish-turn-btn-disabled"));
        endTurnButton.boundsPolygon = new Polygon(new float[]{
            15, 135,
            98, 135,
            220, 10,
            15, 10,
        });
        endTurnButton.onClick = () -> {
            Main.game.audioManager.playSound(AudioManager.Sounds.lockIn);
            EnemyAI.doTurn(screen.gameBoard);
            screen.actionManager.endTurn();
            endTurnButton.pulse = false;

        };
        endTurnButton.imagePulse = new Animation<>(0.1f,
            screen.assets.atlas.findRegions("icons/finish-turn-btn-pulse"), Animation.PlayMode.LOOP);

        settingsButtonPanelBound = new Rectangle(Config.Screen.window_width - 50f, Config.Screen.window_height - 50f, 50f, 50f);
        settingsButton = new ImageButton(Config.Screen.window_width - 50f, Config.Screen.window_height - 50f, 50f, 50f,
            screen.assets.atlas.findRegion("icons/kenney-ui/gear"), null, null, null);
        settingsButton.onClick = () -> screen.settingsUI.showSettings();

        profilePanel = TileOverlayAssets.panelWhite;
        profilePanelBounds = new Rectangle(1205, 154, 60, 111);

        actionsPanel = TileOverlayAssets.panelWhite;
        actionsPanelBounds = new Rectangle(15, 154, 60, 111);
        actionPoints = new Array<>();
        refreshActionPoints();
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
        refreshActionPoints();
        touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        screen.worldCamera.unproject(touchPos);

        var pressed = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        var disabled = screen.actionManager.getCurrentPhase() != ActionManager.Phase.CollectActions;
//        endTurnButton.update(dt, touchPos, pressed, disabled);
        endTurnButton.update(dt, touchPos, pressed, disabled);
        settingsButton.update(dt, touchPos, pressed, false);

        var actionsAvailable = screen.actionManager.playerActionsAvailable;
        var tempActions = screen.actionManager.tempActionPoints;
        for (int i = 0; i < actionPoints.size; i++) {
            var point = actionPoints.get(i);
            point.state = ActionPoint.State.Empty;
            if (i < actionsAvailable) {
                point.state = ActionPoint.State.Temp;
            }
            if (i < tempActions) {
                point.state = ActionPoint.State.Full;
            }
        }
        var outOfActions = actionsAvailable == 0;
        var inCollectActionPhase = screen.actionManager.getCurrentPhase() == ActionManager.Phase.CollectActions;
        endTurnButton.pulse = outOfActions && inCollectActionPhase;
    }

    public void render(SpriteBatch batch) {
        endTurnButton.render(batch);
        batch.setColor(Color.DARK_GRAY);
        Assets.NinePatches.metal.draw(batch, settingsButtonPanelBound.x, settingsButtonPanelBound.y, settingsButtonPanelBound.width, settingsButtonPanelBound.height);
        batch.setColor(Color.WHITE);
        settingsButton.render(batch);

        // draw profile panel
        var board = screen.gameBoard;
        if (board.hoverTile != null) {
            var piece = board.getGamePiece(board.hoverTile);
            if (piece == null) {
                profileHealthBar = null;
            } else {
                var panel = profilePanelBounds;
                batch.setColor(piece.owner.color);
                profilePanel.draw(batch, panel.x, panel.y, panel.width, panel.height);
                batch.setColor(Color.WHITE);

                var margin = 0; // NOTE(brian) - this is intentional, in case we want to tweak sizing/positioning
                var portrait = piece.portrait.getKeyFrame(0);
                var size = panel.width - margin * 2;
                var flip = piece.owner == GamePiece.Owner.Enemy ? -1f : 1f;
                batch.draw(portrait,
                    panel.x + margin,
                    panel.y + (panel.height - size) / 2f,
                    size / 2f, size / 2f,
                    size, size,
                    flip, 1, 0);

                // draw other creature info in lower profile view panel thing
                // name, health, has move action queued, position in turn order, etc...
                var hMargin = 3f;
                var lineSpacing = 20f;
                var leftMin = 1080f;
                var leftMax = 1185f;
                var right = 1265f;
                var width = (right - leftMax) - (2 * hMargin);
                var widthBig = (right - leftMin) - (2 * hMargin);
                var bottom = 20f;
                var top = 135f;
                var yPos = top - 10f;
                var font = screen.assets.font;
                var layout = screen.assets.layout;

                // figure out where/if this creature is in the turn order
                var isMoving = false;
                var turn = -1;
                for (int i = 0; i < board.actionQueueUI.turnOrderUIItems.size; i++) {
                    var item = board.actionQueueUI.turnOrderUIItems.get(i);
                    if (item.action.getPiece() == piece) {
                        isMoving = (item.action instanceof MoveAction);
                        turn = i;
                        break;
                    }
                }
                var turnColor = (turn == -1) ? Color.GRAY: Color.WHITE;
                //var turnStr = (turn == -1) ? "No action" : "Turn #" + (turn + 1);
                var turnStr = (isMoving) ? "Move #" + (turn + 1) : "No Action";

                font.getData().setScale(0.5f);
                {
                    // piece owner name
                    layout.setText(font, piece.owner.name(), piece.owner.color, width, Align.right, false);
                    font.draw(batch, layout, leftMax, yPos);
                    yPos -= layout.height + lineSpacing;

                    // piece type name
                    layout.setText(font, piece.type.name(), Color.WHITE, width, Align.right, false);
                    font.draw(batch, layout, leftMax, yPos);
                    yPos -= layout.height + lineSpacing;

                    // position in turn order
                    layout.setText(font, turnStr, turnColor, widthBig, Align.right, false);
                    font.draw(batch, layout, leftMin, yPos);
                    yPos -= layout.height + lineSpacing;

                    // health bar
                    var x = leftMax;
                    var y = bottom + margin;
                    profileHealthBar = new HealthBar(piece.healthBar, x, y);
                    profileHealthBar.boxHeight *= 2;
                    profileHealthBar.updatePosition(x, y);
                    profileHealthBar.render(batch, 1f);
                }
                font.getData().setScale(1f);
            }
        }

        var actionsAvailable = screen.actionManager.playerActionsAvailable > 0;
        batch.setColor(actionsAvailable ? Color.LIGHT_GRAY : Color.DARK_GRAY);
        actionsPanel.draw(batch, actionsPanelBounds.x, actionsPanelBounds.y, actionsPanelBounds.width, actionsPanelBounds.height);

        for (var point : actionPoints) {
            // TODO(brian) - would be nice to change color through blue -> green -> yellow -> red as actions are used
            var color = Color.GRAY;
            switch (point.state){
                case Empty: break;
                case Temp: color = Color.YELLOW; break;
                case Full: color = Color.LIME; break;
            }
            if (!actionsAvailable) color = Color.DARK_GRAY;

            batch.setColor(color);
            actionsPanel.draw(batch, point.bounds.x, point.bounds.y, point.bounds.width, point.bounds.height);
        }
        batch.setColor(1, 1, 1, 1);
    }
}
