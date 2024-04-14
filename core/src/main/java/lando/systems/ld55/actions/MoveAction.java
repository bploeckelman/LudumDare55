package lando.systems.ld55.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
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
    boolean turnOver;
    boolean blocked;
    GamePiece blockingPiece;
    float blockAccum;
    Vector2 startMovePos = new Vector2();
    Vector2 targetMove = new Vector2();

    public MoveAction(GameBoard board, GamePiece piece, GameTile targetTile) {
        if (targetTile == null){
            Gdx.app.log("MoveAction", "TargetTile was null");
        }
        Gdx.app.log("MoveAction", "Move action crated");
        this.piece = piece;
        this.targetTile = targetTile;
        this.board = board;
        turnOver = false;
        reset();
    }

    @Override
    public boolean isCompleted() {
        return piece.currentTile == targetTile || targetTile == null;
    }

    @Override
    public boolean doneTurn() {
        return turnOver || (!piece.isMoving && piece.currentTile == nextTile);
    }

    @Override
    public void update(float dt) {
        if (blocked) {
            blockAccum += dt;
            if (blockAccum > GamePiece.moveSeconds){
                blockAccum = GamePiece.moveSeconds;
            }
            float halfWayTime = GamePiece.moveSeconds/2f;
            float quarterTime = GamePiece.moveSeconds/4f;
            if (blockAccum < halfWayTime){
                piece.position.set(startMovePos);
                piece.position.interpolate(targetMove, blockAccum / halfWayTime, Interpolation.linear);
            } else {
                // bouncing back
                piece.position.set(targetMove);
                piece.position.interpolate(startMovePos, (blockAccum-halfWayTime) / halfWayTime, Interpolation.linear);
            }
            piece.setPosition(piece.position.x, piece.position.y);
            if (blockAccum - dt < halfWayTime && blockAccum > halfWayTime){
                if (blockingPiece.owner != piece.owner){
                    // Attack
                    // TODO: Melee attack sound
                    // TODO: do damage
                } else {
                    // friendly,  TODO: bounce sound
                }
            }

            if (blockAccum >= GamePiece.moveSeconds) {
                turnOver = true;
            }
        } else if (!piece.isMoving && piece.currentTile != nextTile) {
            // Can move into tile?
            for (GamePiece p : board.gamePieces){
                if (p.currentTile == nextTile){
                    blockingPiece = p;
                    blocked = true;
                }
            }

            if (blocked) {
                startMovePos.set(piece.currentTile.center);
                targetMove.set(nextTile.center).sub(piece.currentTile.center).scl(.5f).add(piece.currentTile.center);
                // initiate a block and/or attack
            } else {
                piece.moveToTile(nextTile);
            }
        }


    }

    @Override
    public GamePiece getPiece() {
        return piece;
    }

    @Override
    public void reset() {
        blockAccum = 0;
        blockingPiece = null;
        blocked = false;
        turnOver = false;
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
