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

    public static final float SummonTime = 1f;

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

    // start higher to get cucaracha 1st sooner
    private int spawnCount = 4;
    private void startSpawn() {
        if (gamePiece.owner == GamePiece.Owner.Enemy) {
            var bounds = board.spawnEvil.bounds;
            float x = bounds.x + bounds.width / 2;
            gamePiece.startSpawn(x, 560, board.spawnEvil.anim.getAnimationDuration() - 0.25f);
            board.spawnEvil.activate();
            Main.game.audioManager.playSound(AudioManager.Sounds.spawn_evil_start, .75f);
        } else {
            gamePiece.startSpawn(44, 470, board.spawnGood.anim.getAnimationDuration());
            board.spawnGood.activate();
            var goodSpawnSound = (++spawnCount % 10 == 0) ? AudioManager.Sounds.cucaracha_fanfare : AudioManager.Sounds.spawn_good_start;
            Main.game.audioManager.playSound(goodSpawnSound);
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

    private void next() {

    }
}
