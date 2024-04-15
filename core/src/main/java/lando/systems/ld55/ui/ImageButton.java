package lando.systems.ld55.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.assets.Assets;

public class ImageButton {
    public NinePatch background;
    public NinePatch backgroundDefault;
    public Animation<TextureRegion> image;
    public Animation<TextureRegion> imageHovered;
    public Animation<TextureRegion> imagePressed;
    public Animation<TextureRegion> imageDisabled;
    public Animation<TextureRegion> imagePulse;
    public TextureRegion keyframe;
    public Rectangle bounds;
    public Circle boundsCircle;
    public Polygon boundsPolygon;
    public Color color;

    public Runnable onClick = null;
    public float stateTime = 0f;
    public float pulseAccum = 0f;
    public boolean hovered = false;
    public boolean pressed = false;
    public boolean disabled = false;
    public boolean wasPressed = false;
    public boolean active = false; // for 'checkbox' style
    public boolean pulse = false;

    public ImageButton(float x, float y, TextureRegion image, TextureRegion imageHovered, TextureRegion imagePressed, TextureRegion imageDisabled) {
        this(x, y, image.getRegionWidth(), image.getRegionHeight(), image, imageHovered, imagePressed, imageDisabled);
    }

    public ImageButton(float x, float y, float w, float h, TextureRegion image, TextureRegion imageHovered, TextureRegion imagePressed, TextureRegion imageDisabled) {
        this.bounds = new Rectangle(x, y, w, h);
        var radius = Math.min(w, h) / 2f;
        this.boundsCircle = new Circle(x + radius, y + radius, radius);
        this.image = new Animation<>(0.1f, Array.with(image), Animation.PlayMode.LOOP);
        if (imageHovered != null) {
            this.imageHovered = new Animation<>(0.1f, Array.with(imageHovered), Animation.PlayMode.LOOP);
        }
        if (imagePressed != null) {
            this.imagePressed = new Animation<>(0.1f, Array.with(imagePressed), Animation.PlayMode.LOOP);
        }
        if (imageDisabled != null) {
            this.imageDisabled = new Animation<>(0.1f, Array.with(imageDisabled), Animation.PlayMode.LOOP);
        }
        this.color = Color.WHITE.cpy();
        updateKeyFrame();
    }

    public void onClick() {
        if (onClick != null) {
            onClick.run();
        }
    }

    public void update(float dt, Vector3 touchPos, boolean isPressed, boolean isDisabled) {
        pressed = false;
        hovered = false;

        disabled = isDisabled;
        if (!disabled) {
            if (boundsPolygon != null && boundsPolygon.contains(touchPos.x, touchPos.y)) {
                pressed = isPressed;
                hovered = !pressed;
            } else if (boundsCircle.contains(touchPos.x, touchPos.y)) {
                pressed = isPressed;
                hovered = !pressed;
            }

            if (wasPressed && !pressed) {
                wasPressed = false;
                onClick();
            }
            wasPressed = pressed;
        }

        if (pulse) {
            var speed = 8f;
            pulseAccum += dt * speed;
        } else {
            pulseAccum = 0f;
        }

        stateTime += dt;
        updateKeyFrame();
    }

    public void render(SpriteBatch batch) {
        if (keyframe == null) return; // just to be safe, update probably gets called before render first frame

        batch.setColor(color);
        if (background != null) {
            background.draw(batch, bounds.x, bounds.y, bounds.width, bounds.height);
        }

        var margin = 8f;
        batch.draw(keyframe,
            bounds.x + margin,
            bounds.y + margin,
            bounds.width - 2f * margin,
            bounds.height - 2 * margin);

        if (pulse) {
            // not everything will have a pulsing image, so default to disabled so the behavior still typically works
            if (imagePulse == null) {
                imagePulse = imageDisabled;
            }
            var pulseKeyframe = imagePulse.getKeyFrame(stateTime);
            var alpha = (float) ((Math.sin(pulseAccum) + 1f) / 2f);
            batch.setColor(1, 1, 1, alpha);
            batch.draw(pulseKeyframe,
                bounds.x + margin,
                bounds.y + margin,
                bounds.width - 2 * margin,
                bounds.height - 2 * margin);
        }

        batch.setColor(1, 1, 1, 1);
    }

    private void updateKeyFrame() {
        color.set(Color.WHITE);
        keyframe = image.getKeyFrame(stateTime);
        if (backgroundDefault != null) {
            background = backgroundDefault;
        }

        if (disabled) {
            if (imageDisabled != null) {
                keyframe = imageDisabled.getKeyFrame(stateTime);
            } else {
                color.set(Color.DARK_GRAY);
                if (background != null) {
                    background = Assets.NinePatches.plain_dim;
                }
            }
        } else if (pressed || active) {
            color.set(Color.LIME);
            if (imagePressed != null) {
                keyframe = imagePressed.getKeyFrame(stateTime);
            }
            if (background != null) {
                background = Assets.NinePatches.glass_green;
            }
        } else if (hovered) {
            if (imageHovered != null) {
                keyframe = imageHovered.getKeyFrame(stateTime);
            } else {
                color.set(Color.SKY);
                if (background != null) {
                    background = Assets.NinePatches.glass_blue;
                }
            }
        }
    }
}
