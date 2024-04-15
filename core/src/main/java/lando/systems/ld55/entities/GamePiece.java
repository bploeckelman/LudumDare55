package lando.systems.ld55.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.Stats;
import lando.systems.ld55.actions.ActionBase;
import lando.systems.ld55.actions.MoveAction;
import lando.systems.ld55.assets.Assets;
import lando.systems.ld55.assets.TileOverlayAssets;
import lando.systems.ld55.screens.GameScreen;
import lando.systems.ld55.ui.HealthBar;
import lando.systems.ld55.ui.MovementBreadcrumb;

public class GamePiece {
    public enum Owner {
        Player(Color.CYAN),       // alternatives: LIGHTBLUE, BLUE
        Enemy(Color.SALMON);  // alternatives: FIREBRICK, CORAL, RED
        public final Color color;
        Owner(Color color) {
            this.color = color;
        }
    }
    public enum Type {
        Pawn(1, 1,
            Direction.Top | Direction.Right | Direction.Bottom,
            Direction.Top | Direction.Left | Direction.Bottom),
        Knight(2, 1,
            Direction.Top | Direction.TopRight | Direction.Right | Direction.BottomRight | Direction.Bottom,
            Direction.Top | Direction.TopLeft | Direction.Left | Direction.BottomLeft | Direction.Bottom),
        Bishop(3, 2,
            Direction.TopRight | Direction.BottomRight,
            Direction.TopLeft | Direction.BottomLeft),
        Rook(4, 3,
            Direction.Top | Direction.Right | Direction.Bottom | Direction.Left,
            Direction.Top | Direction.Right | Direction.Bottom | Direction.Left),
        Queen(5, 4,
            Direction.TopLeft | Direction.Top | Direction.TopRight | Direction.Right | Direction.BottomRight | Direction.Bottom | Direction.BottomLeft | Direction.Left,
            Direction.TopLeft | Direction.Top | Direction.TopRight | Direction.Right | Direction.BottomRight | Direction.Bottom | Direction.BottomLeft | Direction.Left);

        public final int defaultMaxHealth; //called default Max Health because they can potentially be leveled up
        public final int actionsToSpawn;
        public final int directionPlayer;
        public final int directionEnemy;
        Type(int defaultMaxHealth, int actionsToSpawn, int directionPlayer, int directionEnemy) {
            this.defaultMaxHealth = defaultMaxHealth;
            this.actionsToSpawn = actionsToSpawn;
            this.directionPlayer = directionPlayer;
            this.directionEnemy = directionEnemy;
        }

        public static Type random() {
            var types = values();
            return types[MathUtils.random(types.length - 1)];
        }
    }

    public static GamePiece getRandom(Assets assets, Owner owner) {
        return getGamePiece(assets, Type.random(), owner);
    }

    public static GamePiece getGamePiece(Assets assets, Type type, Owner owner) {
        // for now choose alignment based on owner
        var alignment = owner == Owner.Player ? 0 : 1;
        var movement = 0;
        var maxHealth = type.defaultMaxHealth;
        var direction = (owner == Owner.Player)
            ? type.directionPlayer
            : type.directionEnemy;
        Array<Animation<TextureRegion>> animGroup;
        switch (type) {
            case Knight:
                animGroup = assets.knight.get(alignment);
                movement = 6;
                break;
            case Bishop:
                animGroup = assets.bishop.get(alignment);
                movement = 10;
                break;
            case Rook:
                animGroup = assets.rook.get(alignment);
                movement = 10;
                break;
            case Queen:
                animGroup = assets.queen.get(alignment);
                movement = 10;
                break;
            case Pawn:
            default:
                animGroup = assets.pawn.get(alignment);
                movement = 4;
                break;
        }
        return new GamePiece(assets, owner, animGroup, direction, movement, maxHealth, type);
    }

    public final int TILE_OFFSET_Y = 10;
    public final static float moveSeconds = 0.4f; // seconds

    public Owner owner;

    private final Assets assets;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> attack;
    public final Animation<TextureRegion> portrait;
    private boolean isAttacking = false;
    private float attackTime = 0;
    private final int directions;
    private final int maxMovement;
    public Array<GameTile> moveTiles = new Array<>();

