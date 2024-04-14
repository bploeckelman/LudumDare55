package lando.systems.ld55.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld55.Main;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.GameTile;

public class MoveAction extends ActionBase {

    GamePiece piece;
    GameTile targetTile;
    GameTile nextTile;
    float moveAmount;
    GameBoard board;

    public MoveAction(GameBoard board, GamePiece piece, GameTile targetTile) {
        if (targetTile == null){
            Gdx.app.log("MoveAction", "TargetTile was null");
        }
        Gdx.app.log("MoveAction", "Move action crated");
        this.piece = piece;
        this.targetTile = targetTile;
        this.board = board;
        reset();
    }

    @Override
    public boolean isCompleted() {
        return piece.currentTile == targetTile || targetTile == null;
    }

    @Override
    public boolean doneTurn() {
        return !piece.isMoving && piece.currentTile == nextTile;
    }

    @Override
    public void update(float dt) {
        if (!piece.isMoving && piece.currentTile != nextTile) {
            piece.moveToTile(nextTile);
        }


    }

    @Override
    public GamePiece getPiece() {
        return piece;
    }

    @Override
    public void reset() {
        moveAmount = 0;
        int nextX = (int)Math.signum(targetTile.x - piece.currentTile.x) + piece.currentTile.x;
        int nextY = (int)Math.signum(targetTile.y - piece.currentTile.y) + piece.currentTile.y;
        nextTile = board.getTileAt(nextX, nextY);

    }

    Vector2 startPos = new Vector2();
    Vector2 endPos = new Vector2();
    Vector2 delta = new Vector2();
    @Override
    public void render(SpriteBatch batch) {
        startPos.set(piece.currentTile.bounds.getCenter(startPos));
        if (targetTile != null) {
            endPos.set(targetTile.bounds.getCenter(endPos));
        } else {
            endPos.set(startPos);
        }
        delta.set(endPos).sub(startPos);

        float width = 1f;
        batch.setColor(Color.YELLOW);
        batch.draw(Main.game.assets.pixelRegion, startPos.x, startPos.y - width/2f, 0, width/2f, delta.len(), width, 1, 1, delta.angle());
    }


}
