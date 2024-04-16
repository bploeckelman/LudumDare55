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
        super(TileOverlayAssets.panelWhite,
            TileOverlayAssets.disabledCross,
            "", //"Confirm",
            true);

        pointsUsed = 1;
        this.board = board;
        this.moveTile = tile;
        this.piece = piece;
        this.iconRadiusScale = 1.25f;
        this.iconEnabledColor.set(1, 1, 1, 0.4f);
        this.backgroundEnabledColor.set(0.5f, 0.5f, 0.5f, 1f);
        if (board.gameScreen.actionManager.playerActionsAvailable > 0){
//            text = "Stop\nmove\n$1";
            text = " Stop \n$1";
            enabled= true;
        } else {
            text = "No move\nto cancel";
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