    private float animState = 0;
    private float selectedAnimState = 0;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion keyframe;

    private final Rectangle bounds = new Rectangle();
    public final Vector2 position = new Vector2();
    private boolean selected = false;
    public Pattern pattern;
    public Type type;

    public GameTile currentTile;

    private GameTile moveTile;
    private final Vector2 startPosition = new Vector2();
    private final Vector2 movePosition = new Vector2();
    public boolean isMoving = false;
    private float moveAnimState = 0;

    public ActionBase currentAction;
    public int maxHealth;
    public int currentHealth;
    public boolean summoning;

    public HealthBar healthBar;

    public GamePiece(Assets assets, Owner owner, Array<Animation<TextureRegion>> animGroup, int directions, int maxMovement, int maxHealth, Type type) {
        this.owner = owner;
        this.type = type;
        this.assets = assets;
        this.idle = animGroup.get(0);
        this.attack = animGroup.get(1);
        this.portrait = animGroup.get(2);
        setCurrentAnimation(this.idle);

        this.directions = directions;
        this.maxMovement = maxMovement;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.summoning = true;
        healthBar = new HealthBar(assets, position.x, position.y + 60f, maxHealth);
        switch (type) {
            case Pawn:
                pattern = Pattern.PAWN_ATK;
                break;
            case Knight:
                pattern = Pattern.KNIGHT_ATK;
                break;
            case Bishop:
                pattern = Pattern.BISHOP_ATK;
                break;
            case Rook:
                pattern = Pattern.ROOK_ATK;
                break;
            case Queen:
                pattern = Pattern.QUEEN_ATK;
                break;
        }
    }

    private void setCurrentAnimation(Animation<TextureRegion> animation) {
        currentAnimation = animation;
        animState = 0;
        keyframe = this.currentAnimation.getKeyFrame(animState);
        bounds.setWidth(keyframe.getRegionWidth());
        bounds.setHeight(keyframe.getRegionHeight());
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public boolean canRemove() {
        return isDead() && bloodDuration <= 0;
    }

    public void takeDamage(int amount, GameBoard board) {
        currentHealth -= amount;
        // TODO: make something cool happen here, health bar floats away as a particle maybe?
        healthBar.updateCurrentHealth(currentHealth);
        if (currentHealth <= 0){
            if (owner == Owner.Player){
                Stats.playerUnitsKilled++;
            } else {
                Stats.enemyUnitsKilled++;
            }
        }

        if (currentHealth <= 0 && currentAction != null){
            board.gameScreen.actionManager.removeAction(currentAction);
            currentAction = null;
        }

        // make the bar brighter
        healthAlpha = 1f;
    }

    public void attack(GamePiece attackedPiece) {
        // TODO: do types attack particles and sounds
        setCurrentAnimation(attack);
        attackTime = attack.getAnimationDuration();
        isAttacking = true;

        switch (type) {
            case Pawn:
                GameScreen.particles.spawnSwordSlash(position.x, position.y + 5f, attackedPiece.position.x, attackedPiece.position.y + 5f);
                break;
            case Knight:
                GameScreen.particles.spawnSwordSlash(position.x, position.y + 5f, attackedPiece.position.x, attackedPiece.position.y + 5f);
                break;
            case Rook:
                GameScreen.particles.spawnArrow(position.x, position.y + 5f, attackedPiece.position.x, attackedPiece.position.y + 5f);
                break;
            case Queen:
                GameScreen.particles.spawnMagic(position.x, position.y + 5f, attackedPiece.position.x, attackedPiece.position.y + 5f);
                break;
            case Bishop:
                GameScreen.particles.spawnFireball(position.x, position.y + 5f, attackedPiece.position.x, attackedPiece.position.y + 5f);
                break;
            default:
                GameScreen.particles.spawnSwordSlash(position.x, position.y + 5f, attackedPiece.position.x, attackedPiece.position.y + 5f);
                break;
        }
    }

    public GamePiece select(GameBoard gameBoard) {
        selected = true;
        selectedAnimState = 0;
        addMoveTiles(gameBoard);
        setupMoveOverlay(gameBoard);
        return this;
    }

    public GamePiece deselect(GameBoard gameBoard) {
        selected = false;
        selectedAnimState = 0;
        moveTiles.clear();
        gameBoard.playerMoveOverlay.clear();
        gameBoard.selectedPiece = null;
        gameBoard.playerMoveOverlay.clear();
        return this;
    }

//    public GamePiece toggleSelect(GameBoard gameBoard) {
//        if (isMoving) return null;
//
//        selectedAnimState = 0;
//        selected = !selected;
//        if (selected) {
//            addMoveTiles(gameBoard);
//        } else {
//            moveTiles.clear();
//        }
//
//        return selected ? this : null;
//    }

    public void setTile(GameTile tile) {
        currentTile = tile;
        setPosition(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + TILE_OFFSET_Y);
        selected = false;
        isMoving = false;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x - bounds.width / 2, y);
        healthBar.updatePosition(position.x, position.y - healthBar.barBounds.height);
    }

