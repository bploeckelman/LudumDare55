package lando.systems.ld55.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lando.systems.ld55.actions.ActionBase;
import lando.systems.ld55.actions.MoveAction;
import lando.systems.ld55.assets.Assets;

public class GamePiece {
    public enum Owner {Player, Enemy}
    public enum Type {
        Pawn, Knight, Bishop, Rook, Queen;

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
        var direction = Direction.Left;
        var movement = 0;
        Array<Animation<TextureRegion>> animGroup;
        switch (type) {
            case Knight:
                animGroup = assets.knight.get(alignment);
                direction = (owner == Owner.Player)
                    ? Direction.Top | Direction.TopRight | Direction.Right | Direction.BottomRight | Direction.Bottom
                    : Direction.Top | Direction.TopLeft | Direction.Left | Direction.BottomLeft | Direction.Bottom;
                movement = 3;
                break;
            case Bishop:
                animGroup = assets.bishop.get(alignment);
                direction = (owner == Owner.Player)
                    ? Direction.TopRight | Direction.BottomRight
                    : Direction.TopLeft | Direction.BottomLeft;
                movement = 10;
                break;
            case Rook:
                animGroup = assets.rook.get(alignment);
                direction = Direction.Top | Direction.Right | Direction.Bottom | Direction.Left;
                movement = 10;
                break;
            case Queen:
                animGroup = assets.queen.get(alignment);
                direction = Direction.TopLeft | Direction.Top | Direction.TopRight | Direction.Right | Direction.BottomRight | Direction.Bottom | Direction.BottomLeft | Direction.Left;
                movement = 10;
                break;
            case Pawn:
            default:
                animGroup = assets.pawn.get(alignment);
                direction = (owner == Owner.Player)
                    ? Direction.Top | Direction.Right | Direction.Bottom
                    : Direction.Top | Direction.Left | Direction.Bottom;
                movement = 1;
                break;
        }
        return new GamePiece(assets, owner, animGroup.get(0), animGroup.get(1), direction, movement);
    }

    private final int TILE_OFFSET_Y = 10;
    public Owner owner;

    private final Assets assets;
    private final Animation<TextureRegion> idle;
    private final Animation<TextureRegion> attack;
    private final int directions;
    private final int maxMovement;
    private Array<GameTile> moveTiles = new Array<>();

    private float animState = 0;
    private float selectedAnimState = 0;
    private Animation<TextureRegion> currentAnimation;
    private TextureRegion keyframe;

    private final Rectangle bounds = new Rectangle();
    private final Vector2 position = new Vector2();
    private boolean selected = false;

    public GameTile currentTile;

    private GameTile moveTile;
    private final Vector2 startPosition = new Vector2();
    private final Vector2 movePosition = new Vector2();
    public boolean isMoving = false;
    private float moveAnimState = 0;
    private float moveSeconds = 1; // seconds

    public ActionBase currentAction;

    public GamePiece(Assets assets, Owner owner, Animation<TextureRegion> idle, Animation<TextureRegion> attack, int directions, int maxMovement) {
        this.owner = owner;
        this.assets = assets;
        this.idle = idle;
        this.attack = attack;
        setCurrentAnimation(this.idle);

        this.directions = directions;
        this.maxMovement = maxMovement;
    }

    private void setCurrentAnimation(Animation<TextureRegion> animation) {
        currentAnimation = animation;
        animState = 0;
        keyframe = this.currentAnimation.getKeyFrame(animState);
        bounds.setWidth(keyframe.getRegionWidth());
        bounds.setHeight(keyframe.getRegionHeight());
    }

    public boolean isDead() {
        return false;
    }

    public GamePiece toggleSelect(GameBoard gameBoard) {
        if (isMoving) return null;

        selectedAnimState = 0;
        selected = !selected;
        if (selected) {
            addMoveTiles(gameBoard);
        } else {
            moveTiles.clear();
        }

        return selected ? this : null;
    }

    public void setTile(GameTile tile) {
        currentTile = tile;
        setPosition(tile.bounds.x + tile.bounds.width / 2, tile.bounds.y + TILE_OFFSET_Y);
        selected = false;
        isMoving = false;
    }

    private void setPosition(float x, float y) {
        position.set(x, y);
        bounds.setPosition(x - bounds.width / 2, y);
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
        if (isMoving) {
            // TODO: handle a collision (melee attack) here
            updateMovement(dt);
        }
        if (currentAction != null && currentAction.isCompleted()){
            currentAction = null;
        }
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

        float yOffset = getYOffset();
        batch.draw(keyframe, bounds.x, bounds.y + yOffset, bounds.width / 2, bounds.height / 2, bounds.width, bounds.height, 1, 1, 0);

        if (currentAction != null && currentAction instanceof MoveAction) {
            currentAction.render(batch);
        }
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

    private void addMoveTiles(GameBoard gameBoard) {
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
                addMoveTile(gameBoard,i + 1, -i - 1);
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
}
