package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.Config;

public class GameBoard {

    public static float topBottomMargin = 60;
    public Rectangle boardRegion;
    public Array<GameTile> tiles = new Array<>();

    public GameBoard(int tileWide) {
        float boardsize = Config.Screen.window_height - (2 * topBottomMargin);
        float tileSize = boardsize/tileWide;
        float leftEdge = (Config.Screen.window_width - boardsize)/2f;
        Vector2 boardStartPoint = new Vector2(leftEdge, topBottomMargin);

        for (int y = 0; y < tileWide; y++) {
            for (int x = 0; x < tileWide; x++) {
                Rectangle rect = new Rectangle(boardStartPoint.x + (x * tileSize), boardStartPoint.y + (y * tileSize), tileSize, tileSize);
                tiles.add(new GameTile(x, y, rect));
            }
        }

    }

    public void update(float dt) {

    }

    public void render(SpriteBatch batch) {
        for (GameTile tile : tiles) {
            tile.render(batch);
        }
    }
}
