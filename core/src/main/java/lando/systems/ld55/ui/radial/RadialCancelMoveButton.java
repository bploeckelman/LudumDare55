package lando.systems.ld55.ui.radial;

import lando.systems.ld55.actions.ActionBase;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.GameTile;

public class RadialCancelMoveButton extends RadialButton {

    GameBoard board;
    GameTile moveTile;
    GamePiece piece;

    public RadialCancelMoveButton(GameBoard board, GamePiece piece, GameTile tile) {
        super(TileOverlayAssets.panelRed,
            TileOverlayAssets.disabledCross,
            "Confirm",
            true);

        pointsUsed = 1;
        this.board = board;
        this.moveTile = tile;
        this.piece = piece;
        if (board.gameScreen.actionManager.playerActionsAvailable > 0){
            text = "Cancel\n(1 Action)";
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
