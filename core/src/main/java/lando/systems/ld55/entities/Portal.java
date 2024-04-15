package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld55.Main;

public class Portal {

    public static float animationTime = .5f;

    public Rectangle bounds;
    public float accum;
    private ShaderProgram shader;
    private Color color1 = new Color();
    private float animOffset;
    private float alpha;

    public Portal(Rectangle bounds, Color color1) {
        this.color1.set(color1);
        this.bounds = bounds;
        this.accum = 0;
        this.alpha = 0;
        this.animOffset = MathUtils.random(20f);
        this.shader = Main.game.assets.portalShader;
    }

    public void update(float dt) {
        accum += dt;
        if (accum < 1f) {
            alpha = accum;
        } else if (accum > animationTime - 1f) {
            alpha = animationTime - accum;
        }
    }

    public void render(SpriteBatch batch) {
        boolean isAlreadyDrawing = batch.isDrawing();
        if (isAlreadyDrawing) batch.end();
        batch.setShader(shader);
        batch.begin();
        batch.setColor(1f, 1f, 1f, alpha);
        shader.setUniformf("u_time", accum + animOffset);
        shader.setUniformf("u_color1", color1);
        shader.setUniformf("u_color2", Color.LIME);

        batch.draw(Main.game.assets.noiseTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.end();
        batch.setShader(null);
        if (isAlreadyDrawing) batch.begin();
    }

    public boolean isComplete() {
        return accum > animationTime;
    }
}
