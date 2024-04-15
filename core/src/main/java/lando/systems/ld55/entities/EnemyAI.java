package lando.systems.ld55.entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.Main;
import lando.systems.ld55.actions.ActionBase;
import lando.systems.ld55.actions.MoveAction;
import lando.systems.ld55.actions.SpawnAction;

public class EnemyAI {

    public static Array<GamePiece> playerPieces = new Array<>();
    public static Array<GamePiece> enemyPieces = new Array<>();
    public static void doTurn(GameBoard board) {
        int actions = 3;
        // Make game decisions and add to the action queue

        // This is dumb rally dumb
        playerPieces.clear();
        enemyPieces.clear();
        for (GamePiece p : board.gamePieces) {
            if (p.owner == GamePiece.Owner.Player){
                playerPieces.add(p);
            } else {
                enemyPieces.add(p);
            }
        }

        int enemyCount = enemyPieces.size;
        int playerCount = playerPieces.size;
        int failsafe = 0;
        // Spawn things
        while (actions > 0 && enemyCount <= playerCount && failsafe < 100)
        {
            failsafe++;
            GameTile summonTile = tryToFindSpawnTile(board);
            if (summonTile != null) {
                GamePiece.Type type = GamePiece.Type.random();
                if (type.actionsToSpawn <= actions) {
                    board.gameScreen.actionManager.addAction(new SpawnAction(board,
                        GamePiece.getGamePiece(Main.game.assets, type, GamePiece.Owner.Enemy), summonTile));
                    actions -= type.actionsToSpawn;
                    enemyCount++;
                }
            }
        }

        for (int i = 0; i < actions; i++) {
            // Try to move around randomly
            if (enemyPieces.size > 0) {
                GamePiece movePiece = enemyPieces.random();
                if (movePiece.currentAction instanceof MoveAction) {
                    if (MathUtils.randomBoolean(.2f)) {
                        ActionBase a = movePiece.currentAction;
                        board.gameScreen.actionManager.removeAction(a);
                        movePiece.currentAction = null;
                    } else {
                        continue;
                    }
                }
                movePiece.moveTiles.clear();
                movePiece.addMoveTiles(board);
                if (movePiece.moveTiles.size > 0) {
                    GameTile moveTile = movePiece.moveTiles.random();
                    board.gameScreen.actionManager.addAction(new MoveAction(board, movePiece, moveTile));
                }
            }
        }

    }

    public static GameTile tryToFindSpawnTile(GameBoard board) {
        for (int i = 0; i < 5; i++) {
            int row = MathUtils.random(board.tilesHigh-1);
            for (int x = board.tilesWide-1; x > 0; x --) {
                GameTile tile = board.getTileAt(x, row);
                if (tile != null) {
                    GamePiece p = board.getGamePiece(tile);
                    if (p == null) {
                        return tile;
                    }
                    break;
                }
            }
        }

        return null;
    }
}
