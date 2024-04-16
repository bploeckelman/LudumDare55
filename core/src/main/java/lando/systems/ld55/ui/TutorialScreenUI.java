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
import lando.systems.ld55.screens.TutorialScreen;

public class TutorialScreenUI {

    public final Vector3 touchPos = new Vector3();

    public final TutorialScreen screen;
    public final ImageButton endTurnButton;

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

    public TutorialScreenUI(TutorialScreen screen) {
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
        endTurnButton.imagePulse = new Animation<>(0.1f,
            screen.assets.atlas.findRegions("icons/finish-turn-btn-pulse"), Animation.PlayMode.LOOP);


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
        var disabled = true;
//        endTurnButton.update(dt, touchPos, pressed, disabled);
        endTurnButton.update(dt, touchPos, pressed, disabled);

        var actionsAvailable = 3;
        var tempActions = 3;
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
    }

    public void render(SpriteBatch batch) {
        endTurnButton.render(batch);
        batch.setColor(Color.WHITE);

        var actionsAvailable = true;
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
