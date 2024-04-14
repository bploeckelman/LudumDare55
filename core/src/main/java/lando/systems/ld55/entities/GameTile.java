package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld55.Main;

public class GameTile {
    public int x;
    public int y;
    public boolean valid;
    public Rectangle bounds;
    public Vector2 center;

    public GameTile(int x, int y, Rectangle rect) {
        this.x = x;
        this.y = y;
        this.valid = true;
        this.bounds = rect;
        this.center = new Vector2();
        this.bounds.getCenter(center);
    }

    public void update(float dt) {

    }

    public void render(SpriteBatch batch) {
        if (!valid) return;

        // Temp color stuff
        Color c = Color.DARK_GRAY;
        if ((x + y) % 2 == 0) {
            c = Color.LIGHT_GRAY;
        }
        batch.setColor(c.r, c.g, c.b, 0.5f);
        batch.draw(Main.game.assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
        batch.setColor(Color.WHITE);
    }
}
