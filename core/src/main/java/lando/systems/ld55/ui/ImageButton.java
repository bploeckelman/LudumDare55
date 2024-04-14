package lando.systems.ld55.ui;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ImageButton {
    public Animation<TextureRegion> image;
    public Animation<TextureRegion> imageHovered;
    public Animation<TextureRegion> imagePressed;
    public Animation<TextureRegion> imageDisabled;
    public TextureRegion keyframe;
    public Rectangle bounds;
    public Circle boundsCircle;

    public Runnable onClick = null;
    public float stateTime = 0f;
    public boolean hovered = false;
    public boolean pressed = false;
    public boolean disabled = false;
    public boolean wasPressed = false;

    public ImageButton(float x, float y, TextureRegion image, TextureRegion imageHovered, TextureRegion imagePressed, TextureRegion imageDisabled) {
        this(x, y, image.getRegionWidth(), image.getRegionHeight(), image, imageHovered, imagePressed, imageDisabled);
    }

    public ImageButton(float x, float y, float w, float h, TextureRegion image, TextureRegion imageHovered, TextureRegion imagePressed, TextureRegion imageDisabled) {
        this.bounds = new Rectangle(x, y, w, h);
        var radius = w / 2f;
        this.boundsCircle = new Circle(x + radius, y + radius, radius);
        this.image = new Animation<>(0.1f, Array.with(image), Animation.PlayMode.LOOP);
        this.imageHovered = new Animation<>(0.1f, Array.with(imageHovered), Animation.PlayMode.LOOP);
        this.imagePressed = new Animation<>(0.1f, Array.with(imagePressed), Animation.PlayMode.LOOP);
        this.imageDisabled = new Animation<>(0.1f, Array.with(imageDisabled), Animation.PlayMode.LOOP);
        updateKeyFrame();
    }

    public void onClick() {
        if (onClick != null) {
            onClick.run();
        }
    }

    public void update(float dt, Vector3 touchPos, boolean isPressed, boolean isDisabled) {
        disabled = isDisabled;
        if (!disabled) {
            if (boundsCircle.contains(touchPos.x, touchPos.y)) {
                pressed = isPressed;
                hovered = !pressed;
            }

            if (wasPressed && !pressed) {
                wasPressed = false;
                onClick();
            }
            wasPressed = pressed;
        }

        stateTime += dt;
        updateKeyFrame();
    }

    public void render(SpriteBatch batch) {
        if (keyframe == null) return; // just to be safe, update probably gets called before render first frame
        batch.draw(keyframe, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    private void updateKeyFrame() {
        keyframe = disabled ? imageDisabled.getKeyFrame(stateTime)
            : hovered ? imageHovered.getKeyFrame(stateTime)
            : pressed ? imagePressed.getKeyFrame(stateTime)
            : image.getKeyFrame(stateTime);
    }
}
