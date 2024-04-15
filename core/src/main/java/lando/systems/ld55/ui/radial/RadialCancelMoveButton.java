package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld55.Main;
import lando.systems.ld55.actions.ActionBase;
import lando.systems.ld55.actions.MoveAction;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.GameTile;

public class RadialCancelMoveButton extends RadialButton {

    GameBoard board;
    GameTile moveTile;
    GamePiece piece;

    public RadialCancelMoveButton(GameBoard board, GamePiece piece, GameTile tile) {
        super(TileOverlayAssets.panelRed, TileOverlayAssets.cross, "Confirm", true);
        this.board = board;
        this.moveTile = tile;
        this.piece = piece;
        if (board.gameScreen.actionManager.playerActionsAvailable > 0){
            text = "Cancel\n1 Action";
            enabled= true;
        } else {
            text = "No Actions\nfor move";
            enabled = false;
        }
    }

    @Override
    public void onClick() {
        board.gameScreen.actionManager.playerActionsAvailable--;
        ActionBase move = piece.currentAction;
        board.gameScreen.actionManager.removeAction(move);
        piece.currentAction = null;
    }
}
