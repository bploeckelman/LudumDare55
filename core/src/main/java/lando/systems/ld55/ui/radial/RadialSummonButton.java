package lando.systems.ld55.ui.radial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
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
    public GamePiece.Type pieceType;
    GameBoard board;
    GameTile summonTile;

    public RadialSummonButton(GameBoard board, GameTile tile, GamePiece.Type type) {
        super(TileOverlayAssets.panelWhite, TileOverlayAssets.pawnPlus, "", false);
        this.summonTile = tile;
        this.board = board;
        this.pieceType = type;
        int actionsAvailable = board.gameScreen.actionManager.playerActionsAvailable;
        if (actionsAvailable >= type.actionsToSpawn) {
            pointsUsed = type.actionsToSpawn;
            enabled = true;
        }
        Animation<TextureRegion> portrait = board.gameScreen.assets.getPortrait(type, GamePiece.Owner.Player);
        this.icon = portrait.getKeyFrame(0);
        iconEnabledColor.set(Color.WHITE);
        iconDisabledColor.set(.8f, .8f, .8f, 1f);
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
