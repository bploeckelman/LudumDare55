package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lando.systems.ld55.Main;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.actions.SpawnAction;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.GameTile;

public class RadialSummonButton extends RadialButton {
    GamePiece.Type pieceType;
    GameBoard board;
    GameTile summonTile;

    public RadialSummonButton(GameBoard board, GameTile tile, GamePiece.Type type) {
        super(TileOverlayAssets.panelWhite, TileOverlayAssets.pawnPlus, "DEBUG", false);
        this.summonTile = tile;
        this.board = board;
        this.pieceType = type;
        int actionsAvailable = board.gameScreen.actionManager.playerActionsAvailable;
        switch (type) {
            case Pawn:
                if (actionsAvailable >= type.actionsToSpawn) {
                    pointsUsed = type.actionsToSpawn;
                    enabled = true;
//                    this.text = "Pawn\n1 Action";
                    this.text = "Summon\nPawn\n(1 move)";
                } else {
                    this.text = "Not\nenough\nactions";
                }
                break;
            case Knight:
                if (actionsAvailable >= type.actionsToSpawn) {
                    pointsUsed = type.actionsToSpawn;
                    enabled = true;
//                    this.text = "Knight\n1 Action";
                    this.text = "Summon\nKnight\n(1 move)";
                } else {
//                    this.text = "Knight\nNot enough\nactions";
                    this.text = "Not\nenough\nactions";
                }
                break;
            case Bishop:
                if (actionsAvailable >= type.actionsToSpawn) {
                    pointsUsed = type.actionsToSpawn;
                    enabled = true;
                    this.text = "Summon\nBishop\n(2 moves)";
                } else {
//                    this.text = "Bishop\nNot enough\nactions";
                    this.text = "Not\nenough\nactions";
                }
                break;
            case Rook:
                if (actionsAvailable >= type.actionsToSpawn) {
                    pointsUsed = type.actionsToSpawn;
                    enabled = true;
                    this.text = "Summon\nRook\n(2 moves)";
                } else {
//                    this.text = "Rook\nNot enough\nactions";
                    this.text = "Not\nenough\nactions";
                }
                break;
            case Queen:
                if (actionsAvailable >= type.actionsToSpawn) {
                    pointsUsed = type.actionsToSpawn;
                    enabled = true;
                    this.text = "Summon\nQueen\n(3 moves)";
                } else {
//                    this.text = "Queen\nNot enough\nactions";
                    this.text = "Not\nenough\nactions";
                }
                break;
        }

    }

    @Override
    public void onClick() {
        ActionManager actionManager = board.gameScreen.actionManager;
        var assets = board.gameScreen.assets;
        var owner = GamePiece.Owner.Player;
        actionManager.playerActionsAvailable -= pointsUsed;
        Main.game.audioManager.playSound(AudioManager.Sounds.select);
        switch (pieceType) {
            case Pawn:
                board.gameScreen.actionManager.addAction(new SpawnAction(board,
                    GamePiece.getGamePiece(assets, GamePiece.Type.Pawn, owner), summonTile));
                break;
            case Knight:
                board.gameScreen.actionManager.addAction(new SpawnAction(board,
                    GamePiece.getGamePiece(assets, GamePiece.Type.Knight, owner), summonTile));
                break;
            case Bishop:
                board.gameScreen.actionManager.addAction(new SpawnAction(board,
                    GamePiece.getGamePiece(assets, GamePiece.Type.Bishop, owner), summonTile));
                break;
            case Rook:
                board.gameScreen.actionManager.addAction(new SpawnAction(board,
                    GamePiece.getGamePiece(assets, GamePiece.Type.Rook, owner), summonTile));
                break;
            case Queen:
                board.gameScreen.actionManager.addAction(new SpawnAction(board,
                    GamePiece.getGamePiece(assets, GamePiece.Type.Queen, owner), summonTile));
                break;
        }
    }
}
