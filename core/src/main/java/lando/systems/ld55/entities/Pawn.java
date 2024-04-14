package lando.systems.ld55.entities;

import lando.systems.ld55.assets.Assets;

public class Pawn extends GamePiece {

    public Pawn(Assets assets, Owner owner) {
        super(assets, owner, assets.cherry, assets.cherry, Direction.Top | Direction.Right | Direction.Bottom | Direction.Left, 1);
    }
}
