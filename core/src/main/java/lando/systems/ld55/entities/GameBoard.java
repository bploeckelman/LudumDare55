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
    public Rectangle boardRegion;
    public Array<GameTile> tiles = new Array<>();
    public Array<Portal> portalAnimations = new Array<>();

    private GameTile hoverTile;
    private GameScreen gameScreen;
    private Vector3 screenPosition = new Vector3();

    public GameBoard(GameScreen gameScreen, int tileWide) {
        this.gameScreen = gameScreen;
        float boardsize = Config.Screen.window_height - ( topMargin + bottomMargin);
        float tileSize = boardsize/tileWide;
        float leftEdge = (Config.Screen.window_width - boardsize)/2f;
        Vector2 boardStartPoint = new Vector2(leftEdge, bottomMargin);
        boardRegion = new Rectangle(boardStartPoint.x, boardStartPoint.y, boardsize, boardsize);

        for (int y = 0; y < tileWide; y++) {
            for (int x = 0; x < tileWide; x++) {
                Rectangle rect = new Rectangle(boardStartPoint.x + (x * tileSize), boardStartPoint.y + (y * tileSize), tileSize, tileSize);
                tiles.add(new GameTile(x, y, rect));
            }
        }

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
        if (hoverTile != null){
            batch.setColor(1.0f, 0f, 0f, .4f);
            batch.draw(Main.game.assets.whitePixel, hoverTile.bounds.x, hoverTile.bounds.y, hoverTile.bounds.width, hoverTile.bounds.height);
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
