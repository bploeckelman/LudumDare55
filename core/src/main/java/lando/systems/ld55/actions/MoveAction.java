package lando.systems.ld55.actions;

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
    public void reset() {
        moveAmount = 0;
        int nextX = (int)Math.signum(targetTile.x - piece.currentTile.x) + piece.currentTile.x;
        int nextY = (int)Math.signum(targetTile.y - piece.currentTile.y) + piece.currentTile.y;
        nextTile = board.getTileAt(nextX, nextY);

    }
}
