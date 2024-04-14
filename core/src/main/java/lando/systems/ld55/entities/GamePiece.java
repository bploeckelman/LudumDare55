package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld55.actions.ActionBase;

public class GamePiece {

    private final int TILE_OFFSET_Y = 10;

    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> attack;

    private float animState = 0;
    private float selectedAnimState = 0;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion keyframe;

    private final Rectangle bounds = new Rectangle();
    private final Vector2 position = new Vector2();
    private boolean selected = false;

    public GameTile currentTile;

    private GameTile moveTile;
    private final Vector2 startPosition = new Vector2();
    private final Vector2 movePosition = new Vector2();
    public boolean isMoving = false;
    private float moveAnimState = 0;
    private float moveSeconds = 1; // seconds

    public ActionBase currentAction;

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

    public void selectTile(GameTile tile) {
//        if (isMoving) return;
//
//        if (currentTile == null) {
//            setTile(tile);
//        } else if (currentTile == tile) {
//            selected = !selected;
//            selectedAnimState = 0;
//        } else if (selected) {
//            moveToTile(tile);
//        } else {
//            setTile(tile);
//        }
    }

    public void setTile(GameTile tile) {
        currentTile = tile;
        setPosition(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + TILE_OFFSET_Y);
        selected = false;
        isMoving = false;
    }

    private void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x - bounds.width / 2, y);
    }

    public void moveToTile(GameTile tile) {
        moveTile = tile;
        startPosition.set(position);
        movePosition.set(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + TILE_OFFSET_Y);
        moveAnimState = 0;

        selected = false;
        isMoving = true;
    }

    public void update(float dt) {
        animState += dt;
        keyframe = currentAnimation.getKeyFrame(animState);
        if (selected) {
            selectedAnimState += dt;
        }
        if (isMoving) {
            updateMovement(dt);
        }
        if (currentAction != null && currentAction.isCompleted()){
            currentAction = null;
        }
    }

    private void updateMovement(float dt) {
        moveAnimState += dt;
        if (moveAnimState > moveSeconds) {
            moveAnimState = moveSeconds;
            setTile(moveTile);
        }
        position.set(startPosition);
        position.interpolate(movePosition, moveAnimState / moveSeconds, Interpolation.linear);
        setPosition(position.x, position.y);
    }

    public void render(SpriteBatch batch) {
        if (currentTile == null) return;

        float yOffset = getYOffset();
        batch.draw(keyframe, bounds.x, bounds.y + yOffset, bounds.width / 2, bounds.height / 2, bounds.width, bounds.height, 1, 1, 0);
    }

    private float getYOffset() {
        float anim = 0;
        float yAdjust = 1;
        if (selected) {
            anim = selectedAnimState * 4;
            yAdjust = TILE_OFFSET_Y / 2f;
        } else if (isMoving) {
            anim = moveAnimState / moveSeconds * MathUtils.PI;
            yAdjust = 30;
        }
        return yAdjust * MathUtils.sin(anim);
    }
}