    public void moveToTile(GameTile tile) {
        moveTile = tile;
        startPosition.set(position);
        movePosition.set(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + TILE_OFFSET_Y);
        moveAnimState = 0;

        selected = false;
        isMoving = true;
    }

    public void update(float dt) {
        animState += dt;
        keyframe = currentAnimation.getKeyFrame(animState);
        if (selected) {
            selectedAnimState += dt;
        }
        if (isAttacking) {
            if (animState > attackTime + 0.25f) {
                isAttacking = false;
                setCurrentAnimation(idle);
            }
        }
        if (isMoving) {
            // TODO: handle a collision (melee attack) here
            updateMovement(dt);
        }

        updateBlood(dt);

        if (currentAction != null && currentAction.isCompleted()){
            currentAction = null;
        }

        adjustFocus(dt);
        healthBar.updateCurrentHealth(currentHealth);
    }

    private void updateMovement(float dt) {
        moveAnimState += dt;
        if (moveAnimState > moveSeconds) {
            moveAnimState = moveSeconds;
            setTile(moveTile);
        }
        position.set(startPosition);
        position.interpolate(movePosition, moveAnimState / moveSeconds, Interpolation.linear);
        setPosition(position.x, position.y);
    }

    public void render(SpriteBatch batch) {
        if (currentTile == null) return;

        float flip = owner == Owner.Enemy ? -1 : 1;

        float yOffset = getYOffset();
        batch.draw(keyframe, bounds.x, bounds.y + yOffset, bounds.width / 2, bounds.height / 2, bounds.width, bounds.height, flip, 1, 0);

        if (currentAction != null && currentAction instanceof MoveAction) {
            currentAction.render(batch);
        }

        if (!isDead()) {
            healthBar.render(batch, healthAlpha);
        }
        //TEST
//        if (isAttacking){
//            batch.setColor(Color.RED);
//            batch.draw(Main.game.assets.pixelRegion, bounds.x, bounds.y, bounds.width, bounds.height);
//            batch.setColor(Color.WHITE);
//        }
        //TEST
    }

    public void renderMovement(SpriteBatch batch) {
        var alpha = 0.7f;
        for (var t : moveTiles) {
            var bounds = t.bounds;

            var color = Color.RED;
            batch.setColor(color.r, color.g, color.b, alpha);
            batch.draw(assets.whitePixel, bounds.x, bounds.y, bounds.width, bounds.height);
        }

        batch.setColor(Color.WHITE);
    }

    private float getYOffset() {
        float anim = 0;
        float yAdjust = 1;
        if (selected) {
            anim = selectedAnimState * 4;
            yAdjust = TILE_OFFSET_Y / 2f;
        } else if (isMoving) {
            anim = moveAnimState / moveSeconds * MathUtils.PI;
            yAdjust = 30;
        }
        return yAdjust * MathUtils.sin(anim);
    }

