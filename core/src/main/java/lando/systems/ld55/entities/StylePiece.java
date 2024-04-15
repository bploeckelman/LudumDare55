package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class StylePiece {

    private final Animation<TextureRegion> animation;
    private final Vector2 position = new Vector2();
    private float animState = 0;
    private boolean flipped;

    public StylePiece(Animation<TextureRegion> animation, float x, float y, float animState, boolean flipped) {
        this.animation = animation;
        this.position.set(x, y);

        if (animState == -1) {
            animState = MathUtils.random(1f);
        }
        this.animState = animState;
        this.flipped = flipped;
    }

    public void update(float dt) {
        animState += dt;
    }

    public void render(SpriteBatch batch) {
        var frame = animation.getKeyFrame(animState);
        float scaleX = flipped ? 1f : -1f;
        float width = frame.getRegionWidth();
        float height = frame.getRegionHeight();
        batch.draw(frame, position.x - width / 2f, position.y, width / 2f, height / 2f, width, height, scaleX, 1f, 0);
    }
}
