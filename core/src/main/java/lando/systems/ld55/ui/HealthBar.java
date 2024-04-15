package lando.systems.ld55.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld55.assets.Assets;

public class HealthBar {
    private final static float BASE_BOX_WIDTH = 20;
    private final static float BASE_BOX_HEIGHT = 6;
    private final static float BAR_BORDER_SIZE = 1;
    private final static float BOX_BORDER_SIZE = 1;

    private float boxWidth = BASE_BOX_WIDTH;
    private float boxHeight = BASE_BOX_HEIGHT;

    private final Texture boxBorder;
    private final Texture boxFill;

    public Rectangle barBounds;
    public int maxHealth;
    public int currentHealth;
    public float barWidth;

    public HealthBar(Assets assets, float x, float y, int maxHealth) {
        this.boxBorder = assets.pixel;
        this.boxFill = assets.pixel;
        updatePosition(x, y);
        boxWidth = BASE_BOX_WIDTH / maxHealth + BASE_BOX_WIDTH * .2f;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.barWidth = boxWidth * maxHealth + BAR_BORDER_SIZE * 2 - BOX_BORDER_SIZE * (maxHealth - 1);
    }

    public void updatePosition(float x, float y) {
        barWidth = boxWidth * maxHealth + BAR_BORDER_SIZE * 2 - BOX_BORDER_SIZE * (maxHealth - 1);
        barBounds = new Rectangle(x - BAR_BORDER_SIZE - barWidth / 2f, y - BAR_BORDER_SIZE, barWidth, BASE_BOX_HEIGHT + BAR_BORDER_SIZE * 2);
        //barBounds.setPosition(x - BAR_BORDER_SIZE, y - BAR_BORDER_SIZE);
    }

    public void updateCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public void updateMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        boxWidth = BASE_BOX_WIDTH / maxHealth;
        updatePosition(barBounds.x, barBounds.y);
    }

    public void render(SpriteBatch batch, float alpha) {
        float borderX = barBounds.x;
        float borderY = barBounds.y;
        float borderWidth = barBounds.width;
        float borderHeight = barBounds.height;
        batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, alpha);
        batch.draw(boxBorder, borderX, borderY, borderWidth, borderHeight);

        // Draw health boxes
        for (int i = 0; i < maxHealth; i++) {
            float boxX = barBounds.x + BOX_BORDER_SIZE + i * (boxWidth - BOX_BORDER_SIZE);
            boolean isFilled = i < currentHealth;
            drawBox(batch, boxX, barBounds.y + BOX_BORDER_SIZE, isFilled, alpha);
        }
        batch.setColor(Color.WHITE);
    }

    private void drawBox(SpriteBatch batch, float x, float y, boolean filled, float alpha) {
        // Draw black border
        batch.setColor(0, 0, 0, alpha);
        batch.draw(boxBorder, x, y, boxWidth, boxHeight);

        if (filled) {
            if (currentHealth <= maxHealth / 3f || (currentHealth <= 1 && maxHealth > 1)) {
                batch.setColor(0.8f, 0.1f, 0.1f, alpha);  // Red color
            } else if (currentHealth < maxHealth * 2f / 3f) {
                batch.setColor(0.8f, 0.8f, 0.1f, alpha);  // Yellow color
            } else {
                batch.setColor(0.1f, 0.8f, 0.1f, alpha);  // Green color
            }
            batch.draw(boxFill, x + BOX_BORDER_SIZE, y + BOX_BORDER_SIZE, boxWidth - 2 * BOX_BORDER_SIZE, boxHeight - 2 * BOX_BORDER_SIZE);
            batch.setColor(1f, 1f, 1f, 1f);
        }
    }
}
