package lando.systems.ld55.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.actions.ActionBase;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.GameTile;

public class ActionQueueUI {
    public ActionManager actionManager;
    public GameBoard gameBoard;
    public Array<ActionItemUI> turnOrderUIItems;
    public ActionItemUI hoveredAction;

    public ActionQueueUI(ActionManager manager, GameBoard board) {
        this.actionManager = manager;
        this.gameBoard = board;
        turnOrderUIItems = new Array<>();
    }

    Vector3 mousePos = new Vector3();
    public void update(float dt) {
        int tiles = Math.min(actionManager.getActionQueue().size, 80);
        int rows = 0;
        if (tiles > 5) rows = 1;
        if (tiles > 20) rows = 2;
//        if (tiles > 60) rows = 3;
        int itemsPerRow = (int) (Math.pow(2, rows) * 5);
        float tileSize = (float) (65f / (Math.pow(2, rows))) - 8;
        float margin = (float) (5f / (Math.pow(2, rows)));
        for (ActionItemUI item : turnOrderUIItems) {
            // Mark as remove, we will mark keep if we find it, and then remove others as needed
            item.remove = true;
        }

        Rectangle r = new Rectangle();
        for (int i = 0; i < tiles; i ++){
            r.set(465 + (tileSize + margin) * (i % itemsPerRow), 74 - ((1+(i / itemsPerRow)) * (tileSize + margin)- margin), tileSize, tileSize);
            ActionBase action = actionManager.getActionQueue().get(i);
            boolean found = false;
            // O(n^2) fuck you
            for (ActionItemUI item : turnOrderUIItems) {
                if (item.action == action) {
                    found = true;
                    item.remove = false;
                    // Move it
                    item.updateRect(r);
                }
            }
            if (!found) {
                turnOrderUIItems.add(new ActionItemUI(action, r));
            }
        }
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameBoard.gameScreen.worldCamera.unproject(mousePos);
        hoveredAction = null;
        GameTile hoverTile = gameBoard.hoverTile;
        GamePiece hoverPiece = null;
        if (hoverTile != null) {
            hoverPiece = gameBoard.getGamePiece(hoverTile);
        }
        for (int i = turnOrderUIItems.size -1; i >= 0; i --) {
            ActionItemUI item = turnOrderUIItems.get(i);
            item.highlight = false;
            if (actionManager.getCurrentPhase() == ActionManager.Phase.ResolveActions){
                if (i == actionManager.currentAction) {
                    item.highlight = true;
                }
            } if (actionManager.getCurrentPhase() == ActionManager.Phase.CollectActions) {
                if (item.bounds.contains(mousePos.x, mousePos.y)){
                    item.highlight = true;
                    hoveredAction = item;
                } else if (hoverPiece != null && hoverPiece == item.action.getPiece()){
                    item.highlight = true;
                }
            }
            item.update(dt);
            if (item.remove) {
                turnOrderUIItems.removeIndex(i);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (ActionItemUI item : turnOrderUIItems) {
            item.render(batch);
        }
    }
}
