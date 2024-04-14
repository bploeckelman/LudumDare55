package lando.systems.ld55.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class StylePiece {

    private final Animation<TextureRegion> animation;
    private final Vector2 position = new Vector2();
    private float animState = MathUtils.random(1f);

    public StylePiece(Animation<TextureRegion> animation, float x, float y) {
        this.animation = animation;
        this.position.set(x, y);
    }

    public void update(float dt) {
        animState += dt;
    }

    public void render(SpriteBatch batch) {
        var frame = animation.getKeyFrame(animState);
        batch.draw(frame, position.x - frame.getRegionWidth() / 2f, position.y);
    }
}
