package lando.systems.ld55.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import lando.systems.ld55.assets.Assets;

public class HealthBar {
    private final static float BASE_BOX_WIDTH = 30;
    private final static float BASE_BOX_HEIGHT = 10;
    private final static float BAR_BORDER_SIZE = 2;
    private final static float BOX_BORDER_SIZE = 2;

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

    public void render(SpriteBatch batch) {
        float borderX = barBounds.x;
        float borderY = barBounds.y;
        float borderWidth = barBounds.width;
        float borderHeight = barBounds.height;
        batch.setColor(Color.WHITE);
        batch.draw(boxBorder, borderX, borderY, borderWidth, borderHeight);

        // Draw health boxes
        for (int i = 0; i < maxHealth; i++) {
            float boxX = barBounds.x + BOX_BORDER_SIZE + i * (boxWidth - BOX_BORDER_SIZE);
            boolean isFilled = i < currentHealth;
            drawBox(batch, boxX, barBounds.y + BOX_BORDER_SIZE, isFilled);
        }
        batch.setColor(Color.WHITE);
    }

    private void drawBox(SpriteBatch batch, float x, float y, boolean filled) {
        // Draw black border
        batch.setColor(Color.BLACK);
        batch.draw(boxBorder, x, y, boxWidth, boxHeight);

        if (filled) {
            if (currentHealth <= maxHealth / 3f || currentHealth <= 1) {
                batch.setColor(0.8f, 0.1f, 0.1f, 1f);  // Red color
            } else if (currentHealth < maxHealth * 2f / 3f) {
                batch.setColor(0.8f, 0.8f, 0.1f, 1f);  // Yellow color
            } else {
                batch.setColor(0.1f, 0.8f, 0.1f, 1f);  // Green color
            }
            batch.draw(boxFill, x + BOX_BORDER_SIZE, y + BOX_BORDER_SIZE, boxWidth - 2 * BOX_BORDER_SIZE, boxHeight - 2 * BOX_BORDER_SIZE);
            batch.setColor(1f, 1f, 1f, 1f);
        }
    }
}
