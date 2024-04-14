package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld55.assets.Assets;

public class Spawn {

    public GamePiece.Owner owner;
    public Rectangle bounds;
    public Animation<TextureRegion> idle;
    public Animation<TextureRegion> active;
    public Animation<TextureRegion> anim;
    public TextureRegion keyframe;
    public float stateTime;

    public Spawn(Assets assets, GamePiece.Owner owner, float x, float y) {
        this.owner = owner;

        if (owner == GamePiece.Owner.Player) {
            idle = assets.spawnGoodIdle;
            active = assets.spawnGoodActive;
        } else {
            idle = assets.spawnEvilIdle;
            active = assets.spawnEvilActive;
        }
        anim = idle;
        stateTime = 0f;
        keyframe = anim.getKeyFrame(stateTime);

        bounds = new Rectangle(
            x - keyframe.getRegionWidth() / 2f,
            y - keyframe.getRegionHeight() / 2f,
            keyframe.getRegionWidth(),
            keyframe.getRegionHeight());
    }

    public void activate() {
        stateTime = 0;
        anim = active;
    }

    public void update(float dt) {
        if (anim == active && anim.isAnimationFinished(stateTime)) {
            stateTime = 0;
            anim = idle;
        } else{
            stateTime += dt;
        }
        keyframe = anim.getKeyFrame(stateTime);
    }

    public void render(SpriteBatch batch) {
        batch.draw(keyframe, bounds.x, bounds.y, bounds.width, bounds.height);
    }
}
