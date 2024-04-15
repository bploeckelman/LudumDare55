package lando.systems.ld55.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import lando.systems.ld55.Config;
import lando.systems.ld55.Main;
import lando.systems.ld55.actions.ActionBase;
import lando.systems.ld55.actions.ActionManager;
import lando.systems.ld55.actions.MoveAction;
import lando.systems.ld55.actions.SpawnAction;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.screens.GameScreen;
import lando.systems.ld55.ui.ActionQueueUI;
import lando.systems.ld55.ui.radial.RadialMenu;
import lando.systems.ld55.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class GameBoard extends InputAdapter {

    public static float marginLeft = 112;
    public static float marginRight = 112;
    public static float marginTop = 120;
    public static float marginBottom = 120;

    public static final Color gridColor = new Color(.7f, .7f, .9f, 1f);

    public GameTile hoverTile;
    public Rectangle boardRegion;
    public Array<GameTile> tiles = new Array<>();
    public Array<Portal> portalAnimations = new Array<>();
    public Array<GamePiece> gamePieces = new Array<>();
    public Spawn spawnGood;
    public Spawn spawnEvil;
    public GamePiece selectedPiece;

    // TODO - remove
    public List<TileOverlayInfo> tileOverlays;

    // track overlays for different things separately
    public final List<TileOverlayInfo> spawnTileOverlays = new ArrayList<>();
    public final List<TileOverlayInfo> moveTileOverlays = new ArrayList<>();
    public final List<TileOverlayInfo> attackTileOverlays = new ArrayList<>();
    public final List<TileOverlayInfo> goalTileOverlays = new ArrayList<>();

    public FrameBuffer gridFB;
    public Texture gridTexture;

    public final int tilesWide;
    public final int tilesHigh;
    public final GameScreen gameScreen;
    public final Vector3 screenPosition = new Vector3();

    private RadialMenu radialMenu;
    public ActionQueueUI actionQueueUI;

    private final Array<GameTile> loseTiles = new Array<>();
    private final Array<GameTile> winTiles = new Array<>();

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
        var tileSize = boardWidth / tilesWide; // should be same as (boardHeight / tilesHigh)
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

                if (tile.valid) {
                    if (x == 0)  {
                        loseTiles.add(tile);
                    } else if (x == tilesWide -1) {
                        winTiles.add(tile);
                    }
                }
            }
        }

        // setup goal tile overlays
        for (var tile : winTiles) {
            goalTileOverlays.add(new TileOverlayInfo(tile, 0)
                .addLayer("base-panel", 1f, 1, 1, 1, 0.3f, TileOverlayAssets.panelWhite, null, null)
                .addLayer("icon-laurel", 0.9f, Color.GOLD, 1f, null, TileOverlayAssets.laurel, null)
                .addLayer("icon-crown", 0.6f, Color.WHITE, 1f, null, TileOverlayAssets.crown, null)
            );
        }

        gridFB = new FrameBuffer(Pixmap.Format.RGBA8888, Config.Screen.window_width, Config.Screen.window_height, true);
        gridTexture = gridFB.getColorBufferTexture();
        gridTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        actionQueueUI = new ActionQueueUI(gameScreen.actionManager, this);
        spawnGood = new Spawn(Main.game.assets, GamePiece.Owner.Player, 180, 655);
        spawnEvil = new Spawn(Main.game.assets, GamePiece.Owner.Enemy, 1115, 195);

        removeThis();
    }

    private void removeThis() {
        var types = new GamePiece.Type[] { GamePiece.Type.Pawn, GamePiece.Type.Bishop, GamePiece.Type.Queen, GamePiece.Type.Knight, GamePiece.Type.Rook };
//        int y = 2;
//        for (var type : types) {
//            var gp = GamePiece.getGamePiece(gameScreen.assets, type, GamePiece.Owner.Enemy);
//            gp.setTile(getTileAt(14, y++));
//            gamePieces.add(gp);
//        }
//
        var y = 2;
        for (var type : types) {
            var gp = GamePiece.getGamePiece(gameScreen.assets, type, GamePiece.Owner.Enemy);
            gp.setTile(getTileAt(4, y++));
            gamePieces.add(gp);
        }
        // LET'S GO BANANA
        var gp = GamePiece.getGamePiece(gameScreen.assets, GamePiece.Type.Pawn, GamePiece.Owner.Player);
        gp.setTile(getTileAt(20, 5));
        gamePieces.add(gp);
    }

    private boolean isCornerTile(int x, int y, int cornerDepth) {
        return (x + y < cornerDepth)                   // bottom left
            || (x + (tilesHigh - y - 1) < cornerDepth) // bottom right
            || ((tilesWide - x - 1) + y < cornerDepth) // top left
            || ((tilesWide - x - 1) + (tilesHigh - y - 1) < cornerDepth); // top right
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //TODO work out the logic of when things should be clicked on when we get this sorted
        if (radialMenu != null && radialMenu.handleClick(screenX, screenY)) {
            return true;
        }
        if (hoverTile != null) {
            GamePiece gamePiece = getGamePiece(hoverTile);
            if (gamePiece != null && gamePiece.owner != GamePiece.Owner.Player) gamePiece = null;
            if (gamePiece == null) {
                if (hoverTile.summonable) {
                    radialMenu = new RadialMenu(this, hoverTile, null, RadialMenu.MenuType.Summon);
                } else {
                    // Move mode
                    if (selectedPiece != null) {
                        // Can we make a move
                        boolean moved = false;
                        for (GameTile t : selectedPiece.moveTiles){
                            if (t == hoverTile){
                                radialMenu = new RadialMenu(this, hoverTile, selectedPiece, RadialMenu.MenuType.Move);
                            }
                        }

                    }
                }
            } else {

                if (selectedPiece != gamePiece) {
                    if (selectedPiece != null) {
                        selectedPiece.deselect(this);
                    }
                    selectedPiece = gamePiece.select(this);
                    if (selectedPiece.currentAction != null && selectedPiece.currentAction instanceof MoveAction){
                        radialMenu = new RadialMenu(this, hoverTile, selectedPiece, RadialMenu.MenuType.CancelMove);
                    }
                }
            }

            return true;
        }
        return false;
    }

    public GamePiece getGamePiece(GameTile tile) {
        for (GamePiece gp : gamePieces) {
            if (gp.currentTile == tile) {
                return gp;
            }
        }
        for (ActionBase action : gameScreen.actionManager.getActionQueue()){
            if (action instanceof SpawnAction) {
                SpawnAction spawnAction = (SpawnAction) action;
                if (spawnAction.spawnTile == tile){
                    return spawnAction.getPiece();
                }
            }
        }
        return null;
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

    public List<TileOverlayInfo> getTileOverlaysForPattern(GameTile center, Pattern pattern) {
        var overlays = new ArrayList<TileOverlayInfo>();

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
                    var anim = gameScreen.assets.numbers.get(damage);
                    var region = MathUtils.randomBoolean() ? TileOverlayAssets.getRandomArrow() : TileOverlayAssets.getRandomRegion();
                    var overlay = new TileOverlayInfo(tile, damage)
                        .addLayer("base-panel", 1f, 1, 1, 1, 0.5f, TileOverlayAssets.getRandomPatch(), null, null)
//                        .addLayer("icon", 0.5f, Utils.randomColor(), null, region, null)
                        .addLayer("dmg", 0.33f, Utils.randomColor(), null, null, anim);
                        ;
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
                if (tile.bounds.contains(screenPos.x, screenPos.y) && tile.valid) {
                    return tile;
                }
            }
        }
        return null;
    }

    public void update(float dt) {
        if (gameScreen.gameOver) { return; }

        // TEST ---------------
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            spawnGood.activate();
            spawnEvil.activate();
        }
        // TEST ---------------

        refreshSummonableTiles();
        refreshMovementTiles();

        screenPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        var tile = getTileAtScreenPos(screenPosition);
        if (tile != null && tile.valid) {
            if (tile != hoverTile) {
                tileOverlays = getTileOverlaysForPattern(tile, currentPattern);
            }
            hoverTile = tile;
        } else {
            hoverTile = null;
            tileOverlays.clear();
        }

        // Check here for action hover
        actionQueueUI.update(dt);
        if (actionQueueUI.hoveredAction != null){
            hoverTile = actionQueueUI.hoveredAction.action.getPiece().currentTile;
        }

        for (int i = portalAnimations.size -1; i >= 0; i--) {
            Portal p = portalAnimations.get(i);
            p.update(dt);
            if (p.isComplete()) {
                portalAnimations.removeIndex(i);
            }
        }
        spawnGood.update(dt);
        spawnEvil.update(dt);

        for (int i = gamePieces.size -1; i >= 0; i--) {
            GamePiece gp = gamePieces.get(i);
            gp.update(dt);
            if (gp.isDead()) {
                gamePieces.removeIndex(i);
            }

            // slow down animation when landing on end tile and blow shit up
            if (gp.owner == GamePiece.Owner.Enemy) {
                if (loseTiles.contains(gp.currentTile, true)) {
                    gameScreen.gameOver(false);
                }
            } else if (winTiles.contains(gp.currentTile, true)) {
                gameScreen.gameOver(true);
            }
        }

        if (radialMenu != null) {
            radialMenu.update(dt);
            if (radialMenu.menuComplete()) {
                radialMenu = null;
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(gameScreen.assets.levelLayout, 0, 0, gameScreen.worldCamera.viewportWidth, gameScreen.worldCamera.viewportHeight);

        for (GameTile tile : tiles) {
            tile.render(batch);
        }
        renderGrid(batch);
        for (Portal p : portalAnimations) {
            p.render(batch);
        }
        spawnGood.render(batch);
        spawnEvil.render(batch);

        // Ony draw when we are in planning mode
        if (gameScreen.actionManager.getCurrentPhase() == ActionManager.Phase.CollectActions) {
            // draw tile overlays
            batch.setColor(1, 1, 1, 1);
            spawnTileOverlays.forEach(overlay -> overlay.render(batch));
            goalTileOverlays.forEach(overlay -> overlay.render(batch));
            moveTileOverlays.forEach(overlay -> overlay.render(batch));
            attackTileOverlays.forEach(overlay -> overlay.render(batch));
        }

        batch.setColor(Color.WHITE);

        for (GamePiece gp : gamePieces) {
            gp.render(batch);
        }

        // NOTE - this is showing where a possible move can go to
        if (selectedPiece != null && selectedPiece.currentAction == null ) {
            selectedPiece.renderMovement(batch);
        }

        if (radialMenu != null) {
            radialMenu.render(batch);
        }

        actionQueueUI.render(batch);
    }

    public void renderFrameBuffers(SpriteBatch batch) {
        gridFB.begin();
        ScreenUtils.clear(0f, 0f, 0f, 0f);
        batch.begin();
        for (GameTile t : tiles){
            t.renderFrameBuffer(batch);
        }
        batch.end();
        gridFB.end();
    }

    private void renderGrid(SpriteBatch batch) {
//        Gdx.gl.glEnable(GL30.GL_BLEND);
//        batch.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
        ShaderProgram shader = gameScreen.assets.gridShader;
        batch.end();
        batch.setShader(shader);
        batch.begin();
        batch.setColor(gridColor);
        shader.setUniformf("u_res", Config.Screen.window_width, Config.Screen.window_height);
        shader.setUniformf("u_mouse", screenPosition);

        batch.draw(gridTexture, 0, Config.Screen.window_height, Config.Screen.window_width, -Config.Screen.window_height);

        batch.setShader(null);
        batch.setColor(Color.WHITE);
        batch.end();
        batch.begin();
    }

    private void refreshSummonableTiles() {
        spawnTileOverlays.clear();

        for (int y = 0; y < tilesHigh; y++) {
            var summonable = true;

            for (int x = 0; x < tilesWide; x++) {
                // only consider valid tiles
                var tile = getTileAt(x, y);
                if (tile == null) continue;

                // can't summon if the tile has a piece on it
                for (var piece : gamePieces) {
                    if (piece.currentTile == tile) {
                        summonable = false;
                        break;
                    }
                }
                tile.summonable = summonable;

                // setup the overlay for this tile
                if (tile.summonable) {
                    var isRadialMenuTarget = (radialMenu != null && radialMenu.tile == tile);
                    var panelAlpha = isRadialMenuTarget ? 0.8f : 0.5f;
                    var panelPatch = isRadialMenuTarget ? TileOverlayAssets.panelGreen : TileOverlayAssets.panelBlue;
                    spawnTileOverlays.add(new TileOverlayInfo(tile, 0)
                        .addLayer("base-panel", 0.75f, 1, 1, 1, panelAlpha, panelPatch, null, null)
                        .addLayer("icon", 0.66f, Color.WHITE, null, TileOverlayAssets.pawnUp, null)
                    );
                }

                // reset flag for next tile
                summonable = false;
            }
        }
    }

    private void refreshMovementTiles() {
        moveTileOverlays.clear();
        var activeMoveLists = gameScreen.actionManager.getActiveMoveLists();
        for (var moveList : activeMoveLists) {
            for (var breadcrumb : moveList) {
                var color = breadcrumb.piece.owner == GamePiece.Owner.Player ? Color.SKY : Color.SALMON;
                var icon = TileOverlayAssets.getArrowForDir(breadcrumb.direction);
                var overlay = new TileOverlayInfo(breadcrumb.tile, 0)
                    // NOTE(brian) - panel is too much since all movement paths will be drawn each frame
                    //.addLayer("base-panel", 1f, 1, 1, 1, 0.25f, TileOverlayAssets.panelYellow, null, null)
                    .addLayer("direction-icon-shadow", 1, 0, 0, 0, 1, null, icon, null)
                    .addLayer("direction-icon", 0.8f, color.r, color.g, color.b, 1, null, icon, null)
                    ;
                moveTileOverlays.add(overlay);
            }
        }
    }
}
