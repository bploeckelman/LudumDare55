package lando.systems.ld55.actions;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lando.systems.ld55.entities.GamePiece;
import lando.systems.ld55.entities.GameTile;

public class SpawnAction extends ActionBase {

    private GamePiece gamePiece;
    private GameTile spawnTile;
    private boolean spawned;

    public SpawnAction(GamePiece gamePiece, GameTile tile) {
        this.gamePiece = gamePiece;
        this.spawnTile = tile;
        spawned = false;
    }

    @Override
    public boolean isCompleted() {
        return spawned;
    }

    @Override
    public boolean doneTurn() {
        return false;
    }

    @Override
    public void update(float dt) {
        // TODO make the stuff that will spawn the creature and then set spawn to true
    }

    @Override
    public GamePiece getPiece() {
        return gamePiece;
    }

    @Override
    public void reset() {

    }

    @Override
    public void render(SpriteBatch batch) {

    }
}
