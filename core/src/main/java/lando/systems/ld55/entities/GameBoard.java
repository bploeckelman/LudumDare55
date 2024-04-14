package lando.systems.ld55.entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends InputAdapter {

    public static float marginLeft = 112;
    public static float marginRight = 112;
    public static float marginTop = 120;
    public static float marginBottom = 120;

    public GameTile hoverTile;
    public Rectangle boardRegion;
    public Array<GameTile> tiles = new Array<>();
    public Array<Portal> portalAnimations = new Array<>();
    public Array<GamePiece> gamePieces = new Array<>();

    public final int tilesWide;
    public final int tilesHigh;
    public final GameScreen gameScreen;
    public final Vector3 screenPosition = new Vector3();

    // TEMP -------------
    public Pattern currentPattern = Pattern.QUEEN_ATK;
    // TEMP -------------

    public GameBoard(GameScreen gameScreen, int tilesWide, int tilesHigh) {
        this.gameScreen = gameScreen;
        this.tilesWide = tilesWide;
        this.tilesHigh = tilesHigh;

        var cornerDepth = 3; // 'cutout' 3 tiles deep in each corner on both axes
        var boardWidth = gameScreen.worldCamera.viewportWidth - (marginLeft + marginRight);
        var boardHeight = gameScreen.worldCamera.viewportHeight - (marginTop + marginBottom);
        // these should be the same...
        var tileSize = boardWidth / tilesWide;
//        var tileSize = boardHeight / tilesHigh;
        var boardStartPoint = new Vector2(marginLeft, marginBottom);
        boardRegion = new Rectangle(boardStartPoint.x, boardStartPoint.y, boardWidth, boardHeight);

        for (int y = 0; y < tilesHigh; y++) {
            for (int x = 0; x < tilesWide; x++) {
                var rect = new Rectangle(
                    boardStartPoint.x + (x * tileSize),
                    boardStartPoint.y + (y * tileSize),
                    tileSize, tileSize);
                var tile = new GameTile(x, y, rect);
                tile.valid = !isCornerTile(x, y, cornerDepth);
                tiles.add(tile);
            }
        }
    }

    private boolean isCornerTile(int x, int y, int cornerDepth) {
        return (x + y < cornerDepth)                   // bottom left
            || (x + (tilesHigh - y - 1) < cornerDepth) // bottom right
            || ((tilesWide - x - 1) + y < cornerDepth) // top left
            || ((tilesWide - x - 1) + (tilesHigh - y - 1) < cornerDepth); // top right
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (hoverTile != null) {
            GamePiece gamePiece = getGamePiece(hoverTile);
            if (gamePiece == null) {
                addGamePiece(new GamePiece(gameScreen.assets.cherry, gameScreen.assets.cherry), hoverTile);
                portalAnimations.add(new Portal(hoverTile.bounds, Color.BLUE));
                gameScreen.particles.portal(hoverTile.bounds.x + hoverTile.bounds.width / 2f, hoverTile.bounds.y + hoverTile.bounds.height / 2f, hoverTile.bounds.width / 2f);
            } else {
                gamePiece.toggleSelect();
            }
            return true;
        }
        return false;
    }

    private GamePiece getGamePiece(GameTile tile) {
        for (GamePiece gp : gamePieces) {
            if (gp.currentTile == tile) {
                return gp;
            }
        }
        return null;
    }

    public void addGamePiece(GamePiece gamePiece, GameTile tile) {
       gamePiece.setTile(tile);
       gamePieces.add(gamePiece);
    }

    public GameTile getTileAt(int x, int y) {
        if (x < 0 || x >= tilesWide
         || y < 0 || y >= tilesHigh) {
            return null;
        }
        int i = x + y * tilesWide;
        var tile = tiles.get(i);
        return (tile.valid) ? tile : null;
    }

    public GameTile getTileRelative(GameTile tile, int offsetX, int offsetY) {
        return getTileAt(tile.x + offsetX, tile.y + offsetY);
    }

    public List<TileOverlay> getTileOverlaysForPattern(GameTile center, Pattern pattern) {
        var overlays = new ArrayList<TileOverlay>();

        var centerIndex = Pattern.size / 2;
        for (int y = 0; y < Pattern.size; y++) {
            for (int x = 0; x < Pattern.size; x++) {
                char ch = pattern.vals[y][x];
                if (ch == ' ' || ch == 'x') continue;

                // get the damage amount based on the digit for this tile in the pattern
                int damage = 1;
                if (Character.isDigit(ch)) {
                    damage = Character.digit(ch, 10);
                }

                // NOTE - game board y coord is inverted relative to pattern array coords
                int invY = Pattern.size - 1 - y;
                // TODO - if this is for an 'enemy' rather than the 'player', invert X also

                int offsetX = x - centerIndex;
                int offsetY = invY - centerIndex;

                var tile = getTileRelative(center, offsetX, offsetY);
                if (tile != null) {
                    var margin = 15f;
                    var anim = gameScreen.assets.numbers.get(damage);
                    var overlay = TileOverlay.builder()
                        .tile(tile)
                        .color(Color.BLUE.cpy())
                        .anim(anim)
                        .bounds(new Rectangle(
                            tile.bounds.x + margin,
                            tile.bounds.y + margin,
                            tile.bounds.width - 2 * margin,
                            tile.bounds.height - 2 * margin))
                        .build();
                    overlays.add(overlay);
                }
            }
        }
        return overlays;
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

    public void update(float dt) {
        // TEST ---------------
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            currentPattern = currentPattern.next__TEST();
        }
        // TEST ---------------

        screenPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        var tile = getTileAtScreenPos(screenPosition);
        if (tile != null && tile.valid) {
            hoverTile = tile;
        }

        for (int i = portalAnimations.size -1; i >= 0; i--) {
            Portal p = portalAnimations.get(i);
            p.update(dt);
            if (p.isComplete()) {
                portalAnimations.removeIndex(i);
            }
        }

        for (int i = gamePieces.size -1; i >= 0; i--) {
            GamePiece gp = gamePieces.get(i);
            gp.update(dt);
            if (gp.isDead()) {
                gamePieces.removeIndex(i);
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(gameScreen.assets.levelLayout, 0, 0, gameScreen.worldCamera.viewportWidth, gameScreen.worldCamera.viewportHeight);

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

            // draw overlay for hovered tile
            var bounds = hoverTile.bounds;
            batch.setColor(color.r, color.g, color.b, alpha);
            batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);

            // prep to draw overlays for tiles in pattern

            var tileOverlays = getTileOverlaysForPattern(hoverTile, currentPattern);
            for (var overlay : tileOverlays) {
                bounds = overlay.tile.bounds;

                color = Color.RED;
                batch.setColor(color.r, color.g, color.b, alpha);
                batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);

                color = overlay.color;
                bounds = overlay.bounds;
                batch.setColor(color.r, color.g, color.b, 1f);
                batch.draw(overlay.anim.getKeyFrame(0), bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }

        batch.setColor(Color.WHITE);

        for (GamePiece gp : gamePieces) {
            gp.render(batch);
        }
    }
}
