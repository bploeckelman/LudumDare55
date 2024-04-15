package lando.systems.ld55.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
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
            gamePiece.setTile(spawnTile);
            started = true;
            // TODO: Play portal sound


            // TODO: particles
            board.gameScreen.particles.portal(spawnTile.bounds.x + spawnTile.bounds.width / 2f, spawnTile.bounds.y + spawnTile.bounds.height / 2f, spawnTile.bounds.width / 2f);
        }

        if (accum > SummonTime) {
            spawned = true;
            gamePiece.summoning = false;
            board.gamePieces.add(gamePiece);
            for (int i = board.portalAnimations.size -1; i >= 0; i--) {
                Portal p = board.portalAnimations.get(i);
                if (p.tile == spawnTile) {
                    board.portalAnimations.removeIndex(i);
                }
            }
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
