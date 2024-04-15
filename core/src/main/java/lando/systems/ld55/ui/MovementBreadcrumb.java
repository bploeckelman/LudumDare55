package lando.systems.ld55.ui;

import lando.systems.ld55.entities.GameTile;

public class MovementBreadcrumb {
    public enum Direction {Up, UpRight, Right, DownRight, Down, DownLeft, Left, UpLeft, End}

    public GameTile tile;
    public Direction direction;

    public MovementBreadcrumb(GameTile tile, int dX, int dY) {
        this.tile = tile;
        if (dX == 0 && dY == 0) direction = Direction.End;
        if (dX == 0 && dY == 1) direction = Direction.Up;
        if (dX == 1 && dY == 1) direction = Direction.UpRight;
        if (dX == 1 && dY == 0) direction = Direction.Right;
        if (dX == 1 && dY == -1) direction = Direction.DownRight;
        if (dX == 0 && dY == -1) direction = Direction.Down;
        if (dX == -1 && dY == -1) direction = Direction.DownLeft;
        if (dX == -1 && dY == 0) direction = Direction.Left;
        if (dX == -1 && dY == 0) direction = Direction.UpLeft;
    }

}