    public void addMoveTiles(GameBoard gameBoard) {
        if (currentTile == null) { return; }

        for (int i = 0; i < maxMovement; i++) {
            if (hasDirection(Direction.Top)) {
                addMoveTile(gameBoard,0, i + 1);
            }

            if (hasDirection(Direction.TopRight)) {
                addMoveTile(gameBoard,i + 1, i + 1);
            }

            if (hasDirection(Direction.Right)) {
                addMoveTile(gameBoard,i + 1, 0);
            }

            if (hasDirection(Direction.BottomRight)) {
                addMoveTile(gameBoard,i + 1, -i - 1);
            }

            if (hasDirection(Direction.Bottom)) {
                addMoveTile(gameBoard,0, -i - 1);
            }

            if (hasDirection(Direction.BottomLeft)) {
                addMoveTile(gameBoard,-i - 1, -i - 1);
            }

            if (hasDirection(Direction.Left)) {
                addMoveTile(gameBoard,-i - 1, 0);
            }

            if (hasDirection(Direction.TopLeft)) {
                addMoveTile(gameBoard,-i - 1, i + 1);
            }
        }
    }

    private void setupMoveOverlay(GameBoard gameBoard) {
        gameBoard.playerMoveOverlay.clear();

        for (GameTile tile : moveTiles)
            gameBoard.playerMoveOverlay.add(new TileOverlayInfo(tile, 0)
            .addLayer("base-panel", 1f, 1, 1, 1, 0.4f, TileOverlayAssets.panelWhite, null, null));

    }

    public void setupPathOverlay(GameBoard gameBoard, GameTile targetTile) {
        for (TileOverlayInfo info : gameBoard.playerMoveOverlay) {
            info.removeLayer("path");
        }
        if (targetTile == null){
            return;
        }
        if (moveTiles.contains(targetTile, true)){
            // On available Move path
            int dX = (int)Math.signum(targetTile.x - currentTile.x);
            int dY = (int)Math.signum(targetTile.y - currentTile.y);
            int nextX = dX + currentTile.x;
            int nextY = dY + currentTile.y;

            while (dX != 0 || dY != 0) {
                GameTile t = gameBoard.getTileAt(nextX, nextY);
                dX = (int) Math.signum(targetTile.x - nextX);
                dY = (int) Math.signum(targetTile.y - nextY);
                nextX = dX + nextX;
                nextY = dY + nextY;

                for (TileOverlayInfo info : gameBoard.playerMoveOverlay){
                    if (info.tile == t){
                        info.addLayer("path", 1f, 1, 1, 0, 1f, null, TileOverlayAssets.getArrowForDir(MovementBreadcrumb.getDirectionFrom(dX, dY)), null);
                    }
                }
            }


        }
    }

    private void addMoveTile(GameBoard gameBoard, int x, int y) {
        GameTile tile = gameBoard.getTileRelative(currentTile, x, y);
        if (tile != null && tile.valid) {
            moveTiles.add(tile);
        }
    }

    private boolean hasDirection(int direction) {
        return (directions & direction) == direction;
    }

    private float healthAlpha = 0;
    private boolean hasFocus = false;
    public void setFocus(boolean focus) {
        hasFocus = focus;
    }

    private void adjustFocus(float dt) {
        if (!hasFocus) {
            dt = -dt * 2;
        } else {
            dt *= 4;
        }

        float alpha = healthAlpha + dt;
        healthAlpha = MathUtils.clamp(alpha, 0.6f, 1f);
    }

    private boolean normalBlood = false;
    private float bloodDuration = 0;
    private void updateBlood(float dt) {
        if (normalBlood) {
            GameScreen.particles.bloodBurst(position.x, position.y + bounds.height / 2);
            normalBlood = false;
        }

        bloodDuration -= dt;
        if (bloodDuration < 0) return;
        Gdx.app.log("test", ""+bloodDuration);

        GameScreen.particles.bloodFountain(position.x, position.y + bounds.height / 2);
    }
    public void bleed() {
        GameScreen.particles.spawnBloodPuddle(position.x, position.y);
        if (currentHealth <= 0) {
            bloodDuration = 2.5f;
        } else {
            normalBlood = true;
        }
    }
}
