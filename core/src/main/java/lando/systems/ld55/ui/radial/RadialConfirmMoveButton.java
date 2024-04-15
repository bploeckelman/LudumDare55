package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld55.Main;
import lando.systems.ld55.actions.ActionBase;
import lando.systems.ld55.actions.MoveAction;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.GameTile;

public class RadialConfirmMoveButton extends RadialButton {

    GameBoard board;
    GameTile moveTile;
    GamePiece piece;

    public RadialConfirmMoveButton(GameBoard board, GamePiece piece, GameTile tile) {
        super(TileOverlayAssets.panelGreen, TileOverlayAssets.pawnRight, "Confirm", true);
        this.pointsUsed = 1;
        this.board = board;
        this.moveTile = tile;
        this.piece = piece;
        if (board.gameScreen.actionManager.playerActionsAvailable > 0){
            text = "Move\n1 Action";
            enabled= true;
        } else {
            text = "No Actions\nfor move";
            enabled = false;
        }
    }

    @Override
    public void onClick() {
        board.gameScreen.actionManager.playerActionsAvailable--;
        ActionBase move = new MoveAction(board, piece, moveTile);
        piece.currentAction = move;
        board.gameScreen.actionManager.addAction(move);
        Main.game.audioManager.playSound(AudioManager.Sounds.metalTap);
    }
}
