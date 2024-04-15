package lando.systems.ld55.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import lando.systems.ld55.Main;
import lando.systems.ld55.audio.AudioManager;
import lando.systems.ld55.entities.GameBoard;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.GameTile;
import lando.systems.ld55.entities.Portal;

public class SpawnAction extends ActionBase {

    public static final float SummonTime = .5f;

    private GamePiece gamePiece;
    public GameTile spawnTile;
    private boolean spawned;
    private boolean started;
    private GameBoard board;
    private float accum;

    public SpawnAction(GameBoard board, GamePiece gamePiece, GameTile tile) {
        this.gamePiece = gamePiece;
        this.spawnTile = tile;
        spawned = false;
        started = false;
        this.board = board;

        board.portalAnimations.add(new Portal(spawnTile, gamePiece.owner == GamePiece.Owner.Player ? Color.BLUE : Color.RED));

        gamePiece.currentTile = tile;
    }

    @Override
    public boolean isCompleted() {
        return spawned;
    }

    @Override
    public boolean doneTurn() {
        return spawned;
    }

    @Override
    public void update(float dt) {
        accum += dt;
        if (!started) {
            started = true;
            startSpawn();
        }

        if (accum > SummonTime) {
            for (int i = board.portalAnimations.size -1; i >= 0; i--) {
                Portal p = board.portalAnimations.get(i);
                if (p.tile == spawnTile) {
                    board.portalAnimations.removeIndex(i);
                }
            }
            spawned = true;

            GamePiece tilePiece = board.getGamePiece(spawnTile);
            if (tilePiece != gamePiece) {
                // Spawn failed because something moved into it
            } else {
                gamePiece.summoning = false;
                board.gamePieces.add(gamePiece);
            }
        }
    }

    private void startSpawn() {
// moving here for now
//        Gdx.app.log("cucaracha counter", String.valueOf(board.cucarachaCounter));
//        if(board.cucarachaCounter >= 20) {
//            Main.game.audioManager.playSound(AudioManager.Sounds.cucaracha_fanfare);
//            board.cucarachaCounter = 0;
//        }
//        else {
//            if(gamePiece.owner == GamePiece.Owner.Player) {
//                Main.game.audioManager.playSound(AudioManager.Sounds.horn_fanfare);
//            }
//
//            else {
//                Main.game.audioManager.playSound(AudioManager.Sounds.enemy_spawn);
//            }
//            board.cucarachaCounter++;
//        }



        float x, y;
        if (gamePiece.owner == GamePiece.Owner.Enemy) {
            var bounds = board.spawnEvil.bounds;
            x = bounds.x + bounds.width / 2;
            gamePiece.startSpawn(x, 560, board.spawnEvil.anim.getAnimationDuration() + 0.5f);
            board.spawnEvil.activate();
            Main.game.audioManager.playSound(AudioManager.Sounds.spawn_evil_start);
        } else {
            gamePiece.startSpawn(44, 470, board.spawnGood.anim.getAnimationDuration() + 0.5f);
            board.spawnGood.activate();
            Main.game.audioManager.playSound(AudioManager.Sounds.spawn_good_start);
        }
    }

    @Override
    public GamePiece getPiece() {
        return gamePiece;
    }

    @Override
    public void reset() {
        // unsed for Spawn
    }

    @Override
    public void render(SpriteBatch batch) {
        float alpha = MathUtils.clamp((accum - 1f)/ (Portal.animationTime - 1f), 0f, 1f);
        batch.setColor(1f, 1f, 1f, alpha);
        gamePiece.render(batch);
        batch.setColor(Color.WHITE);
    }
}
