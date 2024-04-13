package lando.systems.ld55.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.screens.GameScreen;

public class GameBoard {

    public static float topMargin = 80;
    public static float bottomMargin = 40;

    public GameTile hoverTile;
    public Rectangle boardRegion;
    public Array<GameTile> tiles = new Array<>();
    public Array<Portal> portalAnimations = new Array<>();

    public final int tilesWide;
    public final GameScreen gameScreen;
    public final Vector3 screenPosition = new Vector3();

    public GameBoard(GameScreen gameScreen, int tilesWide) {
        this.gameScreen = gameScreen;
        this.tilesWide = tilesWide;

        float boardsize = Config.Screen.window_height - (topMargin + bottomMargin);
        float tileSize = boardsize/tilesWide;
        float leftEdge = (Config.Screen.window_width - boardsize)/2f;
        Vector2 boardStartPoint = new Vector2(leftEdge, bottomMargin);
        boardRegion = new Rectangle(boardStartPoint.x, boardStartPoint.y, boardsize, boardsize);

        for (int y = 0; y < tilesWide; y++) {
            for (int x = 0; x < tilesWide; x++) {
                Rectangle rect = new Rectangle(boardStartPoint.x + (x * tileSize), boardStartPoint.y + (y * tileSize), tileSize, tileSize);
                tiles.add(new GameTile(x, y, rect));
            }
        }
    }

    public GameTile getTileAt(int x, int y) {
        if (x < 0 || x >= tilesWide
         || y < 0 || y >= tilesWide) {
            return null;
        }
        int i = x + y * tilesWide;
        return tiles.get(i);
    }

    public GameTile getTileRelative(GameTile tile, int offsetX, int offsetY) {
        return getTileAt(tile.x + offsetX, tile.y + offsetY);
    }

    public void update(float dt) {
        screenPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        hoverTile = getTileAtScreenPos(screenPosition);

        if (Gdx.input.justTouched() && hoverTile != null) {
            portalAnimations.add(new Portal(hoverTile.bounds, Color.BLUE));
        }

        for (int i = portalAnimations.size -1; i >= 0; i--) {
            Portal p = portalAnimations.get(i);
            p.update(dt);
            if (p.isComplete()) {
                portalAnimations.removeIndex(i);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (GameTile tile : tiles) {
            tile.render(batch);
        }
        for (Portal p : portalAnimations) {
            p.render(batch);
        }

        // draw hover overlays (work in progress)
        if (hoverTile != null){
            var texture = gameScreen.assets.whitePixel;
            var color = Color.LIME;
            var alpha = 0.4f;

            var bounds = hoverTile.bounds;
            batch.setColor(color.r, color.g, color.b, alpha);
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);

            // find neighbor tiles (without pattern)
            // TODO - find neighbor tiles (with pattern)
            var left  = getTileRelative(hoverTile, -1,  0);
            var right = getTileRelative(hoverTile, +1,  0);
            var up    = getTileRelative(hoverTile,  0, +1);
            var down  = getTileRelative(hoverTile,  0, -1);

            color = Color.RED;
            batch.setColor(color.r, color.g, color.b, alpha);
            if (left != null) {
                bounds = left.bounds;
                batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
            }
            if (right != null) {
                bounds = right.bounds;
                batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
            }
            if (up != null) {
                bounds = up.bounds;
                batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
            }
            if (down != null) {
                bounds = down.bounds;
                batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
        batch.setColor(Color.WHITE);
    }

    public GameTile getTileAtScreenPos(Vector3 screenPos) {
        gameScreen.worldCamera.unproject(screenPos);
        if (boardRegion.contains(screenPos.x, screenPos.y)){
            // could maybe look this up by index later
            for (GameTile tile : tiles) {
                if (tile.bounds.contains(screenPos.x, screenPos.y)) {
                    return tile;
                }
            }
        }
        return null;
    }
}
