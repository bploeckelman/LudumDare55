package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lando.systems.ld55.assets.Assets;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.ui.MovementBreadcrumb;

public class GameTile {
    public int x;
    public int y;
    public boolean valid;
    public Rectangle bounds;
    public Vector2 center;
    public boolean summonable;

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

        // Temp color stuff, should be handled by the TileOverlayInfo items
//        Color c = Color.DARK_GRAY;
//        if ((x + y) % 2 == 0) {
//            c = Color.LIGHT_GRAY;
//        }
//        batch.setColor(c.r, c.g, c.b, 0.1f);
//        batch.draw(Main.game.assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
//        batch.setColor(Color.WHITE);
    }

    public void renderFrameBuffer(SpriteBatch batch) {
        if (!valid) return;
        batch.setColor(Color.WHITE);
        Assets.NinePatches.outline.draw(batch, bounds.x-2, bounds.y-2, bounds.width+2, bounds.height+2);
    }

    public void renderEndTurnBuffer(SpriteBatch batch) {
        if (!valid) return;
        batch.setColor(Color.WHITE);
        Assets.NinePatches.outline.draw(batch, bounds.x-2, bounds.y-2, bounds.width+2, bounds.height+2);
        TextureRegion arrowTexture = TileOverlayAssets.getArrowForDir(MovementBreadcrumb.Direction.Right);
        batch.draw(arrowTexture, bounds.x, bounds.y, bounds.width, bounds.height );
    }
}
