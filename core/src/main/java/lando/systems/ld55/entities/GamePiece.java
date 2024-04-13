package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class GamePiece {

    private final int TILE_OFFSET_Y = 10;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> attack;

    private float animState = 0;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion keyframe;
    private GameTile currentTile;
    private GameTile moveTile;
    private final Rectangle bounds = new Rectangle();
    private final Vector2 position = new Vector2();

    public GamePiece(Animation<TextureRegion> idle, Animation<TextureRegion> attack) {
        this.idle = idle;
        this.attack = attack;
        setCurrentAnimation(this.idle);
    }

    private void setCurrentAnimation(Animation<TextureRegion> animation) {
        currentAnimation = animation;
        animState = 0;
        keyframe = this.currentAnimation.getKeyFrame(animState);
        bounds.setWidth(keyframe.getRegionWidth());
        bounds.setHeight(keyframe.getRegionHeight());
    }

    public void setTile(GameTile tile) {
        currentTile = tile;
        setPosition(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + TILE_OFFSET_Y);
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x - bounds.width / 2, y);
    }

    public void update(float dt) {
        animState += dt;
        keyframe = currentAnimation.getKeyFrame(animState);
    }

    public void render(SpriteBatch batch) {
        if (currentTile == null) return;

        batch.draw(keyframe, bounds.x, bounds.y);
    }
}